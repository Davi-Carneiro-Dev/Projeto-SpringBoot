package br.edu.faculdade.vagas.config;

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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(
        name = "app.carga-inicial.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class CargaInicial implements ApplicationRunner {

    private final EmpresaRepository empresaRepository;
    private final VagaRepository vagaRepository;
    private final PessoaRepository pessoaRepository;
    private final CandidaturaRepository candidaturaRepository;

    public CargaInicial(EmpresaRepository empresaRepository,
                        VagaRepository vagaRepository,
                        PessoaRepository pessoaRepository,
                        CandidaturaRepository candidaturaRepository) {
        this.empresaRepository = empresaRepository;
        this.vagaRepository = vagaRepository;
        this.pessoaRepository = pessoaRepository;
        this.candidaturaRepository = candidaturaRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (bancoPossuiDados()) {
            return;
        }

        Empresa lequeTech = empresaRepository.save(new Empresa(
                "Leque Tech",
                "leque-tech",
                "https://leque-tech.example.com",
                "Empresa de desenvolvimento de produtos digitais."
        ));
        Empresa dadosBrasil = empresaRepository.save(new Empresa(
                "Dados Brasil",
                "dados-brasil",
                "https://dados-brasil.example.com",
                "Empresa especializada em dados e inteligência de negócios."
        ));

        Vaga javaJunior = vagaRepository.save(new Vaga(
                "Pessoa Desenvolvedora Java Júnior",
                "Desenvolvimento e manutenção de APIs REST com Java e Spring Boot.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                lequeTech
        ));
        vagaRepository.save(new Vaga(
                "Pessoa Desenvolvedora Front-end Pleno",
                "Construção de interfaces web acessíveis e responsivas.",
                Area.FRONT_END,
                Senioridade.PLENO,
                "São Paulo - Híbrido",
                false,
                lequeTech
        ));
        Vaga estagioDados = vagaRepository.save(new Vaga(
                "Estágio em Dados",
                "Apoio na análise de dados e criação de indicadores.",
                Area.DADOS,
                Senioridade.ESTAGIO,
                "Remoto",
                true,
                dadosBrasil
        ));

        Pessoa maria = pessoaRepository.save(new Pessoa(
                "Maria da Silva",
                "maria.silva@example.com",
                Perfil.CANDIDATO
        ));
        Pessoa joao = pessoaRepository.save(new Pessoa(
                "João Souza",
                "joao.souza@example.com",
                Perfil.CANDIDATO
        ));

        candidaturaRepository.save(new Candidatura(maria, javaJunior));
        candidaturaRepository.save(new Candidatura(joao, estagioDados));
    }

    private boolean bancoPossuiDados() {
        return empresaRepository.count() > 0
                || vagaRepository.count() > 0
                || pessoaRepository.count() > 0
                || candidaturaRepository.count() > 0;
    }
}
