package br.com.barbarapaschoalin.portal.service;

import br.com.barbarapaschoalin.portal.api.CriarSolicitacaoRequest;
import br.com.barbarapaschoalin.portal.domain.HistoricoStatus;
import br.com.barbarapaschoalin.portal.domain.Solicitacao;
import br.com.barbarapaschoalin.portal.domain.StatusSolicitacao;
import br.com.barbarapaschoalin.portal.repository.HistoricoStatusRepository;
import br.com.barbarapaschoalin.portal.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final HistoricoStatusRepository historicoRepository;

    private static final Map<StatusSolicitacao, EnumSet<StatusSolicitacao>> TRANSICOES = Map.of(
        StatusSolicitacao.ABERTA, EnumSet.of(StatusSolicitacao.EM_ATENDIMENTO, StatusSolicitacao.CANCELADA),
        StatusSolicitacao.EM_ATENDIMENTO, EnumSet.of(StatusSolicitacao.RESOLVIDA, StatusSolicitacao.ABERTA, StatusSolicitacao.CANCELADA),
        StatusSolicitacao.RESOLVIDA, EnumSet.noneOf(StatusSolicitacao.class),
        StatusSolicitacao.CANCELADA, EnumSet.noneOf(StatusSolicitacao.class)
    );

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository,
                              HistoricoStatusRepository historicoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.historicoRepository = historicoRepository;
    }

    @Transactional(readOnly = true)
    public List<Solicitacao> listar(StatusSolicitacao status) {
        return status == null
            ? solicitacaoRepository.findAllByOrderByCriadaEmDesc()
            : solicitacaoRepository.findByStatusOrderByCriadaEmDesc(status);
    }

    @Transactional(readOnly = true)
    public Solicitacao buscar(Long id) {
        return solicitacaoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Solicitação não encontrada: " + id));
    }

    public Solicitacao criar(CriarSolicitacaoRequest request) {
        Solicitacao criada = solicitacaoRepository.save(new Solicitacao(
            request.titulo().trim(),
            request.descricao().trim(),
            request.solicitante().trim(),
            request.departamento().trim(),
            request.prioridade()
        ));
        historicoRepository.save(new HistoricoStatus(
            criada.getId(), null, StatusSolicitacao.ABERTA, "Solicitação criada"
        ));
        return criada;
    }

    public Solicitacao alterarStatus(Long id, StatusSolicitacao novoStatus, String observacao) {
        Solicitacao item = buscar(id);
        StatusSolicitacao atual = item.getStatus();

        if (!TRANSICOES.getOrDefault(atual, EnumSet.noneOf(StatusSolicitacao.class)).contains(novoStatus)) {
            throw new RegraNegocioException(
                "Transição não permitida: " + atual + " -> " + novoStatus
            );
        }

        item.alterarStatus(novoStatus);
        Solicitacao salva = solicitacaoRepository.save(item);
        historicoRepository.save(new HistoricoStatus(id, atual, novoStatus, observacao));
        return salva;
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatus> historico(Long id) {
        buscar(id); // Confirma que a solicitação existe.
        return historicoRepository.findBySolicitacaoIdOrderByAlteradoEmDesc(id);
    }
}
