package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.VagaEntrada;
import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.exception.RecursoNaoEncontradoException;
import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private VagaRepository repository;

    @Test
    void deveCriarUmaVagaComOsDadosRecebidos() {
        VagaService service = new VagaService(repository);
        VagaEntrada entrada = entradaValida();
        when(repository.save(org.mockito.ArgumentMatchers.any(Vaga.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        VagaResposta resposta = service.criar(entrada);

        ArgumentCaptor<Vaga> captor = ArgumentCaptor.forClass(Vaga.class);
        verify(repository).save(captor.capture());
        Vaga salva = captor.getValue();

        assertEquals("Pessoa Desenvolvedora Java", salva.getTitulo());
        assertEquals(Area.BACK_END, salva.getArea());
        assertEquals(Senioridade.JUNIOR, resposta.senioridade());
        assertEquals("empresa-exemplo", resposta.empresaSlug());
    }

    @Test
    void deveInformarQuandoAVagaNaoExiste() {
        VagaService service = new VagaService(repository);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException erro = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(99L)
        );

        assertEquals("Vaga 99 não encontrada.", erro.getMessage());
    }

    private VagaEntrada entradaValida() {
        return new VagaEntrada(
                "Pessoa Desenvolvedora Java",
                "Desenvolvimento e manutenção de APIs REST.",
                Area.BACK_END,
                Senioridade.JUNIOR,
                "Remoto",
                true,
                "empresa-exemplo"
        );
    }
}
