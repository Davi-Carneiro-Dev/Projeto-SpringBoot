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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @BeforeEach
    void limparBanco() {
        vagaRepository.deleteAll();
        empresaRepository.deleteAll();
    }

    @Test
    void deveCriarListarEConsultarEmpresaPorSlug() throws Exception {
        String localizacao = mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empresaJson("Empresa Exemplo", "empresa-exemplo")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/empresas/empresa-exemplo"))
                .andExpect(jsonPath("$.nome").value("Empresa Exemplo"))
                .andExpect(jsonPath("$.slug").value("empresa-exemplo"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get("/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.site").value("https://empresa-exemplo.com.br"));
    }

    @Test
    void deveRejeitarSlugDuplicado() throws Exception {
        empresaRepository.save(novaEmpresa("empresa-exemplo", "Primeira Empresa"));

        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empresaJson("Outra Empresa", "empresa-exemplo")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflito de dados"));
    }

    @Test
    void deveListarAsVagasDaEmpresa() throws Exception {
        Empresa empresa = empresaRepository.save(novaEmpresa("empresa-exemplo", "Empresa Exemplo"));
        vagaRepository.save(new Vaga(
                "Pessoa Desenvolvedora Java",
                "Desenvolvimento de APIs.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                empresa
        ));

        mockMvc.perform(get("/empresas/empresa-exemplo/vagas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Pessoa Desenvolvedora Java"))
                .andExpect(jsonPath("$[0].empresaSlug").value("empresa-exemplo"));
    }

    @Test
    void deveAtualizarEExcluirEmpresaSemVagas() throws Exception {
        empresaRepository.save(novaEmpresa("empresa-exemplo", "Empresa Exemplo"));

        mockMvc.perform(put("/empresas/empresa-exemplo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empresaJson("Empresa Atualizada", "empresa-atualizada")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Empresa Atualizada"))
                .andExpect(jsonPath("$.slug").value("empresa-atualizada"));

        mockMvc.perform(delete("/empresas/empresa-atualizada"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/empresas/empresa-atualizada"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveImpedirExclusaoDeEmpresaComVagas() throws Exception {
        Empresa empresa = empresaRepository.save(novaEmpresa("empresa-exemplo", "Empresa Exemplo"));
        vagaRepository.save(new Vaga(
                "Pessoa Desenvolvedora Java",
                "Desenvolvimento de APIs.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                empresa
        ));

        mockMvc.perform(delete("/empresas/empresa-exemplo"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("A empresa possui vagas e não pode ser excluída."));
    }

    @Test
    void deveValidarOsDadosDaEmpresa() throws Exception {
        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.slug").exists())
                .andExpect(jsonPath("$.campos.descricao").exists());
    }

    private Empresa novaEmpresa(String slug, String nome) {
        return new Empresa(
                nome,
                slug,
                "https://" + slug + ".com.br",
                "Empresa de tecnologia."
        );
    }

    private String empresaJson(String nome, String slug) {
        return """
                {
                  "nome": "%s",
                  "slug": "%s",
                  "site": "https://%s.com.br",
                  "descricao": "Empresa especializada em tecnologia."
                }
                """.formatted(nome, slug, slug);
    }
}
