package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
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
public class NutricionistaServiceTest {
    @Mock
    NutricionistaRepository nutricionistaRepository;
    @InjectMocks
    NutricionistaService nutricionistaService;
    Nutricionista nutricionista;
    @BeforeEach
    public void setup() {
        nutricionista = new Nutricionista();
    }
    @Test
    void listar() {
        List<Nutricionista> nutricionistas = new ArrayList<>();
        nutricionistas.add(nutricionista);
        when(nutricionistaRepository.findAll()).thenReturn(nutricionistas);
        List<NutricionistaResponseDTO> resultado = nutricionistaService.listarNutricionistas();
        assertNotNull(resultado);
        assertEquals(nutricionistas.get(0).getId(), resultado.get(0).getId());
        verify(nutricionistaRepository).findAll();
    }
    @Test
    void buscar() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(nutricionista));
        NutricionistaResponseDTO resultado = nutricionistaService.buscarNutricionista(1L);
        assertNotNull(resultado);
        assertEquals(nutricionista.getId(), resultado.getId());
        verify(nutricionistaRepository).findById(anyLong());
    }
    @Test
    void salvar() {
        NutricionistaRequestDTO nutricionistaRequestDTO = new NutricionistaRequestDTO();
        when(nutricionistaRepository.save(any())).thenReturn(nutricionista);
        NutricionistaResponseDTO resultado = nutricionistaService.salvarNutricionista(nutricionistaRequestDTO);
        assertNotNull(resultado);
        assertEquals(nutricionistaRequestDTO.getNome(), resultado.getNome());
        verify(nutricionistaRepository).save(any());
    }
    @Test
    void atualizar() {
        NutricionistaRequestDTO nutricionistaRequestDTO = new NutricionistaRequestDTO();
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(nutricionista));
        when(nutricionistaRepository.save(any())).thenReturn(nutricionista);
        NutricionistaResponseDTO resultado = nutricionistaService.atualizarNutricionista(1L, nutricionistaRequestDTO);
        assertNotNull(resultado);
        assertEquals(nutricionista.getId(), resultado.getId());
        assertEquals(nutricionistaRequestDTO.getNome(), resultado.getNome());
        verify(nutricionistaRepository).findById(anyLong());
        verify(nutricionistaRepository).save(any());
    }
    @Test
    void remover() {
        doNothing().when(nutricionistaRepository).deleteById(anyLong());
        assertDoesNotThrow(() -> nutricionistaService.removerNutricionista(1L));
        verify(nutricionistaRepository).deleteById(anyLong());
    }
    @Test
    void removerThrow() {
        doThrow(RuntimeException.class).when(nutricionistaRepository).deleteById(anyLong());
        assertThrows(RuntimeException.class, () -> nutricionistaService.removerNutricionista(0L));
        verify(nutricionistaRepository).deleteById(anyLong());
    }
    @Test
    void buscarThrow() {
        doThrow(RuntimeException.class).when(nutricionistaRepository).findById(anyLong());
        assertThrows(RuntimeException.class, () -> nutricionistaService.buscarNutricionista(0L));
        verify(nutricionistaRepository).findById(anyLong());
    }
    @Test
    void buscarNull() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(nutricionistaService.buscarNutricionista(0L));
        verify(nutricionistaRepository).findById(anyLong());
    }
    @Test
    void atualizarAssert() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            nutricionistaService.atualizarNutricionista(0L, new NutricionistaRequestDTO());
        } catch (AssertionError ignored) {}
        assertThrows(AssertionError.class, () -> nutricionistaService.atualizarNutricionista(0L, new NutricionistaRequestDTO()));
        verify(nutricionistaRepository, times(2)).findById(anyLong());
    }
    @Test
    void salvarThrow() {
        when(nutricionistaRepository.findByNome(any())).thenReturn(Optional.of(nutricionista));
        assertThrows(RuntimeException.class, () -> nutricionistaService.salvarNutricionista(new NutricionistaRequestDTO()));
        verify(nutricionistaRepository).findByNome(any());
    }
    @Test
    void atualizarThrow() {
        nutricionista.setNome("Nome");
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.of(nutricionista));
        when(nutricionistaRepository.findByNome(any())).thenReturn(Optional.of(new Nutricionista()));
        assertThrows(RuntimeException.class, () -> nutricionistaService.atualizarNutricionista(1L, new NutricionistaRequestDTO()));
        verify(nutricionistaRepository).findById(anyLong());
        verify(nutricionistaRepository).findByNome(any());
    }
    @Test
    void atualizarNotThrow() {
        nutricionista.setNome("Nome");
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.of(nutricionista));
        when(nutricionistaRepository.findByNome(any())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> nutricionistaService.atualizarNutricionista(1L, new NutricionistaRequestDTO()));
        verify(nutricionistaRepository).findById(anyLong());
        verify(nutricionistaRepository).findByNome(any());
    }
    @Test
    void adicionarAno() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.of(nutricionista));
        assertDoesNotThrow(() -> nutricionistaService.adicionarAnoExperiencia(1L));
        verify(nutricionistaRepository).findById(anyLong());
    }
    @Test
    void adicionarAnoAssert() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            nutricionistaService.adicionarAnoExperiencia(0L);
        } catch (AssertionError ignored) {}
        assertThrows(AssertionError.class, () -> nutricionistaService.adicionarAnoExperiencia(0L));
        verify(nutricionistaRepository, times(2)).findById(anyLong());
    }
    @Test
    void adicionarCertificacao() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.of(nutricionista));
        assertDoesNotThrow(() -> nutricionistaService.adicionarCertificacao("Certificação", 1L));
        verify(nutricionistaRepository).findById(anyLong());
    }
    @Test
    void adicionarCertificacaoNull() {
        when(nutricionistaRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> nutricionistaService.adicionarCertificacao("Certificação", 0L));
        verify(nutricionistaRepository).findById(anyLong());
    }
}
