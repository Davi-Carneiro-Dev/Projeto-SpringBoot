package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VagaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VagaRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void deveCriarEConsultarUmaVaga() throws Exception {
        String json = """
                {
                  "titulo": "Pessoa Desenvolvedora Java",
                  "descricao": "Desenvolvimento e manutenção de APIs REST.",
                  "area": "BACK_END",
                  "senioridade": "JUNIOR",
                  "local": "Remoto",
                  "aceitaIniciante": true,
                  "empresaSlug": "empresa-exemplo"
                }
                """;

        String localizacao = mockMvc.perform(post("/vagas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.titulo").value("Pessoa Desenvolvedora Java"))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.area").value("BACK_END"))
                .andExpect(jsonPath("$.empresaSlug").value("empresa-exemplo"));
    }

    @Test
    void deveResponderQuatrocentosQuandoOsDadosSaoInvalidos() throws Exception {
        mockMvc.perform(post("/vagas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Dados inválidos"))
                .andExpect(jsonPath("$.campos.titulo").exists())
                .andExpect(jsonPath("$.campos.area").exists());
    }

    @Test
    void deveResponderQuatrocentosParaEnumInexistente() throws Exception {
        String json = """
                {
                  "titulo": "Vaga inválida",
                  "descricao": "Uma descrição válida.",
                  "area": "AREA_INEXISTENTE",
                  "senioridade": "JUNIOR",
                  "local": "Remoto",
                  "aceitaIniciante": true,
                  "empresaSlug": "empresa-exemplo"
                }
                """;

        mockMvc.perform(post("/vagas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Corpo inválido"));
    }

    @Test
    void deveResponderQuatrocentosEQuatroQuandoAVagaNaoExiste() throws Exception {
        mockMvc.perform(get("/vagas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"));
    }

    @Test
    void deveAtualizarEExcluirUmaVaga() throws Exception {
        Vaga vaga = repository.save(new Vaga(
                "Título original",
                "Descrição original da vaga.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                "empresa-exemplo"
        ));

        String atualizacao = """
                {
                  "titulo": "Título atualizado",
                  "descricao": "Descrição atualizada da vaga.",
                  "area": "DADOS",
                  "senioridade": "PLENO",
                  "local": "Híbrido",
                  "aceitaIniciante": false,
                  "empresaSlug": "empresa-exemplo"
                }
                """;

        mockMvc.perform(put("/vagas/{id}", vaga.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título atualizado"))
                .andExpect(jsonPath("$.area").value("DADOS"));

        mockMvc.perform(delete("/vagas/{id}", vaga.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/vagas/{id}", vaga.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveFiltrarVagasECalcularEstatisticas() throws Exception {
        repository.save(new Vaga(
                "Desenvolvimento Java",
                "Desenvolvimento de APIs.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                "empresa-a"
        ));
        repository.save(new Vaga(
                "Análise de dados",
                "Construção de relatórios.",
                Area.DADOS,
                Senioridade.PLENO,
                "Híbrido",
                false,
                "empresa-b"
        ));

        mockMvc.perform(get("/vagas/busca")
                        .param("area", "BACK_END")
                        .param("senioridade", "JUNIOR")
                        .param("aceitaIniciante", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Desenvolvimento Java"));

        mockMvc.perform(get("/estatisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDeVagas").value(2))
                .andExpect(jsonPath("$.vagasParaIniciantes").value(1))
                .andExpect(jsonPath("$.porArea.BACK_END").value(1))
                .andExpect(jsonPath("$.porArea.DADOS").value(1));
    }
}
