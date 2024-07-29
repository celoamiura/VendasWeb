package com.projeto.vendasWeb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.vendasWeb.models.Cidade;

public interface CidadeRepositorio extends JpaRepository<Cidade, Long> {
	
}
