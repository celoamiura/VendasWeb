package com.projeto.vendasWeb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.projeto.vendasWeb.models.Estado;
import com.projeto.vendasWeb.repo.EstadoRepositorio;

@WebMvcTest(EstadoControle.class)
public class EstadoControleTest {

    @MockBean
    private EstadoRepositorio estadoRepositorio;

    @InjectMocks
    private EstadoControle estadoControle;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(estadoControle).build();
    }

    @Test
    public void testCadastrar() throws Exception {
        mockMvc.perform(get("/cadastroEstado"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/estados/cadastro"))
                .andExpect(model().attributeExists("estado"));
    }

    @Test
    public void testListar() throws Exception {
        when(estadoRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/listarEstado"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/estados/lista"))
                .andExpect(model().attributeExists("listaEstados"));
    }

    @Test
    public void testEditar() throws Exception {
        Estado estado = new Estado();
        estado.setId(1L);

        when(estadoRepositorio.findById(1L)).thenReturn(Optional.of(estado));

        mockMvc.perform(get("/editarEstado/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/estados/cadastro"))
                .andExpect(model().attributeExists("estado"));
    }

    @Test
    public void testRemover() throws Exception {
        mockMvc.perform(get("/removerEstado/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/estados/lista"))
                .andExpect(model().attributeExists("listaEstados"));
    }

    @Test
    public void testSalvar() throws Exception {
        Estado estado = new Estado();
        estado.setId(1L);

        when(estadoRepositorio.saveAndFlush(any(Estado.class))).thenReturn(estado);

        mockMvc.perform(post("/salvarEstado")
                .flashAttr("estado", estado))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/estados/cadastro"))
                .andExpect(model().attributeExists("estado"));
    }

    @Test
    public void testRelatorioEstados() throws Exception {
        when(estadoRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/relatorioEstados"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/estados/relatorio"))
                .andExpect(model().attributeExists("listaEstados"));
    }
}
