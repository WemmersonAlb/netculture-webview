package com.netculture.netculture.controllers;

import com.netculture.netculture.models.Produto;
import com.netculture.netculture.models.Vendedor;
import com.netculture.netculture.repositories.RepositoryLoja;
import com.netculture.netculture.repositories.RepositoryProduto;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/produto")
public class ControllerProduto {
   
    private final RepositoryProduto repositoryProduto;
    
    private final HttpSession session;

    private final RepositoryLoja repositoryLoja;

    public ControllerProduto(RepositoryProduto repositoryProduto, HttpSession session, RepositoryLoja repositoryLoja) {
        this.repositoryProduto = repositoryProduto;
        this.session = session;
        this.repositoryLoja = repositoryLoja;
    }

    @GetMapping("/create/view")
    public String createProdutoView(Model m) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        m.addAttribute("produto", new Produto());
        return "createProduto";
    }

    @PostMapping("/create")
    public String createProduto(Model m, @ModelAttribute Produto produto) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }

        produto.setVendedorId(v.getId());
        repositoryProduto.save(produto);
        m.addAttribute("vLogado", v);
        m.addAttribute("msg", "Produto cadastrado com sucesso!");
        m.addAttribute("lojas",
                repositoryLoja.findByVendedorId(v.getId()));
        return "homeVendedor";
    }

    @GetMapping("/listar")
    public String listarProdutos(Model m) {
        List<Produto> produtos = repositoryProduto.findAll();
        m.addAttribute("produtos", produtos);
        return "listarProdutos";
    }

    @GetMapping("/meus-produtos")
    public String listarMeusProdutos(Model m) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");

        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }

        List<Produto> produtos = repositoryProduto.findByVendedorId(v.getId());
        m.addAttribute("produtos", produtos);
        return "listarProdutos";
    }

    @PostMapping("/delete/{id}")
    public String deletarProduto(@PathVariable ObjectId id, Model m) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        repositoryProduto.deleteById(id);
        m.addAttribute("msg", "Produto removido com sucesso!");
        m.addAttribute("vLogado", v);
        m.addAttribute("lojas",
                repositoryLoja.findByVendedorId(v.getId()));
        return "homeVendedor";
    }
}

