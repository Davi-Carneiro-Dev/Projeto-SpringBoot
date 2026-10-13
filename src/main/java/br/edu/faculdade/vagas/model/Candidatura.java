package br.edu.faculdade.vagas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "candidaturas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_candidatura_pessoa_vaga",
                columnNames = {"pessoa_id", "vaga_id"}
        )
)
public class Candidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SituacaoCandidatura situacao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime enviadaEm;

    protected Candidatura() {
    }

    public Candidatura(Pessoa pessoa, Vaga vaga) {
        this.pessoa = pessoa;
        this.vaga = vaga;
        this.situacao = SituacaoCandidatura.ENVIADA;
    }

    @PrePersist
    private void preencherDataDeEnvio() {
        if (enviadaEm == null) {
            enviadaEm = LocalDateTime.now();
        }
    }

    public void atualizarSituacao(SituacaoCandidatura situacao) {
        this.situacao = situacao;
    }

    public Long getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public SituacaoCandidatura getSituacao() {
        return situacao;
    }

    public LocalDateTime getEnviadaEm() {
        return enviadaEm;
    }
}
