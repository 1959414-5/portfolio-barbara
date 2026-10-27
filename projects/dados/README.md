# Estudo de caso — Atrasos de pedidos (100% sintético)

## Pergunta de negócio
Quais pedidos precisam de ação agora e qual foi a frequência de atraso entre os pedidos já entregues?

O projeto demonstra um fluxo de análise completo: geração de dados imperfeitos, limpeza reproduzível, validação de qualidade, consulta SQL, interpretação e recomendação. Não são dados de qualquer empresa real.

## Ferramentas
- Python 3.10+ e Pandas 2+
- SQL compatível com SQLite
- Power BI Desktop (opcional para visualização)

## Como reproduzir
Na pasta projects/dados, execute:
1. python -m pip install -r requirements.txt
2. python generate_orders.py
3. python clean_orders.py

A execução cria:
- orders_raw.csv: 250 linhas com duplicações, variações de formato e valores inválidos intencionais.
- output/orders_clean.csv: 233 pedidos válidos e normalizados.
- output/quality_report.json: contagem de linhas, duplicidades e registros rejeitados.

Data de referência para pedidos em aberto vencidos: 01/04/2025. A data é fixa para manter os resultados reproduzíveis.

## Resultados esperados
- 250 registros na entrada.
- 10 duplicidades removidas.
- 7 registros inválidos rejeitados.
- 233 registros válidos após a limpeza.
- 183 pedidos entregues, dos quais 58 atrasados e 125 no prazo.
- Taxa de atraso entre pedidos entregues: 58 / 183 = 31,7% (arredondada).
- 40 pedidos em aberto com prazo vencido; 10 pedidos em aberto ainda dentro do prazo.

## Raciocínio e decisão apoiada
1. Separar pedidos entregues de pedidos ainda abertos. Uma entrega atrasada é um resultado já ocorrido; uma pendência vencida é uma necessidade de ação.
2. Gerar uma fila de acompanhamento dos pedidos em aberto vencidos, priorizada por dias após o prazo.
3. Investigar os atrasos já ocorridos por período e região, sem afirmar causalidade apenas com uma segmentação.
4. Corrigir padrões inconsistentes na origem antes de usar os indicadores como base de decisões.

## Power BI
Importe output/orders_clean.csv após executar o pipeline. A pasta powerbi contém medidas sugeridas e um roteiro para estruturar o relatório.

## Limitações
- A base é sintética e não representa uma operação real.
- A taxa de atraso é descritiva; não identifica causa.
- O modelo não inclui feriados, dias úteis ou promessas alteradas.
- A data de referência é fixa, por decisão didática.
- A demonstração web usa uma pequena amostra no navegador. Os resultados acima vêm do dataset determinístico gerado pelo script.

## Estrutura
- generate_orders.py: cria linhas sintéticas e problemas intencionais.
- clean_orders.py: normaliza, valida e produz o dataset tratado e relatório de qualidade.
- queries.sql: consultas analíticas para SQLite.
- dicionario.md: campos e regras de qualidade.
- powerbi/: medidas e proposta de visualização.
