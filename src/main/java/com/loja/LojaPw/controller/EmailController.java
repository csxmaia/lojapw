package com.loja.LojaPw.controller;

import com.loja.LojaPw.entity.Funcionario;
import com.loja.LojaPw.repository.FuncionarioRepository;
import com.loja.LojaPw.service.EnviarEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;
import java.util.UUID;

@Controller
public class EmailController {

    @Autowired
    private FuncionarioRepository funcionarioRepositorio;

    @Autowired
    private EnviarEmail enviarEmail;

    @GetMapping("/solicitarRecSenha")
    public ModelAndView solicitarCodigo() {
        ModelAndView mv = new ModelAndView("/administrativo/recuperarSenha/solicitarToken");
        return mv;
    }

    @PostMapping("/solicitarRecSenha")
    public ModelAndView changePassword(@RequestParam(name = "email") String email, Model model){
        Funcionario funcionario = funcionarioRepositorio.findByEmail(email);
        if(funcionario == null) {
            model.addAttribute("messageResponse", "Funcionario não encontrado");
            ModelAndView mv = new ModelAndView("/administrativo/recuperarSenha/solicitarToken");
            return mv;
        }
        String uuid = UUID.randomUUID().toString();
        String random = uuid.substring(0, 5);
        enviarEmail.send(funcionario.getEmail(),"Codigo de recuperação de senha", random);
        funcionario.setTokenPassword(random);
        funcionarioRepositorio.saveAndFlush(funcionario);
        ModelAndView mv = new ModelAndView("/administrativo/recuperarSenha/alterarSenha");
        return mv;
    }

    @GetMapping("/alterarSenha")
    public ModelAndView alterarSenha() {
        ModelAndView mv = new ModelAndView("/administrativo/recuperarSenha/alterarSenha");
        return mv;
    }

    @PostMapping("/alterarSenha")
    public ModelAndView alterarSenha(@RequestParam(name = "email") String email, @RequestParam(name = "token") String token, @RequestParam(name = "novaSenha") String novaSenha, Model model) {
        Funcionario funcionario = funcionarioRepositorio.findByEmailAndTokenPassword(email, token);
        if(funcionario != null){
                funcionario.setTokenPassword(null);
                funcionario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
                funcionarioRepositorio.saveAndFlush(funcionario);
                model.addAttribute("messageResponse", "Senha alterada com sucesso");
        }
        ModelAndView mv = new ModelAndView("/administrativo/recuperarSenha/alterarSenha");
        return mv;
    }
}
