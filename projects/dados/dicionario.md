# Dicionário de dados

Todos os registros são gerados por generate_orders.py com seed fixa. Nenhum dado vem de cliente ou empresa real.

| Campo | Tipo após limpeza | Significado | Regras |
|---|---|---|---|
| order_id | texto | Identificador fictício do pedido | Obrigatório e único |
| client_id | texto | Código fictício do cliente | Sem relação com clientes reais |
| seller_id | texto | Código fictício do vendedor | Identificador VEND-xx |
| region | categoria | Região comercial fictícia | Sul, Sudeste, Centro-Oeste ou Nordeste |
| order_date | data ISO | Data de criação | YYYY-MM-DD |
| promised_date | data ISO | Prazo prometido | Usada para calcular atraso |
| delivered_date | data ISO/vazio | Data de entrega | Vazia quando ainda está em aberto |
| value_brl | decimal | Valor fictício do pedido | Deve ser numérico e maior que zero |
| status | categoria | Estado do pedido | Entregue ou Em aberto |
| days_late | inteiro | Dias corridos após o prazo | Calculado para pedidos entregues |
| pending_overdue | inteiro 0/1 | Pedido em aberto com prazo vencido | Avaliado em 01/04/2025 |
| date_quality_issue | booleano | Sinal de datas inválidas | Deve ser investigado, não corrigido silenciosamente |

## Problemas intencionais
- Dez linhas duplicadas.
- Sete registros com valor inválido ou negativo.
- Variações de caixa, espaços e grafia de região/status.
- Datas em mais de um formato.
- Valores monetários com formatos brasileiros e decimais.

## Cuidados analíticos
- Atrasos de pedidos entregues e pendências abertas vencidas são medidas distintas.
- A taxa de atraso considera apenas pedidos entregues.
- Uma região com mais atrasos não indica, por si só, que a região causou o atraso.
- A análise usa dias corridos, não dias úteis.
