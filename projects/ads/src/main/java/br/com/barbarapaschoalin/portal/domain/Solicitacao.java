package br.com.barbarapaschoalin.portal.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacoes")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String solicitante;

    @Column(nullable = false, length = 80)
    private String departamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusSolicitacao status;

    @Column(nullable = false)
    private LocalDateTime criadaEm;

    @Column(nullable = false)
    private LocalDateTime atualizadaEm;

    protected Solicitacao() { }

    public Solicitacao(String titulo, String descricao, String solicitante,
                       String departamento, Prioridade prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.solicitante = solicitante;
        this.departamento = departamento;
        this.prioridade = prioridade;
        this.status = StatusSolicitacao.ABERTA;
        this.criadaEm = LocalDateTime.now();
        this.atualizadaEm = this.criadaEm;
    }

    public void alterarStatus(StatusSolicitacao novoStatus) {
        this.status = novoStatus;
        this.atualizadaEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getSolicitante() { return solicitante; }
    public String getDepartamento() { return departamento; }
    public Prioridade getPrioridade() { return prioridade; }
    public StatusSolicitacao getStatus() { return status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public LocalDateTime getAtualizadaEm() { return atualizadaEm; }
}
