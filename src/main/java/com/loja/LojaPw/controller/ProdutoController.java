package com.loja.LojaPw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.loja.LojaPw.entity.Imagem;
import com.loja.LojaPw.repository.ImagemRepository;
import com.loja.LojaPw.repository.MarcaRepository;
import com.loja.LojaPw.constants.ConstantsImagens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.loja.LojaPw.entity.Produto;
import com.loja.LojaPw.repository.ProdutoRepository;

@Controller
@RequestMapping("/administrativo/produtos")
public class ProdutoController {

    ConstantsImagens constantsImagens;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar(Produto produto) {
        ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
        mv.addObject("produto", produto);
        mv.addObject("listaMarcas", marcaRepository.findAll());
        return mv;
    }

    @GetMapping("/listar")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        mv.addObject("listaProdutos", produtoRepository.findAll());
        return mv;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return cadastrar(produto.get());
    }

    @GetMapping("/remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        produtoRepository.delete(produto.get());
        return listar();
    }

    @ResponseBody
    @GetMapping("/mostrarImagem/{imagem}")
    public byte[] retornarImagem(@PathVariable("imagem") String imagem) {
        if (imagem != null || imagem.trim().length() > 0) {
            File imagemArquivo = new File(constantsImagens.CAMINHO_PASTA_IMAGENS + imagem);
            try {
                return Files.readAllBytes(imagemArquivo.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @PostMapping("/salvar")
    public ModelAndView salvar(@Validated Produto produto, BindingResult result, @RequestParam("file") List<MultipartFile> files) {

        if (result.hasErrors()) {
            return cadastrar(produto);
        }
        produtoRepository.saveAndFlush(produto);

        try {
            if (files.size() > 0) {
                for(MultipartFile file : files) {
                    Path pathImage = Paths.get(constantsImagens.CAMINHO_PASTA_IMAGENS + produto.getId().toString() + "-" + file.getOriginalFilename());
                    byte[] bytes = file.getBytes();
                    Files.write(pathImage, bytes);

                    Imagem imagem = new Imagem();
                    imagem.setNome(produto.getId().toString() +  "-" + file.getOriginalFilename());
                    imagem.setProduto(produto);
                    imagemRepository.saveAndFlush(imagem);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return cadastrar(new Produto());
    }

}
