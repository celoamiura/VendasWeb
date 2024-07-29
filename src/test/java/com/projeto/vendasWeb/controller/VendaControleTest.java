package com.projeto.vendasWeb.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.projeto.vendasWeb.models.Venda;
import com.projeto.vendasWeb.repo.AgendamentoRepositorio;
import com.projeto.vendasWeb.repo.ClienteRepositorio;
import com.projeto.vendasWeb.repo.ItemVendaRepositorio;
import com.projeto.vendasWeb.repo.ProdutoRepositorio;
import com.projeto.vendasWeb.repo.VendaRepositorio;

@WebMvcTest(VendaControle.class)
public class VendaControleTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendaRepositorio vendaRepositorio;

    @MockBean
    private ItemVendaRepositorio itemVendaRepositorio;

    @MockBean
    private ProdutoRepositorio produtoRepositorio;

    @MockBean
    private ClienteRepositorio clienteRepositorio;

    @MockBean
    private AgendamentoRepositorio agendamentoRepositorio;

    @InjectMocks
    private VendaControle vendaControle;

    private Venda venda;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        venda = new Venda();
        venda.setId(1L);
        venda.setValorTotal(BigDecimal.valueOf(100));
        venda.setDataVenda(LocalDate.now());
        venda.setQuantidadeTotal(2.0);
        venda.setCliente(null);  // Adicione um cliente conforme necessário

        mockMvc = MockMvcBuilders.standaloneSetup(vendaControle).build();
    }

    @Test
    public void testListarVendas() throws Exception {
        when(vendaRepositorio.findAll()).thenReturn(Arrays.asList(venda));

        mockMvc.perform(get("/listarVenda"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/vendas/lista"))
                .andExpect(model().attributeExists("listaVendas"));
    }

    @Test
    public void testCadastrarVenda() throws Exception {
        mockMvc.perform(get("/cadastroVenda"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/vendas/cadastro"))
                .andExpect(model().attributeExists("venda"))
                .andExpect(model().attributeExists("itemVenda"))
                .andExpect(model().attributeExists("listaItemVenda"))
                .andExpect(model().attributeExists("listaClientes"))
                .andExpect(model().attributeExists("listaProdutos"));
    }

    @Test
    public void testEditarVenda() throws Exception {
        when(vendaRepositorio.findById(1L)).thenReturn(Optional.of(venda));
        when(itemVendaRepositorio.buscarPorVenda(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/editarVenda/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/vendas/cadastro"))
                .andExpect(model().attributeExists("venda"))
                .andExpect(model().attributeExists("itemVenda"))
                .andExpect(model().attributeExists("listaItemVenda"))
                .andExpect(model().attributeExists("listaClientes"))
                .andExpect(model().attributeExists("listaProdutos"));
    }

    @Test
    public void testImprimirVenda() throws Exception {
        when(vendaRepositorio.findById(1L)).thenReturn(Optional.of(venda));
        when(itemVendaRepositorio.buscarPorVenda(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/imprimir-venda").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrativo/vendas/imprimir"))
                .andExpect(model().attributeExists("venda"))
                .andExpect(model().attributeExists("itensVenda"));
    }
}
