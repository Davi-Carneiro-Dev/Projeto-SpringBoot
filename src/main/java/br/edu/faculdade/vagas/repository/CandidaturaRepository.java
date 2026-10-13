package br.edu.faculdade.vagas.repository;

import br.edu.faculdade.vagas.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {

    boolean existsByPessoaIdAndVagaId(Long pessoaId, Long vagaId);

    boolean existsByPessoaId(Long pessoaId);

    @Query("""
            select c from Candidatura c
            join fetch c.pessoa
            join fetch c.vaga
            where (:pessoaId is null or c.pessoa.id = :pessoaId)
              and (:vagaId is null or c.vaga.id = :vagaId)
            order by c.enviadaEm desc
            """)
    List<Candidatura> buscar(
            @Param("pessoaId") Long pessoaId,
            @Param("vagaId") Long vagaId
    );
}
