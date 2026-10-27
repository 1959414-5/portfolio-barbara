package br.com.barbarapaschoalin.portal.api;

import br.com.barbarapaschoalin.portal.domain.HistoricoStatus;
import br.com.barbarapaschoalin.portal.domain.StatusSolicitacao;
import java.time.LocalDateTime;

public record HistoricoResponse(
    Long id,
    Long solicitacaoId,
    StatusSolicitacao statusAnterior,
    StatusSolicitacao statusNovo,
    LocalDateTime alteradoEm,
    String observacao
) {
    public static HistoricoResponse from(HistoricoStatus item) {
        return new HistoricoResponse(item.getId(), item.getSolicitacaoId(),
            item.getStatusAnterior(), item.getStatusNovo(),
            item.getAlteradoEm(), item.getObservacao());
    }
}
