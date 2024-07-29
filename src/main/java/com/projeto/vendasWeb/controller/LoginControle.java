package com.projeto.vendasWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projeto.vendasWeb.models.Usuario;
import com.projeto.vendasWeb.repo.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginControle {

    private final UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public LoginControle(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Credenciais inválidas. Tente novamente.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Logout realizado com sucesso.");
        }
        return "administrativo/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {
        AuthenticationService authService = new AuthenticationService(usuarioRepositorio);
        if (authService.authenticate(email, password)) {
            Usuario usuario = usuarioRepositorio.findByEmail(email);
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/administrativo";
        } else {
            model.addAttribute("errorMsg", "Credenciais inválidas. Tente novamente.");
            return "administrativo/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    private static class AuthenticationService {

        private final UsuarioRepositorio usuarioRepositorio;

        public AuthenticationService(UsuarioRepositorio usuarioRepositorio) {
            this.usuarioRepositorio = usuarioRepositorio;
        }

        public boolean authenticate(String email, String password) {
            Usuario usuario = usuarioRepositorio.findByEmailAndPassword(email, password);
            return usuario != null;
        }
    }
}
