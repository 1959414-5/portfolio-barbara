# Medidas DAX sugeridas

Importe output/orders_clean.csv para o Power BI e nomeie a tabela como Orders.

\`\`\`dax
Pedidos = COUNTROWS(Orders)

Pedidos Entregues =
CALCULATE([Pedidos], Orders[status] = "Entregue")

Pedidos Entregues Atrasados =
CALCULATE(
    [Pedidos],
    Orders[status] = "Entregue",
    Orders[days_late] > 0
)

Taxa de Atraso (%) =
DIVIDE([Pedidos Entregues Atrasados], [Pedidos Entregues], 0)

Pedidos em Aberto Vencidos =
CALCULATE(
    [Pedidos],
    Orders[status] = "Em aberto",
    Orders[pending_overdue] = 1
)

Média de Dias de Atraso =
AVERAGEX(
    FILTER(Orders, Orders[status] = "Entregue" && Orders[days_late] > 0),
    Orders[days_late]
)

Valor em Aberto Vencido =
CALCULATE(
    SUM(Orders[value_brl]),
    Orders[status] = "Em aberto",
    Orders[pending_overdue] = 1
)
\`\`\`

## Verificações
- Formate Taxa de Atraso (%) como porcentagem.
- Use region como segmentador; status deve separar entregas de pendências.
- Confira: Pedidos Entregues = 183, Pedidos Entregues Atrasados = 58 e Pedidos em Aberto Vencidos = 40 após rodar o pipeline.
- Não trate a taxa como evidência causal; ela é descritiva.
