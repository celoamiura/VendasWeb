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

import com.projeto.vendasWeb.models.Cidade;
import com.projeto.vendasWeb.repo.CidadeRepositorio;
import com.projeto.vendasWeb.repo.EstadoRepositorio;

@WebMvcTest(CidadeControle.class)
public class CidadeControleTest {

    @MockBean
    private CidadeRepositorio cidadeRepositorio;

    @MockBean
    private EstadoRepositorio estadoRepositorio;

    @InjectMocks
    private CidadeControle cidadeControle;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cidadeControle).build();
    }

    @Test
    public void testCadastrar() throws Exception {
        when(estadoRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cadastroCidade"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/cidades/cadastro"))
                .andExpect(model().attributeExists("cidade"))
                .andExpect(model().attributeExists("listaEstados"));
    }

    @Test
    public void testListar() throws Exception {
        when(cidadeRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/listarCidade"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/cidades/lista"))
                .andExpect(model().attributeExists("listaCidades"));
    }

    @Test
    public void testEditar() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1L);

        when(cidadeRepositorio.findById(1L)).thenReturn(Optional.of(cidade));
        when(estadoRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/editarCidade/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/cidades/cadastro"))
                .andExpect(model().attributeExists("cidade"))
                .andExpect(model().attributeExists("listaEstados"));
    }

    @Test
    public void testRemover() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1L);

        when(cidadeRepositorio.findById(1L)).thenReturn(Optional.of(cidade));
        when(cidadeRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/removerCidade/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/cidades/lista"))
                .andExpect(model().attributeExists("listaCidades"));
    }

    @Test
    public void testSalvar() throws Exception {
        Cidade cidade = new Cidade();
        cidade.setId(1L);

        when(cidadeRepositorio.saveAndFlush(any(Cidade.class))).thenReturn(cidade);
        when(estadoRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/salvarCidade")
                .flashAttr("cidade", cidade))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/cidades/cadastro"))
                .andExpect(model().attributeExists("cidade"))
                .andExpect(model().attributeExists("listaEstados"));
    }
}
