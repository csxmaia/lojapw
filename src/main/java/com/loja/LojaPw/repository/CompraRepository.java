package com.loja.LojaPw.repository;

import com.loja.LojaPw.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query(value = "select SUM(valor_total) from compra", nativeQuery = true)
    Long faturamentoTotal();

    @Query(value = "select SUM(valor_total) from compra WHERE YEAR(data_compra) = YEAR(CURRENT_DATE()) AND \n" +
            "      MONTH(data_compra) = MONTH(CURRENT_DATE());", nativeQuery = true)
    Long faturamentoMensal();

}
