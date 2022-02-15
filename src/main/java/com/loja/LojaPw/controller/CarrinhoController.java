package com.loja.LojaPw.controller;

import com.loja.LojaPw.entity.Cliente;
import com.loja.LojaPw.entity.Compra;
import com.loja.LojaPw.entity.ItensCompra;
import com.loja.LojaPw.entity.Produto;
import com.loja.LojaPw.repository.ClienteRepository;
import com.loja.LojaPw.repository.CompraRepository;
import com.loja.LojaPw.repository.ItensCompraRepository;
import com.loja.LojaPw.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CarrinhoController {
    private Compra compra = new Compra();
    private Cliente cliente;

    private List<ItensCompra> listItensCompras = new ArrayList<>();

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ItensCompraRepository itensCompraRepository;


    private void calcularTotal() {
        compra.setValorTotal(0.);
        for (ItensCompra it : listItensCompras) {
            compra.setValorTotal(compra.getValorTotal() + it.getValorTotal());
        }
    }

    @GetMapping("/carrinho")
    public ModelAndView carrinho() {
        ModelAndView mv = new ModelAndView("/cliente/carrinho");
        calcularTotal();
        mv.addObject("compra", compra);
        mv.addObject("listaItens", listItensCompras);
        return mv;
    }

    @GetMapping("/alterarQuantidade/{id}/{acao}")
    public String alterarQuantidade(@PathVariable Long id, @PathVariable Integer acao) {
        for (ItensCompra it : listItensCompras) {
            if (it.getProduto().getId().equals(id)) {
                if (acao.equals(1)) {
                    it.setQuantidade(it.getQuantidade() + 1);
                    it.setValorTotal(0.0);
                    it.setValorTotal(it.getValorTotal() + it.getQuantidade() * it.getValorUnitario());
                } else if (acao == 0) {
                    it.setQuantidade(it.getQuantidade() - 1);
                    it.setValorTotal(0.0);
                    it.setValorTotal(it.getValorTotal() + it.getQuantidade() * it.getValorUnitario());
                }
                break;
            }
        }

        return "redirect:/carrinho";
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String  adicionarCarrinho(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        int controle = 0;
        for(ItensCompra ic : listItensCompras){
            if(ic.getProduto().getId().equals(produto.get().getId())){
                controle = 1;
                ic.setQuantidade(ic.getQuantidade() + 1);
                ic.setValorTotal(0.0);
                ic.setValorTotal(ic.getValorTotal() + ic.getQuantidade() * ic.getValorUnitario());
                break;
            }
        }

        if (!produto.isEmpty() && controle == 0) {
            ItensCompra itensCompra = new ItensCompra();
            itensCompra.setProduto(produto.get());
            itensCompra.setValorUnitario(produto.get().getValorVenda());
            itensCompra.setQuantidade(itensCompra.getQuantidade() + 1);
            itensCompra.setValorTotal(itensCompra.getValorTotal() + itensCompra.getQuantidade() * itensCompra.getValorUnitario());

            listItensCompras.add(itensCompra);
        }

        return "redirect:/carrinho";
    }

    private void buscarUsuarioLogado() {
        Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
        if (!(autenticado instanceof AnonymousAuthenticationToken)) {
            String email = autenticado.getName();
            cliente = clienteRepository.buscarClienteEmail(email).get(0);
        }
    }

    @GetMapping("/finalizar")
    public ModelAndView finalizarCompra() {
        buscarUsuarioLogado();
        ModelAndView mv = new ModelAndView("cliente/finalizar");
        calcularTotal();
        mv.addObject("compra", compra);
        mv.addObject("listaItens", listItensCompras);
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PostMapping("/finalizar/confirmar")
    public ModelAndView confirmarCompra(String formaPagamento) {
        ModelAndView mv = new ModelAndView("cliente/mensagemFinalizou");
        compra.setCliente(cliente);
        compra.setFormaPagamento(formaPagamento);
        compraRepository.saveAndFlush(compra);

        for (ItensCompra c : listItensCompras) {
            c.setCompra(compra);
            itensCompraRepository.saveAndFlush(c);
        }
        listItensCompras = new ArrayList<>();
        compra = new Compra();
        return mv;
    }

    @GetMapping("/removerProduto/{id}")
    public String removerProdutoCarrinho(@PathVariable Long id) {

        for (ItensCompra it : listItensCompras) {
            if (it.getProduto().getId().equals(id)) {
                listItensCompras.remove(it);
                break;
            }
        }

        return "redirect:/carrinho";
    }
}
