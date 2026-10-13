package br.edu.faculdade.vagas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "vagas")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Area area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Senioridade senioridade;

    @Column(nullable = false, length = 120)
    private String local;

    @Column(nullable = false)
    private Boolean aceitaIniciante;

    @Column(nullable = false, length = 100)
    private String empresaSlug;

    @Column(nullable = false, updatable = false)
    private LocalDateTime publicadaEm;

    protected Vaga() {
    }

    public Vaga(String titulo, String descricao, Area area, Senioridade senioridade,
                String local, Boolean aceitaIniciante, String empresaSlug) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.area = area;
        this.senioridade = senioridade;
        this.local = local;
        this.aceitaIniciante = aceitaIniciante;
        this.empresaSlug = empresaSlug;
    }

    @PrePersist
    private void preencherDataDePublicacao() {
        if (publicadaEm == null) {
            publicadaEm = LocalDateTime.now();
        }
    }

    public void atualizar(String titulo, String descricao, Area area, Senioridade senioridade,
                          String local, Boolean aceitaIniciante, String empresaSlug) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.area = area;
        this.senioridade = senioridade;
        this.local = local;
        this.aceitaIniciante = aceitaIniciante;
        this.empresaSlug = empresaSlug;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Area getArea() {
        return area;
    }

    public Senioridade getSenioridade() {
        return senioridade;
    }

    public String getLocal() {
        return local;
    }

    public Boolean getAceitaIniciante() {
        return aceitaIniciante;
    }

    public String getEmpresaSlug() {
        return empresaSlug;
    }

    public LocalDateTime getPublicadaEm() {
        return publicadaEm;
    }
}
