package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.PacienteRequestDTO;
import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.entities.Paciente;
import br.com.exemplo.aula.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {
    @Mock
    PacienteRepository pacienteRepository;
    @InjectMocks
    PacienteService pacienteService;
    Paciente paciente;
    @BeforeEach
    public void setup() {
        paciente = new Paciente();
    }
    @Test
    void listar() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(paciente);
        when(pacienteRepository.findAll()).thenReturn(pacientes);
        List<PacienteResponseDTO> resultado = pacienteService.listarPacientes();
        assertNotNull(resultado);
        assertEquals(pacientes.get(0).getId(), resultado.get(0).getId());
        verify(pacienteRepository).findAll();
    }
    @Test
    void buscar() {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.ofNullable(paciente));
        PacienteResponseDTO resultado = pacienteService.buscarPaciente(1L);
        assertNotNull(resultado);
        assertEquals(paciente.getId(), resultado.getId());
        verify(pacienteRepository).findById(anyLong());
    }
    @Test
    void salvar() {
        PacienteRequestDTO pacienteRequestDTO = new PacienteRequestDTO();
        when(pacienteRepository.save(any())).thenReturn(paciente);
        PacienteResponseDTO resultado = pacienteService.salvarPaciente(pacienteRequestDTO);
        assertNotNull(resultado);
        assertEquals(pacienteRequestDTO.getNome(), resultado.getNome());
        verify(pacienteRepository).save(any());
    }
    @Test
    void atualizar() {
        PacienteRequestDTO pacienteRequestDTO = new PacienteRequestDTO();
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.ofNullable(paciente));
        when(pacienteRepository.save(any())).thenReturn(paciente);
        PacienteResponseDTO resultado = pacienteService.atualizarPaciente(1L, pacienteRequestDTO);
        assertNotNull(resultado);
        assertEquals(paciente.getId(), resultado.getId());
        assertEquals(pacienteRequestDTO.getNome(), resultado.getNome());
        verify(pacienteRepository).findById(anyLong());
        verify(pacienteRepository).save(any());
    }
    @Test
    void remover() {
        doNothing().when(pacienteRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> pacienteService.removerPaciente(1L));
        verify(pacienteRepository).deleteById(anyLong());
    }
    @Test
    void removerThrow() {
        doThrow(RuntimeException.class).when(pacienteRepository).deleteById(anyLong());
        assertThrows(RuntimeException.class, () -> pacienteService.removerPaciente(0L));
        verify(pacienteRepository).deleteById(anyLong());
    }
    @Test
    void buscarThrow() {
        doThrow(RuntimeException.class).when(pacienteRepository).findById(anyLong());
        assertThrows(RuntimeException.class, () -> pacienteService.buscarPaciente(0L));
        verify(pacienteRepository).findById(anyLong());
    }
    @Test
    void buscarNull() {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(pacienteService.buscarPaciente(0L));
        verify(pacienteRepository).findById(anyLong());
    }
    @Test
    void atualizarAssert() {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            pacienteService.atualizarPaciente(0L, new PacienteRequestDTO());
        } catch (AssertionError ignored) {}
        assertThrows(AssertionError.class, () -> pacienteService.atualizarPaciente(0L, new PacienteRequestDTO()));
        verify(pacienteRepository, times(2)).findById(anyLong());
    }
}
