package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("Test")
public class NutricionistaServiceSpringTest {
    @Autowired
    NutricionistaService nutricionistaService;
    @MockBean
    NutricionistaRepository nutricionistaRepository;
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
    void salvar() {
        NutricionistaRequestDTO nutricionistaRequestDTO = new NutricionistaRequestDTO();
        when(nutricionistaRepository.save(any())).thenReturn(nutricionista);
        NutricionistaResponseDTO resultado = nutricionistaService.salvarNutricionista(nutricionistaRequestDTO);
        assertNotNull(resultado);
        assertEquals(nutricionistaRequestDTO.getNome(), resultado.getNome());
        verify(nutricionistaRepository).save(any());
    }
}
