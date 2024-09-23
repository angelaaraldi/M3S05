package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.services.PacienteService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PacienteController.class)
@AutoConfigureMockMvc
@ActiveProfiles("Test")
public class PacienteControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    PacienteService pacienteService;
    PacienteResponseDTO pacienteResponseDTO;
    @BeforeEach
    public void setup() {
        pacienteResponseDTO = new PacienteResponseDTO();
        pacienteResponseDTO.setNome("Nome");
        pacienteResponseDTO.setCpf("000.000.000-00");
    }
    @Test
    void salvar() throws Exception {
        when(pacienteService.salvarPaciente(any())).thenReturn(pacienteResponseDTO);
        mvc.perform(post("/pacientes").contentType(MediaType.APPLICATION_JSON).content("""
                             {
                                 "nome": "Nome",
                                 "cpf": "000.000.000-00"
                             }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(pacienteResponseDTO.getNome()))
                .andExpect(jsonPath("$.cpf").value(pacienteResponseDTO.getCpf()));
        verify(pacienteService).salvarPaciente(any());
    }
    @Test
    void listar() throws Exception {
        when(pacienteService.listarPacientes()).thenReturn(List.of(pacienteResponseDTO));
        mvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(pacienteResponseDTO.getNome()))
                .andExpect(jsonPath("$[0].cpf").value(pacienteResponseDTO.getCpf()));
        verify(pacienteService).listarPacientes();
    }
    @Test
    void buscar() throws Exception {
        when(pacienteService.buscarPaciente(anyLong())).thenReturn(pacienteResponseDTO);
        mvc.perform(get("/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(pacienteResponseDTO.getNome()))
                .andExpect(jsonPath("$.cpf").value(pacienteResponseDTO.getCpf()));
        verify(pacienteService).buscarPaciente(anyLong());
    }
    @Test
    void remover() throws Exception {
        doNothing().when(pacienteService).removerPaciente(anyLong());
        mvc.perform(delete("/pacientes/1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
        verify(pacienteService).removerPaciente(anyLong());
    }
    @Test
    void atualizar() throws Exception {
        when(pacienteService.atualizarPaciente(anyLong(), any())).thenReturn(pacienteResponseDTO);
        mvc.perform(put("/pacientes/1").contentType(MediaType.APPLICATION_JSON).content("""
                             {
                                 "nome": "Nome",
                                 "cpf": "000.000.000-00"
                             }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(pacienteResponseDTO.getNome()))
                .andExpect(jsonPath("$.cpf").value(pacienteResponseDTO.getCpf()));
        verify(pacienteService).atualizarPaciente(anyLong(), any());
    }
    @Test
    void listarEmpty() throws Exception {
        when(pacienteService.listarPacientes()).thenReturn(List.of());
        mvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(pacienteService).listarPacientes();
    }
    @Test
    void buscarNull() throws Exception {
        when(pacienteService.buscarPaciente(anyLong())).thenReturn(null);
        try {
            mvc.perform(get("/pacientes/1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$").doesNotExist());
        } catch (ServletException ignored) {}
        verify(pacienteService).buscarPaciente(anyLong());
    }
    @Test
    void atualizarNull() throws Exception {
        when(pacienteService.atualizarPaciente(anyLong(), any())).thenReturn(null);
        try {
            mvc.perform(put("/pacientes/1").contentType(MediaType.APPLICATION_JSON).content("""
                             {
                                 "nome": "Nome",
                                 "cpf": "000.000.000-00"
                             }"""))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$").doesNotExist());
        } catch (ServletException ignored) {}
        verify(pacienteService).atualizarPaciente(anyLong(), any());
    }
}
