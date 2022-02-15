package com.loja.LojaPw.repository;

import com.loja.LojaPw.entity.ItensCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItensCompraRepository extends JpaRepository<ItensCompra, Long> {

    @Query(value = "select pro.*, SUM(itens_compra.quantidade) as quantidade from itens_compra\n" +
            "inner join compra as com on com.id = itens_compra.compra_id \n" +
            "inner join produto as pro on pro.id = itens_compra.produto_id \n" +
            "WHERE YEAR(com.data_compra) = YEAR(CURRENT_DATE()) AND MONTH(com.data_compra) = MONTH(CURRENT_DATE())\n" +
            "group by (produto_id)\n" +
            "order by sum(quantidade) desc\n" +
            "limit 1", nativeQuery = true)
    ItensCompra produtoMaisVendidoMes();

}
