package br.edu.faculdade.vagas.repository;

import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    @Query("""
            select v from Vaga v
            where (:area is null or v.area = :area)
              and (:senioridade is null or v.senioridade = :senioridade)
              and (:aceitaIniciante is null or v.aceitaIniciante = :aceitaIniciante)
            order by v.publicadaEm desc
            """)
    List<Vaga> buscar(
            @Param("area") Area area,
            @Param("senioridade") Senioridade senioridade,
            @Param("aceitaIniciante") Boolean aceitaIniciante
    );
}
