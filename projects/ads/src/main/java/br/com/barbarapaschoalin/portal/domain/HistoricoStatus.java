package br.com.barbarapaschoalin.portal.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status")
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long solicitacaoId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private StatusSolicitacao statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusSolicitacao statusNovo;

    @Column(nullable = false)
    private LocalDateTime alteradoEm;

    @Column(length = 500)
    private String observacao;

    protected HistoricoStatus() { }

    public HistoricoStatus(Long solicitacaoId, StatusSolicitacao statusAnterior,
                           StatusSolicitacao statusNovo, String observacao) {
        this.solicitacaoId = solicitacaoId;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.observacao = observacao;
        this.alteradoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getSolicitacaoId() { return solicitacaoId; }
    public StatusSolicitacao getStatusAnterior() { return statusAnterior; }
    public StatusSolicitacao getStatusNovo() { return statusNovo; }
    public LocalDateTime getAlteradoEm() { return alteradoEm; }
    public String getObservacao() { return observacao; }
}
