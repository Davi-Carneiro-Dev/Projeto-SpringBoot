package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Candidatura;
import br.edu.faculdade.vagas.model.Empresa;
import br.edu.faculdade.vagas.model.Perfil;
import br.edu.faculdade.vagas.model.Pessoa;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.CandidaturaRepository;
import br.edu.faculdade.vagas.repository.EmpresaRepository;
import br.edu.faculdade.vagas.repository.PessoaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PessoaCandidaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @BeforeEach
    @AfterEach
    void limparBanco() {
        candidaturaRepository.deleteAll();
        pessoaRepository.deleteAll();
        vagaRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    @Test
    void deveCriarPessoaNormalizarEmailEBloquearDuplicidade() throws Exception {
        String pessoa = """
                {
                  "nome": "Maria da Silva",
                  "email": "  MARIA@EXEMPLO.COM  ",
                  "perfil": "CANDIDATO"
                }
                """;

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoa))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Maria da Silva"))
                .andExpect(jsonPath("$.email").value("maria@exemplo.com"));

        mockMvc.perform(post("/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoa))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflito de dados"));
    }

    @Test
    void deveCriarFiltrarAtualizarEExcluirCandidatura() throws Exception {
        Pessoa pessoa = pessoaRepository.save(new Pessoa(
                "Maria da Silva",
                "maria@exemplo.com",
                Perfil.CANDIDATO
        ));
        Vaga vaga = vagaRepository.save(novaVaga());

        String candidatura = """
                {
                  "pessoaId": %d,
                  "vagaId": %d
                }
                """.formatted(pessoa.getId(), vaga.getId());

        String localizacao = mockMvc.perform(post("/candidaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(candidatura))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.situacao").value("ENVIADA"))
                .andExpect(jsonPath("$.pessoaNome").value("Maria da Silva"))
                .andExpect(jsonPath("$.vagaTitulo").value("Pessoa Desenvolvedora Java"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get("/candidaturas")
                        .param("pessoaId", pessoa.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(patch(localizacao + "/situacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"situacao\":\"EM_ANALISE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situacao").value("EM_ANALISE"));

        mockMvc.perform(delete(localizacao))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(localizacao))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveBloquearCandidaturaDuplicada() throws Exception {
        Pessoa pessoa = pessoaRepository.save(new Pessoa(
                "Maria da Silva",
                "maria@exemplo.com",
                Perfil.CANDIDATO
        ));
        Vaga vaga = vagaRepository.save(novaVaga());
        candidaturaRepository.save(new Candidatura(pessoa, vaga));

        String candidatura = """
                {
                  "pessoaId": %d,
                  "vagaId": %d
                }
                """.formatted(pessoa.getId(), vaga.getId());

        mockMvc.perform(post("/candidaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(candidatura))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("Esta pessoa já se candidatou a esta vaga."));
    }

    @Test
    void deveBloquearExclusaoDePessoaComCandidatura() throws Exception {
        Pessoa pessoa = pessoaRepository.save(new Pessoa(
                "Maria da Silva",
                "maria@exemplo.com",
                Perfil.CANDIDATO
        ));
        Vaga vaga = vagaRepository.save(novaVaga());
        candidaturaRepository.save(new Candidatura(pessoa, vaga));

        mockMvc.perform(delete("/pessoas/{id}", pessoa.getId()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflito de dados"));
    }

    @Test
    void deveResponderQuatrocentosEQuatroParaPessoaInexistente() throws Exception {
        mockMvc.perform(post("/candidaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pessoaId\":999,\"vagaId\":999}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Pessoa 999 não encontrada."));
    }

    private Vaga novaVaga() {
        Empresa empresa = empresaRepository.save(new Empresa(
                "Empresa Exemplo",
                "empresa-exemplo",
                "https://empresa-exemplo.com.br",
                "Empresa especializada em tecnologia."
        ));
        return new Vaga(
                "Pessoa Desenvolvedora Java",
                "Desenvolvimento de APIs REST.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                empresa
        );
    }
}
