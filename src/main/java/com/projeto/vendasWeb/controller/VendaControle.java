package com.projeto.vendasWeb.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.vendasWeb.models.Agendamento;
import com.projeto.vendasWeb.models.ItemVenda;
import com.projeto.vendasWeb.models.Produto;
import com.projeto.vendasWeb.models.Venda;
import com.projeto.vendasWeb.repo.AgendamentoRepositorio;
import com.projeto.vendasWeb.repo.ClienteRepositorio;
import com.projeto.vendasWeb.repo.ItemVendaRepositorio;
import com.projeto.vendasWeb.repo.ProdutoRepositorio;
import com.projeto.vendasWeb.repo.VendaRepositorio;

@RestController
public class VendaControle {

    @Autowired
    private VendaRepositorio vendaRepositorio;
    @Autowired
    private ItemVendaRepositorio itemVendaRepositorio;
    @Autowired
    private ProdutoRepositorio produtoRepositorio;
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private AgendamentoRepositorio agendamentoRepositorio;

    private List<ItemVenda> listaItemVenda = new ArrayList<>();

    @GetMapping("/cadastroVenda")
    public ModelAndView cadastrar(Venda venda, ItemVenda itemVenda) {
        ModelAndView mv = new ModelAndView("administrativo/vendas/cadastro");
        mv.addObject("venda", venda);
        mv.addObject("itemVenda", itemVenda);
        mv.addObject("listaItemVenda", this.listaItemVenda);
        mv.addObject("listaClientes", clienteRepositorio.findAll());
        mv.addObject("listaProdutos", produtoRepositorio.findAll());

        return mv;
    }

    @GetMapping("/listarVenda")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/vendas/lista");
        mv.addObject("listaVendas", vendaRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarVenda/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Venda> venda = vendaRepositorio.findById(id);
        venda.ifPresent(v -> this.listaItemVenda = itemVendaRepositorio.buscarPorVenda(v.getId()));
        return cadastrar(venda.orElse(new Venda()), new ItemVenda());
    }

    @GetMapping("/removerItemVenda/{id}")
    public ModelAndView removerItem(@PathVariable("id") Long id, Venda venda) {
        itemVendaRepositorio.deleteById(id);
        return cadastrar(venda, new ItemVenda());
    }

    @PostMapping("/salvarVenda")
    public ModelAndView salvar(String acao, Venda venda, ItemVenda itemVenda, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(venda, itemVenda);
        }

        if ("itens".equals(acao)) {
            adicionarItemVenda(venda, itemVenda);
        } else if ("salvar".equals(acao)) {
            venda.setDataVenda(LocalDate.now()); 
            salvarVenda(venda);
            return cadastrar(new Venda(), new ItemVenda());
        }
        return cadastrar(venda, new ItemVenda());
    }

    private void adicionarItemVenda(Venda venda, ItemVenda itemVenda) {
        BigDecimal precoVenda = itemVenda.getProduto().getPrecoVenda();
        BigDecimal subtotal = precoVenda.multiply(BigDecimal.valueOf(itemVenda.getQuantidade()));
        itemVenda.setValor(precoVenda);
        itemVenda.setSubtotal(subtotal.doubleValue());
        venda.setValorTotal(venda.getValorTotal().add(subtotal));
        venda.setQuantidadeTotal(venda.getQuantidadeTotal() + itemVenda.getQuantidade());

        this.listaItemVenda.add(itemVenda);
    }

    private void salvarVenda(Venda venda) {
        vendaRepositorio.saveAndFlush(venda);

        for (ItemVenda item : listaItemVenda) {
            item.setVenda(venda);
            itemVendaRepositorio.saveAndFlush(item);

            Optional<Produto> prodOpt = produtoRepositorio.findById(item.getProduto().getId());
            if (prodOpt.isPresent()) {
                Produto produto = prodOpt.get();
                produto.setEstoque(produto.getEstoque() - item.getQuantidade());
                produto.setPrecoVenda(item.getValor());
                produtoRepositorio.saveAndFlush(produto);
            }
        }
        this.listaItemVenda.clear();
    }

    @GetMapping("/imprimir-venda")
    public ModelAndView imprimirVenda(@RequestParam("id") Long id) {
        ModelAndView mv = new ModelAndView("administrativo/vendas/imprimir");
        Optional<Venda> venda = vendaRepositorio.findById(id);
        if (venda.isPresent()) {
            mv.addObject("venda", venda.get());
            mv.addObject("itensVenda", itemVendaRepositorio.buscarPorVenda(id));

            
            Optional<Agendamento> agendamento = agendamentoRepositorio.findByVendaId(id);
            if (agendamento.isPresent()) {
                mv.addObject("agendamento", agendamento.get());
            }
        } else {
            mv.addObject("erro", "Venda não encontrada");
        }
        return mv;
    }

    @GetMapping("/vendas/por-periodo")
    @ResponseBody
    public List<Venda> buscarVendasPorPeriodo(@RequestParam String periodo) {
        LocalDate inicioPeriodo = getInicioPeriodo(periodo);
        LocalDate fimPeriodo = getFimPeriodo(periodo);
        List<Venda> vendas = vendaRepositorio.findByDataVendaBetween(inicioPeriodo, fimPeriodo);
        return vendas;
    }

    @GetMapping("/itens-vendidos/por-periodo")
    public List<ItemVenda> buscarItensVendidosPorPeriodo(@RequestParam String periodo) {
        LocalDate inicioPeriodo = getInicioPeriodo(periodo);
        LocalDate fimPeriodo = getFimPeriodo(periodo);
        List<ItemVenda> itens = itemVendaRepositorio.findByVendaDataVendaBetween(inicioPeriodo, fimPeriodo);
        Map<Produto, Long> topItens = itens.stream()
                .collect(Collectors.groupingBy(ItemVenda::getProduto, Collectors.counting()));
        return topItens.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(e -> new ItemVenda(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    private LocalDate getInicioPeriodo(String periodo) {
        switch (periodo) {
            case "dia":
                return LocalDate.now().minusDays(1);
            case "semana":
                return LocalDate.now().minusWeeks(1);
            case "mes":
                return LocalDate.now().minusMonths(1);
            case "ano":
                return LocalDate.now().minusYears(1);
            default:
                return LocalDate.now().minusDays(1);
        }
    }

    private LocalDate getFimPeriodo(String periodo) {
        
        switch (periodo) {
            case "dia":
                return LocalDate.now().plusDays(1);
            case "semana":
                return LocalDate.now();
            case "mes":
                return LocalDate.now().plusMonths(1);
            case "ano":
                return LocalDate.now().plusYears(1);
            default:
                return LocalDate.now().plusDays(1);
        }
    }

    public IntPredicate getListaItemVenda() {
        return null;
    }
}
