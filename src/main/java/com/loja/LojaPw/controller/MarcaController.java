package com.loja.LojaPw.controller;

import java.util.Optional;

import com.loja.LojaPw.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.loja.LojaPw.entity.Marca;

@Controller
@RequestMapping("/administrativo/marcas")
public class MarcaController {
	
	@Autowired
	private MarcaRepository marcaRepository;
	
	@GetMapping("/cadastrar")
	public ModelAndView cadastrar(Marca marca) {
		ModelAndView mv = new ModelAndView("administrativo/marca/cadastro");
		mv.addObject("marca", marca);
		return mv;
	}
	
	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("/administrativo/marca/lista");
		mv.addObject("listaMarcas", marcaRepository.findAll());
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Marca> marca = marcaRepository.findById(id);
		return cadastrar(marca.get());
	}
	
	@GetMapping("/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Marca> marca = marcaRepository.findById(id);
		marcaRepository.delete(marca.get());
		return listar();
	}
	
	@PostMapping("/salvar")
	public ModelAndView salvar(@Validated Marca marca, BindingResult result) {
		if(result.hasErrors()) {
	
			return cadastrar(marca);
		}
		
		marcaRepository.saveAndFlush(marca);
		
		return cadastrar(new Marca());
	}

}
