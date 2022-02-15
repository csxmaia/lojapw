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

    @Query(value = "SELECT COUNT(*) from produto", nativeQuery = true)
    Long quantidadeProdutosCadastrados();

    @Query(value = "select pro.*, SUM(itens_compra.quantidade) as quantidade from itens_compra\n" +
            "inner join compra as com on com.id = itens_compra.compra_id \n" +
            "inner join produto as pro on pro.id = itens_compra.produto_id \n" +
            "WHERE YEAR(com.data_compra) = YEAR(CURRENT_DATE()) AND MONTH(com.data_compra) = MONTH(CURRENT_DATE())\n" +
            "group by (produto_id)\n" +
            "order by sum(quantidade) desc\n" +
            "limit 1", nativeQuery = true)
    Produto produtoMaisVendidoMes();
}
