package com.loja.LojaPw.controller;

import com.loja.LojaPw.repository.ClienteRepository;
import com.loja.LojaPw.repository.CompraRepository;
import com.loja.LojaPw.repository.ItensCompraRepository;
import com.loja.LojaPw.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrincipalController {

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping("/administrativo")
	public ModelAndView acessarPrincipal() {
		ModelAndView mv = new ModelAndView("administrativo/home");
		mv.addObject("qtdProdutos", produtoRepository.quantidadeProdutosCadastrados());
		mv.addObject("qtdClientes", clienteRepository.quantidadeClientesCadastrados());
		mv.addObject("faturamentoTotal", compraRepository.faturamentoTotal());
		mv.addObject("faturamentoMensal", compraRepository.faturamentoMensal());
		return mv;
//		return "administrativo/home";
	}
}
