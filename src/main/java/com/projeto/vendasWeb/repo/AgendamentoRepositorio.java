package com.projeto.vendasWeb.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.vendasWeb.models.Agendamento;

@Repository
public interface AgendamentoRepositorio extends JpaRepository<Agendamento, Long> {
    Optional<Agendamento> findByVendaId(Long vendaId);
}
