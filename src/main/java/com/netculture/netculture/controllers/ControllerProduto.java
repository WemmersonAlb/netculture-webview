package com.netculture.netculture.controllers;

import com.netculture.netculture.models.Produto;
import com.netculture.netculture.models.Vendedor;
import com.netculture.netculture.repositories.RepositoryProduto;
import jakarta.servlet.http.HttpSession;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/produto")
public class ControllerProduto {
    @Autowired
    private RepositoryProduto repositoryProduto;
    @Autowired
    private HttpSession session;

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
        m.addAttribute("msg", "Produto cadastrado com sucesso!");
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

    @DeleteMapping("/delete/{id}")
    public String deletarProduto(@PathVariable ObjectId id, Model m) {
        repositoryProduto.deleteById(id);
        m.addAttribute("msg", "Produto removido com sucesso!");
        return "homeVendedor";
    }
}

