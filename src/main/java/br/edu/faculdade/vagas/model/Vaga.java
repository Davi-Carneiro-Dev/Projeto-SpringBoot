package br.edu.faculdade.vagas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, updatable = false)
    private LocalDateTime publicadaEm;

    protected Vaga() {
    }

    public Vaga(String titulo, String descricao, Area area, Senioridade senioridade,
                String local, Boolean aceitaIniciante, Empresa empresa) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.area = area;
        this.senioridade = senioridade;
        this.local = local;
        this.aceitaIniciante = aceitaIniciante;
        this.empresa = empresa;
    }

    @PrePersist
    private void preencherDataDePublicacao() {
        if (publicadaEm == null) {
            publicadaEm = LocalDateTime.now();
        }
    }

    public void atualizar(String titulo, String descricao, Area area, Senioridade senioridade,
                          String local, Boolean aceitaIniciante, Empresa empresa) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.area = area;
        this.senioridade = senioridade;
        this.local = local;
        this.aceitaIniciante = aceitaIniciante;
        this.empresa = empresa;
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
        return empresa.getSlug();
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public LocalDateTime getPublicadaEm() {
        return publicadaEm;
    }
}
