package br.com.barbarapaschoalin.portal.api;

import br.com.barbarapaschoalin.portal.domain.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(@NotNull StatusSolicitacao status, String observacao) { }
