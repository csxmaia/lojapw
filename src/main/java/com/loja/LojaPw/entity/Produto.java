package com.loja.LojaPw.entity;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "produto")
public class Produto implements Serializable {

	public Produto() {
		super();
	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String descricao;
	
	private Double valorVenda;
	
	@ManyToOne
	private Categoria categoria;

	@ManyToOne
	private Marca marca;
	
	private Double quantidadeEstoque = 0.0;

	private String nomeImagem;

}