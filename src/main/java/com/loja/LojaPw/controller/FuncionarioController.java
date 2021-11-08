package com.loja.LojaPw.controller;

import com.loja.LojaPw.entity.Funcionario;
import com.loja.LojaPw.entity.Papel;
import com.loja.LojaPw.entity.Permissao;
import com.loja.LojaPw.repository.CidadeRepository;
import com.loja.LojaPw.repository.FuncionarioRepository;
import com.loja.LojaPw.repository.PermissaoRepository;
import com.loja.LojaPw.service.EnviarEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Controller
@RequestMapping("/administrativo/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioRepository funcionarioRepositorio;

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EnviarEmail enviarEmail;

	@GetMapping("/cadastrar")
	public ModelAndView cadastrar(Funcionario funcionario) {
		ModelAndView mv = new ModelAndView("administrativo/funcionarios/cadastro");
		mv.addObject("funcionario",funcionario);
		mv.addObject("listaCidades",cidadeRepository.findAll());
		return mv;
	}
	
	@GetMapping("/listar")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("/administrativo/funcionarios/lista");
		mv.addObject("listaFuncionarios", funcionarioRepositorio.findAll());
		return mv;
	}
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		Optional<Funcionario> funcionario = funcionarioRepositorio.findById(id);
		return cadastrar(funcionario.get());
	}
	
	@GetMapping("/remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {
		Optional<Funcionario> funcionario = funcionarioRepositorio.findById(id);
		funcionarioRepositorio.delete(funcionario.get());
		return listar();
	}
	
	@PostMapping("/salvar")
	public ModelAndView salvar(@Validated Funcionario funcionario, BindingResult result) {
		if(result.hasErrors()) {
			return cadastrar(funcionario);
		}

		String uuid = UUID.randomUUID().toString();
		String random = uuid.substring(0, 7);

		funcionario.setSenha(new BCryptPasswordEncoder().encode(random));

		enviarEmail.send(funcionario.getEmail(), "Funcionario registrado", "Foi registrado um funcionario no sistema " +
				"com esse email, suas informações para login são: login: " + funcionario.getEmail() + " senha: " + random);

		funcionarioRepositorio.saveAndFlush(funcionario);
		Permissao permissao = new Permissao();
		permissao.setFuncionario(funcionario);
		Papel papel = new Papel();
		papel.setId(1L);
		permissao.setPapel(papel);
		permissaoRepository.save(permissao);
		return cadastrar(new Funcionario());
	}

}