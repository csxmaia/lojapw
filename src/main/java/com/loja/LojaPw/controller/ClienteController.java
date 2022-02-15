package com.loja.LojaPw.controller;

import com.loja.LojaPw.entity.Cliente;
import com.loja.LojaPw.repository.CidadeRepository;
import com.loja.LojaPw.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepositorio;

    @Autowired
    private CidadeRepository cidadeRepositorio;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(Cliente cliente) {
        ModelAndView mv =  new ModelAndView("cliente/cadastrar");
        mv.addObject("cliente",cliente);
        mv.addObject("listaCidades",cidadeRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteRepositorio.findById(id);
        return cadastrar(cliente.get());
    }


    @PostMapping("/salvar")
    public ModelAndView salvar(@Validated Cliente cliente, BindingResult result) {

        if(result.hasErrors()) {
            return cadastrar(cliente);
        }
        cliente.setSenha(new BCryptPasswordEncoder().encode(cliente.getSenha()));
        clienteRepositorio.saveAndFlush(cliente);

        return cadastrar(new Cliente());
    }

}

