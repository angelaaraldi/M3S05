package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.entities.Consulta;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.entities.Paciente;
import br.com.exemplo.aula.repositories.ConsultaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
public class ConsultaControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    ConsultaRepository consultaRepository;
    Consulta consulta;
    @BeforeEach
    public void setup() {
        consulta = new Consulta();
        consulta.setNutricionista(new Nutricionista());
        consulta.getNutricionista().setNome("Nome");
        consulta.setPaciente(new Paciente());
        consulta.setObservacoes("Observações");
    }
    @Test
    void buscar() throws Exception {
        when(consultaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(consulta));
        mvc.perform(get("/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacoes").value(consulta.getObservacoes()));
        verify(consultaRepository).findById(anyLong());
    }
    @Test
    void remover() throws Exception {
        doNothing().when(consultaRepository).deleteById(anyLong());
        mvc.perform(delete("/consultas/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
        verify(consultaRepository).deleteById(anyLong());
    }
    @Test
    void atualizar() throws Exception {
        when(consultaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(consulta));
        when(consultaRepository.save(any())).thenReturn(consulta);
        mvc.perform(put("/consultas/1").contentType(MediaType.APPLICATION_JSON).content("""
                             {
                                 "idNutricionista": 1,
                                 "idPaciente": 1,
                                 "observacoes": "Observações"
                             }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacoes").value(consulta.getObservacoes()));
        verify(consultaRepository).findById(anyLong());
        verify(consultaRepository).save(any());
    }
    @Test
    void buscarNull() throws Exception {
        when(consultaRepository.findById(anyLong())).thenReturn(Optional.empty());
        mvc.perform(get("/consultas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(consultaRepository).findById(anyLong());
    }
    @Test
    void atualizarNull() throws Exception {
        when(consultaRepository.findById(anyLong())).thenReturn(Optional.empty());
        mvc.perform(put("/consultas/1").contentType(MediaType.APPLICATION_JSON).content("""
                             {
                                 "idNutricionista": 1,
                                 "idPaciente": 1,
                                 "observacoes": "Observações"
                             }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(consultaRepository).findById(anyLong());
    }
    @Test
    void listar() throws Exception {
        when(consultaRepository.findAll()).thenReturn(List.of(consulta));
        mvc.perform(get("/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeNutricionista").value(consulta.getNutricionista().getNome()));
        verify(consultaRepository).findAll();
    }
    @Test
    void salvar() throws Exception {
        when(consultaRepository.save(any())).thenReturn(consulta);
        mvc.perform(post("/consultas").contentType(MediaType.APPLICATION_JSON).content("""
                             {
                                 "idNutricionista": 1,
                                 "idPaciente": 1,
                                 "observacoes": "Observações"
                             }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacoes").value(consulta.getObservacoes()));
        verify(consultaRepository).save(any());
    }
    @Test
    void listarEmpty() throws Exception {
        when(consultaRepository.findAll()).thenReturn(List.of());
        mvc.perform(get("/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(consultaRepository).findAll();
    }
}
