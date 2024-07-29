package com.projeto.vendasWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.vendasWeb.models.Estado;
import com.projeto.vendasWeb.repo.EstadoRepositorio;

@Controller
public class EstadoControle {

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroEstado")
    public ModelAndView cadastrar(Estado estado) {
        ModelAndView mv = new ModelAndView("administrativo/estados/cadastro");
        mv.addObject("estado", estado);
        return mv;
    }

    @GetMapping("/listarEstado")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/estados/lista");
        mv.addObject("listaEstados", estadoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarEstado/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        return cadastrar(estadoRepositorio.findById(id).orElse(new Estado()));
    }

    @GetMapping("/removerEstado/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        estadoRepositorio.deleteById(id);
        return listar();
    }

    @PostMapping("/salvarEstado")
    public ModelAndView salvar(Estado estado, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(estado);
        }

        estadoRepositorio.saveAndFlush(estado);
        return cadastrar(new Estado());
    }

    @GetMapping("/relatorioEstados")
    public ModelAndView relatorioEstados() {
        ModelAndView mv = new ModelAndView("administrativo/estados/relatorio");
        mv.addObject("listaEstados", estadoRepositorio.findAll());
        return mv;
    }
}
