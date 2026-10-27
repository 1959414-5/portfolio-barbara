-- Todas as consultas usam dados 100% sintéticos; tabela orders_clean importada do CSV tratado.
-- 1. Taxa de atraso entre os pedidos entregues.
SELECT COUNT(*) AS pedidos_entregues,
       SUM(CASE WHEN days_late > 0 THEN 1 ELSE 0 END) AS entregues_atrasados,
       ROUND(100.0 * SUM(CASE WHEN days_late > 0 THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 1) AS taxa_atraso_pct
FROM orders_clean WHERE status = 'Entregue';

-- 2. Pedidos em aberto com prazo vencido na data de referência.
SELECT order_id, region, seller_id, promised_date, value_brl,
       CAST(julianday('2025-04-01') - julianday(promised_date) AS INTEGER) AS days_past_due
FROM orders_clean WHERE status = 'Em aberto' AND pending_overdue = 1
ORDER BY days_past_due DESC, value_brl DESC;

-- 3. Atrasos por região. Essa segmentação é descritiva e não prova causalidade.
SELECT region, COUNT(*) AS delivered_orders,
       SUM(CASE WHEN days_late > 0 THEN 1 ELSE 0 END) AS late_orders,
       ROUND(AVG(days_late), 1) AS avg_late_days
FROM orders_clean WHERE status = 'Entregue'
GROUP BY region ORDER BY late_orders DESC;

-- 4. Faixas de atraso.
SELECT CASE WHEN days_late = 0 THEN 'No prazo'
            WHEN days_late BETWEEN 1 AND 3 THEN '1-3 dias'
            WHEN days_late BETWEEN 4 AND 7 THEN '4-7 dias'
            ELSE '8+ dias' END AS delay_band,
       COUNT(*) AS orders_count
FROM orders_clean WHERE status = 'Entregue'
GROUP BY delay_band ORDER BY orders_count DESC;

-- 5. Valor fictício em aberto vencido por região.
SELECT region, COUNT(*) AS overdue_open_orders, ROUND(SUM(value_brl), 2) AS value_at_risk
FROM orders_clean WHERE status = 'Em aberto' AND pending_overdue = 1
GROUP BY region ORDER BY value_at_risk DESC;
