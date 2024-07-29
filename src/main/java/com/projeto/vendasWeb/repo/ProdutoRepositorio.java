package com.projeto.vendasWeb.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.vendasWeb.models.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {
	
}
