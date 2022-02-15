package com.loja.LojaPw.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "itens_compra")
public class ItensCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Compra compra;

    private Integer quantidade;

    private Double valorUnitario = 0.0;

    private Double valorTotal = 0.0;

    public Integer getQuantidade() {
        if(quantidade == null) {
            quantidade = 0;
        }
        return quantidade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, produto, compra, quantidade, valorUnitario, valorTotal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItensCompra that = (ItensCompra) o;
        return Objects.equals(id, that.id) && Objects.equals(produto, that.produto) && Objects.equals(compra, that.compra) && Objects.equals(quantidade, that.quantidade) && Objects.equals(valorUnitario, that.valorUnitario) && Objects.equals(valorTotal, that.valorTotal);
    }
}
