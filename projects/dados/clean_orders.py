"""Limpa os dados sintéticos e gera um relatório auditável."""
from __future__ import annotations

import json
import re
import unicodedata
from pathlib import Path

import pandas as pd

ROOT = Path(__file__).resolve().parent
INPUT = ROOT / "orders_raw.csv"
OUT_DIR = ROOT / "output"
OUT_CSV = OUT_DIR / "orders_clean.csv"
OUT_JSON = OUT_DIR / "quality_report.json"
AS_OF = pd.Timestamp("2025-04-01")


def normalize(value: object) -> str:
    if pd.isna(value):
        return ""
    text = unicodedata.normalize("NFKD", str(value))
    text = "".join(ch for ch in text if not unicodedata.combining(ch))
    return re.sub(r"\s+", " ", text).strip().upper()


def parse_brl(value: object) -> float:
    if pd.isna(value):
        return float("nan")
    text = str(value).strip().upper().replace("R$", "").replace(" ", "")
    if not text:
        return float("nan")
    if "," in text:
        text = text.replace(".", "").replace(",", ".")
    try:
        return float(text)
    except ValueError:
        return float("nan")


def parse_dates(values: pd.Series) -> pd.Series:
    return pd.to_datetime(values, format="mixed", dayfirst=True, errors="coerce")


def run() -> dict[str, object]:
    if not INPUT.exists():
        raise FileNotFoundError("Execute primeiro python generate_orders.py")
    raw = pd.read_csv(INPUT, dtype=str, keep_default_na=False)
    raw_rows = len(raw)
    raw["order_id"] = raw["order_id"].astype(str).str.strip()
    deduped = raw.drop_duplicates(subset=["order_id"], keep="first").copy()
    duplicates_removed = raw_rows - len(deduped)

    deduped["value_brl"] = deduped["value"].map(parse_brl)
    valid = deduped["order_id"].ne("") & deduped["value_brl"].notna() & deduped["value_brl"].gt(0)
    rejected = int((~valid).sum())
    clean = deduped.loc[valid].copy()

    region_map = {
        "SUL": "Sul", "REGIAO SUL": "Sul", "SUDESTE": "Sudeste",
        "CENTRO OESTE": "Centro-Oeste", "CENTRO-OESTE": "Centro-Oeste", "NORDESTE": "Nordeste",
    }
    clean["region"] = clean["region"].map(lambda v: region_map.get(normalize(v), "Não classificada"))
    status_map = {
        "ENTREGUE": "Entregue", "DELIVERED": "Entregue",
        "EM ABERTO": "Em aberto", "PENDENTE": "Em aberto", "ABERTO": "Em aberto",
    }
    clean["status"] = clean["status"].map(lambda v: status_map.get(normalize(v), "Não classificado"))

    for col in ("order_date", "promised_date", "delivered_date"):
        clean[col] = parse_dates(clean[col])
    clean["date_quality_issue"] = (
        clean["order_date"].isna() | clean["promised_date"].isna()
        | ((clean["status"] == "Entregue") & clean["delivered_date"].isna())
    )

    delivered = clean["status"].eq("Entregue") & clean["delivered_date"].notna() & clean["promised_date"].notna()
    clean["days_late"] = 0
    clean.loc[delivered, "days_late"] = (
        clean.loc[delivered, "delivered_date"] - clean.loc[delivered, "promised_date"]
    ).dt.days.clip(lower=0)
    clean["pending_overdue"] = (
        clean["status"].eq("Em aberto") & clean["promised_date"].notna() & (clean["promised_date"] < AS_OF)
    ).astype(int)

    for col in ("order_date", "promised_date", "delivered_date"):
        clean[col] = clean[col].dt.strftime("%Y-%m-%d").fillna("")

    columns = [
        "order_id", "client_id", "seller_id", "region", "order_date", "promised_date",
        "delivered_date", "value_brl", "status", "days_late", "pending_overdue", "date_quality_issue"
    ]
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    result = clean[columns].copy()
    result.to_csv(OUT_CSV, index=False, encoding="utf-8-sig", float_format="%.2f")

    delivered_count = int(result["status"].eq("Entregue").sum())
    late_count = int(((result["status"] == "Entregue") & (result["days_late"] > 0)).sum())
    report = {
        "synthetic_data": True, "as_of_date": AS_OF.strftime("%Y-%m-%d"),
        "raw_rows": raw_rows, "duplicate_rows_removed": duplicates_removed,
        "rows_rejected_by_validation": rejected, "clean_rows": len(result),
        "delivered_orders": delivered_count, "delivered_late": late_count,
        "delivered_on_time": delivered_count - late_count,
        "late_rate_among_delivered_pct": round(100 * late_count / delivered_count, 1) if delivered_count else 0.0,
        "open_overdue": int(result["pending_overdue"].sum()),
        "open_not_overdue": int(((result["status"] == "Em aberto") & (result["pending_overdue"] == 0)).sum()),
        "date_quality_issues_flagged": int(result["date_quality_issue"].sum()),
        "notes": ["Todos os registros são sintéticos e gerados com seed fixa.",
                  "Atrasos já ocorridos e pendências vencidas são categorias diferentes.",
                  "A taxa é descritiva; não demonstra causalidade."]
    }
    OUT_JSON.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(report, ensure_ascii=False, indent=2))
    print(f"CSV limpo: {OUT_CSV}")
    return report


if __name__ == "__main__":
    run()
