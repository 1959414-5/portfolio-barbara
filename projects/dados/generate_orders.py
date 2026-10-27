"""Gera pedidos de exemplo 100% sintéticos e determinísticos.
Executar: python generate_orders.py
Saída: orders_raw.csv (250 linhas, incluindo duplicidades e valores inválidos).
"""
from __future__ import annotations

import csv
import random
from datetime import date, timedelta
from pathlib import Path

RNG = random.Random(20261027)
ROOT = Path(__file__).resolve().parent
OUTPUT = ROOT / "orders_raw.csv"


def fmt_date(value: date) -> str:
    formats = ("%d/%m/%Y", "%Y-%m-%d", "%d-%m-%Y")
    return value.strftime(RNG.choice(formats))


def fmt_currency(value: float) -> str:
    style = RNG.randrange(4)
    if style == 0:
        return f"R$ {value:,.2f}".replace(",", "X").replace(".", ",").replace("X", ".")
    if style == 1:
        return f"{value:.2f}"
    if style == 2:
        return f"{value:,.2f}".replace(",", "X").replace(".", ",").replace("X", ".")
    return f"R$ {value:.2f}"


def create_order(number: int, kind: str, late: bool = False) -> dict[str, str]:
    if kind == "delivered":
        order_date = date(2025, 1, 2) + timedelta(days=RNG.randint(0, 70))
        promised = order_date + timedelta(days=RNG.randint(5, 10))
        delivered = promised + timedelta(days=RNG.randint(1, 12)) if late else promised - timedelta(days=RNG.randint(0, 4))
        status = "Entregue"
    elif kind == "overdue":
        order_date = date(2025, 1, 2) + timedelta(days=RNG.randint(0, 45))
        promised = order_date + timedelta(days=RNG.randint(3, 10))
        delivered = None
        status = "Em aberto"
    else:
        order_date = date(2025, 3, 27) + timedelta(days=RNG.randint(0, 4))
        promised = order_date + timedelta(days=RNG.randint(6, 10))
        delivered = None
        status = "Em aberto"

    regions = {
        "Sul": ["Sul", "SUL", " sul ", "REGIÃO SUL"],
        "Sudeste": ["Sudeste", "SUDESTE", " sudeste "],
        "Centro-Oeste": ["Centro-Oeste", "CENTRO OESTE", "centro-oeste"],
        "Nordeste": ["Nordeste", "NORDESTE", " nordeste "],
    }
    region = RNG.choice(list(regions))
    statuses = {
        "Entregue": ["Entregue", "ENTREGUE", "entregue ", "DELIVERED"],
        "Em aberto": ["Em aberto", "EM ABERTO", "pendente", "ABERTO"],
    }
    amount = round(RNG.uniform(450, 18500), 2)
    return {
        "order_id": f"PED-{number:04d}",
        "client_id": f"CLI-{RNG.randint(1, 45):03d}",
        "seller_id": f"VEND-{RNG.randint(1, 8):02d}",
        "region": RNG.choice(regions[region]),
        "order_date": fmt_date(order_date),
        "promised_date": fmt_date(promised),
        "delivered_date": fmt_date(delivered) if delivered else "",
        "value": fmt_currency(amount),
        "status": RNG.choice(statuses[status]),
    }


def main() -> None:
    rows = []
    order_no = 1
    # 183 entregues: 58 atrasadas e 125 no prazo.
    for i in range(183):
        rows.append(create_order(order_no, "delivered", late=i < 58))
        order_no += 1
    for _ in range(40):
        rows.append(create_order(order_no, "overdue"))
        order_no += 1
    for _ in range(10):
        rows.append(create_order(order_no, "open"))
        order_no += 1

    # Sete registros inválidos com IDs exclusivos.
    for i in range(7):
        bad = create_order(order_no, "open")
        bad["order_id"] = f"INV-{i + 1:03d}"
        bad["value"] = "valor indisponível" if i < 4 else "-50,00"
        rows.append(bad)
        order_no += 1

    # Dez duplicações exatas para testar a regra de deduplicação.
    rows.extend(dict(row) for row in rows[:10])
    assert len(rows) == 250
    RNG.shuffle(rows)
    fields = ["order_id", "client_id", "seller_id", "region", "order_date", "promised_date", "delivered_date", "value", "status"]
    with OUTPUT.open("w", newline="", encoding="utf-8-sig") as file:
        writer = csv.DictWriter(file, fieldnames=fields)
        writer.writeheader()
        writer.writerows(rows)
    print(f"Arquivo criado: {OUTPUT} ({len(rows)} linhas de dados sintéticos)")


if __name__ == "__main__":
    main()
