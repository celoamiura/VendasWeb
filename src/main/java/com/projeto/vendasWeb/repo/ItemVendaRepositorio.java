package com.projeto.vendasWeb.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.projeto.vendasWeb.models.ItemVenda;

public interface ItemVendaRepositorio extends JpaRepository<ItemVenda, Long> {
	@Query("SELECT e FROM ItemVenda e WHERE e.venda.id = ?1")
	List<ItemVenda> buscarPorVenda(long id);

	List<ItemVenda> findByVendaDataVendaBetween(LocalDate inicio, LocalDate fim);

    Object findByVendaId(long anyLong);

}