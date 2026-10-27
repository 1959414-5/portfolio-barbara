package br.com.barbarapaschoalin.portal.api;

import br.com.barbarapaschoalin.portal.domain.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarSolicitacaoRequest(
    @NotBlank @Size(max = 120) String titulo,
    @NotBlank @Size(max = 2000) String descricao,
    @NotBlank @Size(max = 100) String solicitante,
    @NotBlank @Size(max = 80) String departamento,
    @NotNull Prioridade prioridade
) { }
