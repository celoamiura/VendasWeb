package com.projeto.vendasWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.projeto.vendasWeb.repo.VendaRepositorio;

@Controller
public class RelatorioControle {

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @GetMapping("/relatorio-vendas")
    public String relatorioVendas(Model model) {
        model.addAttribute("vendas", vendaRepositorio.findAll());
        return "relatorio-vendas";
    }
}
