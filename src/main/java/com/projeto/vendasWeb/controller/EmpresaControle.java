package com.projeto.vendasWeb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.vendasWeb.models.Empresa;
import com.projeto.vendasWeb.repo.EmpresaRepositorio;

@Controller
public class EmpresaControle {
    
    @Autowired
    private EmpresaRepositorio empresaRepositorio;
    
    @GetMapping("/cadastroEmpresa")
    public ModelAndView cadastrar(Empresa empresa) {
        ModelAndView mv = new ModelAndView("administrativo/empresas/cadastro");
        mv.addObject("empresa", empresa);
        return mv;
    }
    
    @GetMapping("/listarEmpresa")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/empresas/lista");
        mv.addObject("listaEmpresas", empresaRepositorio.findAll());
        return mv;
    }
    
    @GetMapping("/editarEmpresa/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Empresa> empresa = empresaRepositorio.findById(id);
        return cadastrar(empresa.get());
    }
    
    @GetMapping("/removerEmpresa/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Empresa> empresa = empresaRepositorio.findById(id);
        empresaRepositorio.delete(empresa.get());
        return listar();
    }

    @PostMapping("/salvarEmpresa")
    public ModelAndView salvar(Empresa empresa, BindingResult result) {
        if(result.hasErrors()) {
            return cadastrar(empresa);
        }
        empresaRepositorio.saveAndFlush(empresa);
        return cadastrar(new Empresa());
    }

    @GetMapping("/relatorioEmpresas")
    public ModelAndView relatorioEmpresas() {
        ModelAndView mv = new ModelAndView("administrativo/empresas/relatorio");
        mv.addObject("listaEmpresas", empresaRepositorio.findAll());
        return mv;
    }
}
