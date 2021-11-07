package com.loja.LojaPw.repository;

import com.loja.LojaPw.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
