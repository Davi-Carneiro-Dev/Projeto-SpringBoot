package br.edu.faculdade.vagas.repository;

import br.edu.faculdade.vagas.dto.ContagemPorArea;
import br.edu.faculdade.vagas.dto.ContagemPorEmpresa;
import br.edu.faculdade.vagas.dto.ContagemPorSenioridade;
import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query("""
            select v from Vaga v
            join fetch v.empresa e
            where (:area is null or v.area = :area)
              and (:senioridade is null or v.senioridade = :senioridade)
              and (:aceitaIniciante is null or v.aceitaIniciante = :aceitaIniciante)
              and (:local is null or lower(v.local) like lower(concat('%', :local, '%')))
              and (:empresaSlug is null or e.slug = :empresaSlug)
              and (:publicadaDepoisDe is null or v.publicadaEm >= :publicadaDepoisDe)
              and (:publicadaAntesDe is null or v.publicadaEm < :publicadaAntesDe)
            """)
    List<Vaga> buscar(
            @Param("area") Area area,
            @Param("senioridade") Senioridade senioridade,
            @Param("aceitaIniciante") Boolean aceitaIniciante,
            @Param("local") String local,
            @Param("empresaSlug") String empresaSlug,
            @Param("publicadaDepoisDe") LocalDateTime publicadaDepoisDe,
            @Param("publicadaAntesDe") LocalDateTime publicadaAntesDe,
            Sort sort
    );

    List<Vaga> findByEmpresa_SlugOrderByPublicadaEmDesc(String slug);

    boolean existsByEmpresa_Slug(String slug);

    @Query("select count(v) from Vaga v where v.aceitaIniciante = true")
    long contarVagasParaIniciantes();

    @Query("""
            select v.area as area, count(v) as quantidade
            from Vaga v
            group by v.area
            order by v.area
            """)
    List<ContagemPorArea> contarPorArea();

    @Query("""
            select v.senioridade as senioridade, count(v) as quantidade
            from Vaga v
            group by v.senioridade
            order by v.senioridade
            """)
    List<ContagemPorSenioridade> contarPorSenioridade();

    @Query("""
            select v.empresa.slug as empresaSlug, count(v) as quantidade
            from Vaga v
            group by v.empresa.slug
            order by v.empresa.slug
            """)
    List<ContagemPorEmpresa> contarPorEmpresa();
}
