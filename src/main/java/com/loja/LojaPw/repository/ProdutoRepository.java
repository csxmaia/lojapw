package com.loja.LojaPw.repository;

import com.loja.LojaPw.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

    @Query(value = "SELECT * FROM produto prod " +
            "INNER JOIN marca m ON m.id = prod.marca_id " +
            "INNER JOIN categoria c ON c.id = prod.categoria_id " +
            "WHERE prod.descricao LIKE %:searchParam% " +
            "OR m.nome LIKE %:searchParam% " +
            "OR c.nome LIKE %:searchParam%",
            countQuery = "SELECT count(*) FROM produto prod " +
                    "INNER JOIN marca m ON m.id = prod.marca_id " +
                    "INNER JOIN categoria c ON c.id = prod.categoria_id " +
                    "WHERE prod.descricao LIKE %:searchParam% " +
                    "OR m.nome LIKE %:searchParam% " +
                    "OR c.nome LIKE %:searchParam%",
            nativeQuery = true)
    List<Produto> findByParam(@Param("searchParam") String searchParam);

}
