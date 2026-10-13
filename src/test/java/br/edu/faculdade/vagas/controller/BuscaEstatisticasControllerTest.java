package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Empresa;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.EmpresaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BuscaEstatisticasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void prepararBanco() {
        vagaRepository.deleteAll();
        empresaRepository.deleteAll();

        Empresa empresaA = empresaRepository.save(empresa("empresa-a", "Empresa A"));
        Empresa empresaB = empresaRepository.save(empresa("empresa-b", "Empresa B"));

        vagaRepository.save(vaga("Analista de Dados", Area.DADOS, Senioridade.PLENO,
                "Híbrido - Recife", false, empresaB));
        vagaRepository.save(vaga("Desenvolvedor Java", Area.BACK_END, Senioridade.JUNIOR,
                "Remoto", true, empresaA));
        vagaRepository.save(vaga("Engenheiro DevOps", Area.DEVOPS, Senioridade.SENIOR,
                "Remoto", false, empresaA));
    }

    @Test
    void devePaginarEOrdenarAsVagas() throws Exception {
        mockMvc.perform(get("/vagas")
                        .param("pagina", "0")
                        .param("tamanho", "2")
                        .param("ordenarPor", "titulo")
                        .param("direcao", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo.length()").value(2))
                .andExpect(jsonPath("$.conteudo[0].titulo").value("Analista de Dados"))
                .andExpect(jsonPath("$.conteudo[1].titulo").value("Desenvolvedor Java"))
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanho").value(2))
                .andExpect(jsonPath("$.totalDeElementos").value(3))
                .andExpect(jsonPath("$.totalDePaginas").value(2))
                .andExpect(jsonPath("$.primeira").value(true))
                .andExpect(jsonPath("$.ultima").value(false));
    }

    @Test
    void deveFiltrarPorEmpresaLocalAreaESenioridade() throws Exception {
        mockMvc.perform(get("/vagas/busca")
                        .param("empresaSlug", "empresa-a")
                        .param("local", "remoto")
                        .param("area", "BACK_END")
                        .param("senioridade", "JUNIOR")
                        .param("aceitaIniciante", "true")
                        .param("ordenarPor", "titulo")
                        .param("direcao", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Desenvolvedor Java"));
    }

    @Test
    void deveCalcularEstatisticasComConsultasAgregadas() throws Exception {
        mockMvc.perform(get("/estatisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDeEmpresas").value(2))
                .andExpect(jsonPath("$.totalDeVagas").value(3))
                .andExpect(jsonPath("$.vagasParaIniciantes").value(1))
                .andExpect(jsonPath("$.porArea.BACK_END").value(1))
                .andExpect(jsonPath("$.porArea.DADOS").value(1))
                .andExpect(jsonPath("$.porEmpresa.empresa-a").value(2))
                .andExpect(jsonPath("$.porEmpresa.empresa-b").value(1));
    }

    @Test
    void deveResponderQuatrocentosParaParametrosInvalidos() throws Exception {
        mockMvc.perform(get("/vagas")
                        .param("tamanho", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Parâmetro inválido"));

        mockMvc.perform(get("/vagas/busca")
                        .param("dataInicio", "2026-10-14")
                        .param("dataFim", "2026-10-13"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail")
                        .value("A data inicial não pode ser posterior à data final."));

        mockMvc.perform(get("/vagas/busca")
                        .param("area", "AREA_INEXISTENTE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Parâmetro inválido"));
    }

    private Empresa empresa(String slug, String nome) {
        return new Empresa(nome, slug, "https://" + slug + ".com.br", "Empresa de tecnologia.");
    }

    private Vaga vaga(String titulo, Area area, Senioridade senioridade,
                      String local, boolean aceitaIniciante, Empresa empresa) {
        return new Vaga(
                titulo,
                "Descrição da vaga " + titulo + ".",
                area,
                senioridade,
                local,
                aceitaIniciante,
                empresa
        );
    }
}
