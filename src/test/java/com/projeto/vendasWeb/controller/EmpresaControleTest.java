package com.projeto.vendasWeb.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.vendasWeb.models.Empresa;
import com.projeto.vendasWeb.repo.EmpresaRepositorio;

public class EmpresaControleTest {

    @Mock
    private EmpresaRepositorio empresaRepositorioMock;

    @InjectMocks
    private EmpresaControle empresaControle;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEditarEmpresa() {
        Long id = 1L;
        Empresa empresaMock = new Empresa();
        empresaMock.setId(id);
        when(empresaRepositorioMock.findById(id)).thenReturn(Optional.of(empresaMock));

        ModelAndView mav = empresaControle.editar(id);

        assertEquals("administrativo/empresas/cadastro", mav.getViewName());
        assertEquals(empresaMock, mav.getModel().get("empresa"));
    }

    @Test
    public void testRemoverEmpresa() {
        Long id = 1L;
        Empresa empresaMock = new Empresa();
        empresaMock.setId(id);
        when(empresaRepositorioMock.findById(id)).thenReturn(Optional.of(empresaMock));

        ModelAndView mav = empresaControle.remover(id);

        assertEquals("administrativo/empresas/lista", mav.getViewName());
        verify(empresaRepositorioMock, times(1)).delete(empresaMock);
    }

    @Test
    public void testSalvarEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setNome("Teste");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView mav = empresaControle.salvar(empresa, bindingResult);

        assertEquals("administrativo/empresas/cadastro", mav.getViewName());
        assertNotNull(mav.getModel().get("empresa"));

        verify(empresaRepositorioMock, times(1)).saveAndFlush(empresa);
    }
}
