package com.projeto.vendasWeb.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.vendasWeb.models.Venda;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {
    List<Venda> findByDataVendaBetween(LocalDate inicio, LocalDate fim);
}