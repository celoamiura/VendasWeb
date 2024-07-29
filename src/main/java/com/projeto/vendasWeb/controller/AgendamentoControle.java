package com.projeto.vendasWeb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.vendasWeb.models.Agendamento;
import com.projeto.vendasWeb.repo.AgendamentoRepositorio;
import com.projeto.vendasWeb.repo.VendaRepositorio;

@Controller
public class AgendamentoControle {

    @Autowired
    private AgendamentoRepositorio agendamentoRepositorio;

    @Autowired
    private VendaRepositorio vendaRepositorio;

    @GetMapping("/cadastroAgendamento")
    public ModelAndView cadastrar(Agendamento agendamento) {
        ModelAndView mv = new ModelAndView("administrativo/agendamentos/cadastro");
        mv.addObject("agendamento", agendamento);
        mv.addObject("listaVendas", vendaRepositorio.findAll());
        return mv;
    }

    @GetMapping("/listarAgendamentos")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/agendamentos/lista");
        mv.addObject("listaAgendamentos", agendamentoRepositorio.findAll());
        return mv;
    }

    @PostMapping("/salvarAgendamento")
    public ModelAndView salvar(Agendamento agendamento, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(agendamento);
        }

        agendamentoRepositorio.saveAndFlush(agendamento);
        return new ModelAndView("redirect:/listarAgendamentos");
    }

    @GetMapping("/editarAgendamento/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Agendamento> agendamento = agendamentoRepositorio.findById(id);
        return cadastrar(agendamento.orElse(new Agendamento()));
    }

    @GetMapping("/removerAgendamento/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        agendamentoRepositorio.deleteById(id);
        return new ModelAndView("redirect:/listarAgendamentos");
    }
}
