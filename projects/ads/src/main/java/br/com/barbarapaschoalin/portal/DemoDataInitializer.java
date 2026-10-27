package br.com.barbarapaschoalin.portal;

import br.com.barbarapaschoalin.portal.api.CriarSolicitacaoRequest;
import br.com.barbarapaschoalin.portal.domain.Prioridade;
import br.com.barbarapaschoalin.portal.domain.Solicitacao;
import br.com.barbarapaschoalin.portal.domain.StatusSolicitacao;
import br.com.barbarapaschoalin.portal.repository.SolicitacaoRepository;
import br.com.barbarapaschoalin.portal.service.SolicitacaoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDataInitializer {

    @Bean
    CommandLineRunner seedDemoData(SolicitacaoRepository repository, SolicitacaoService service) {
        return args -> {
            if (repository.count() > 0) return;
            service.criar(new CriarSolicitacaoRequest(
                "Acesso ao portal financeiro", "Usuária fictícia não consegue acessar a área de relatórios.",
                "Ana Demo", "Financeiro", Prioridade.ALTA
            ));
            service.criar(new CriarSolicitacaoRequest(
                "Erro ao exportar relatório", "A exportação de CSV falha no exemplo do ambiente de treinamento.",
                "Caio Exemplo", "Comercial", Prioridade.MEDIA
            ));
            service.criar(new CriarSolicitacaoRequest(
                "Atualizar cadastro de departamento", "Solicitação de exemplo para validar o fluxo de atendimento.",
                "Lia Teste", "Operações", Prioridade.BAIXA
            ));
            var first = repository.findAllByOrderByCriadaEmDesc().get(0);
            service.alterarStatus(first.getId(), StatusSolicitacao.EM_ATENDIMENTO, "Responsável fictício iniciou a análise.");
        };
    }
}
