# Portal de Solicitações Internas — Java + Spring Boot

Projeto demonstrativo para portfólio de ADS. Todos os nomes, usuários e chamados são sintéticos.

## Stack
- Java 21
- Spring Boot 3.5.x
- Spring Web e Spring Data JPA
- Bean Validation
- H2 para execução local
- SQL do esquema PostgreSQL documentado em sql/schema-postgresql.sql
- JUnit e Spring Boot Test

## Requisitos
- JDK 21+
- Maven 3.9+

## Executar
Na pasta projects/ads:

1. Executar testes: mvn test
2. Iniciar a aplicação: mvn spring-boot:run
3. Abrir a interface: http://localhost:8080
4. Abrir o console H2: http://localhost:8080/h2-console (configuração em application.properties)

A API fica em /api/solicitacoes. As chamadas de exemplo estão em docs/requisicoes.http.

## Endpoints
- GET /api/solicitacoes — lista; filtro opcional por status
- GET /api/solicitacoes/{id} — consulta uma solicitação
- POST /api/solicitacoes — cria uma solicitação
- PATCH /api/solicitacoes/{id}/status — tenta mudar o status segundo as regras
- GET /api/solicitacoes/{id}/historico — histórico de mudanças de status

## Regras demonstradas
- Novas solicitações começam como ABERTA.
- ABERTA pode mudar para EM_ATENDIMENTO ou CANCELADA.
- EM_ATENDIMENTO pode mudar para RESOLVIDA, ABERTA ou CANCELADA.
- RESOLVIDA e CANCELADA são estados finais neste protótipo.
- Cada transição aceita gera um registro de histórico.
- Campos obrigatórios são validados; erros são devolvidos como HTTP 400 ou 404 quando aplicável.

## Limites importantes
- H2 é usado para demonstração local; o conteúdo é recriado ao reiniciar.
- A aplicação não implementa autenticação/autorização de produção, envio de e-mail ou integração com ERP.
- Este é um projeto de aprendizagem, não um produto pronto para uso empresarial.
- O GitHub Pages hospeda o site e a documentação estática, não o backend Java. Para executar a API, siga os passos locais acima.

## Como apresentar em entrevista
Explique o problema, a divisão controller/service/repository, o motivo de usar um banco relacional, as regras de transição de status, um caso de erro e como os testes verificam o comportamento. Não afirme ter implantado em produção.
