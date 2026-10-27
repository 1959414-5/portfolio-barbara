# Portfólio — Bárbara Cabral Paschoalin

Portfólio técnico com duas trilhas independentes: **Dados & Business Intelligence** e **Desenvolvimento de Sistemas (ADS)**. O objetivo é mostrar contexto, raciocínio, decisões, código e demonstrações, não apenas imagens de telas.

## Site
Após a publicação pelo GitHub Pages, o endereço esperado é:
https://1959414-5.github.io/portfolio-barbara/

## Estrutura
- index.html — página inicial e seleção da trilha.
- dados.html — estudo de caso de análise de atrasos com dados sintéticos.
- ads.html — projeto de solicitação interna em Java + Spring Boot.
- styles.css / app.js — identidade visual e comportamento comum.
- projects/dados/ — gerador sintético, pipeline de limpeza, SQL e materiais para Power BI.
- projects/ads/ — aplicação Java, API REST, persistência H2, regras de negócio, testes e documentação.
- docs/guia-videos.md — roteiro para gravar as demonstrações.

## Dados e transparência
Todos os códigos de clientes, vendedores, pedidos, usuários e solicitações dos estudos demonstrativos são fictícios. Nenhum dado de uma empresa real é necessário para executar os exemplos.

## Rodar o projeto de Dados
Na pasta projects/dados:
1. python -m pip install -r requirements.txt
2. python generate_orders.py
3. python clean_orders.py

Isso cria 250 linhas brutas sintéticas, remove 10 duplicidades e rejeita 7 registros com valor inválido, produzindo 233 linhas tratadas e um relatório de qualidade.

## Rodar o projeto ADS
Requer JDK 21+ e Maven 3.9+. Na pasta projects/ads:
1. mvn test
2. mvn spring-boot:run
3. Abrir http://localhost:8080

O backend não executa no GitHub Pages; a hospedagem Pages serve as páginas estáticas e o código/documentação.

## Publicação
O workflow em .github/workflows/pages.yml tenta habilitar e publicar o site no GitHub Pages a partir do conteúdo da raiz do repositório. A primeira execução pode falhar se a conta/repositório exigir habilitação manual de Pages ou permissões adicionais. Nesse caso, abra Settings → Pages e permita publicação por GitHub Actions.

## Status de validação
O pipeline Python foi desenhado para ser reproduzível e gerar as quantidades listadas acima. O projeto Java deve ser compilado e testado localmente antes de ser descrito em entrevista como executado; este ambiente não confirma uma compilação Maven.
