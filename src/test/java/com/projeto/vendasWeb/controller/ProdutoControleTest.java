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

import com.projeto.vendasWeb.models.Produto;
import com.projeto.vendasWeb.repo.ProdutoRepositorio;

@WebMvcTest(ProdutoControle.class)
public class ProdutoControleTest {

    @MockBean
    private ProdutoRepositorio produtoRepositorio;

    @InjectMocks
    private ProdutoControle produtoControle;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(produtoControle).build();
    }

    @Test
    public void testCadastrar() throws Exception {
        mockMvc.perform(get("/cadastroProduto"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/produtos/cadastro"))
                .andExpect(model().attributeExists("produto"));
    }

    @Test
    public void testListar() throws Exception {
        when(produtoRepositorio.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/listarProduto"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/produtos/lista"))
                .andExpect(model().attributeExists("listaProdutos"));
    }

    @Test
    public void testEditar() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");

        when(produtoRepositorio.findById(1L)).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/editarProduto/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/produtos/cadastro"))
                .andExpect(model().attributeExists("produto"));
    }

    @Test
    public void testRemover() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");

        when(produtoRepositorio.findById(1L)).thenReturn(Optional.of(produto));

        mockMvc.perform(get("/removerProduto/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/produtos/lista"))
                .andExpect(model().attributeExists("listaProdutos"));
    }

    @Test
    public void testSalvar() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");

        when(produtoRepositorio.saveAndFlush(any(Produto.class))).thenReturn(produto);

        mockMvc.perform(post("/salvarProduto")
                .flashAttr("produto", produto))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/produtos/cadastro"))
                .andExpect(model().attributeExists("produto"));
    }
}
