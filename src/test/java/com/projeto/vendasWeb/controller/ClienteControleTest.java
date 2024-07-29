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

import com.projeto.vendasWeb.models.Cliente;
import com.projeto.vendasWeb.repo.ClienteRepositorio;
import com.projeto.vendasWeb.repo.CidadeRepositorio;

@WebMvcTest(ClienteControle.class)
public class ClienteControleTest {

    @MockBean
    private ClienteRepositorio clienteRepositorio;

    @MockBean
    private CidadeRepositorio cidadeRepositorio;

    @InjectMocks
    private ClienteControle clienteControle;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteControle).build();
    }

    @Test
    public void testCadastrar() throws Exception {
        when(cidadeRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cadastroCliente"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/clientes/cadastro"))
                .andExpect(model().attributeExists("cliente"))
                .andExpect(model().attributeExists("listaCidades"));
    }

    @Test
    public void testListar() throws Exception {
        when(clienteRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/listarCliente"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/clientes/lista"))
                .andExpect(model().attributeExists("listaClientes"));
    }

    @Test
    public void testEditar() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepositorio.findById(1L)).thenReturn(Optional.of(cliente));
        when(cidadeRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/editarCliente/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/clientes/cadastro"))
                .andExpect(model().attributeExists("cliente"))
                .andExpect(model().attributeExists("listaCidades"));
    }

    @Test
    public void testRemover() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepositorio.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/removerCliente/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/clientes/lista"))
                .andExpect(model().attributeExists("listaClientes"));
    }

    @Test
    public void testSalvar() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepositorio.saveAndFlush(any(Cliente.class))).thenReturn(cliente);
        when(cidadeRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/salvarCliente")
                .flashAttr("cliente", cliente))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/clientes/cadastro"))
                .andExpect(model().attributeExists("cliente"))
                .andExpect(model().attributeExists("listaCidades"));
    }

    @Test
    public void testRelatorioClientes() throws Exception {
        when(clienteRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/relatorioClientes"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/clientes/relatorio"))
                .andExpect(model().attributeExists("listaClientes"));
    }
}
