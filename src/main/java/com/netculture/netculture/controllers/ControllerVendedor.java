package com.netculture.netculture.controllers;

import com.netculture.netculture.models.LoginDTO;
import com.netculture.netculture.models.Loja;
import com.netculture.netculture.models.Produto;
import com.netculture.netculture.models.Vendedor;
import com.netculture.netculture.repositories.RepositoryLoja;
import com.netculture.netculture.repositories.RepositoryProduto;
import com.netculture.netculture.repositories.RepositoryVendedor;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/vendedor")
public class ControllerVendedor {
    
    private final RepositoryLoja repositoryLoja;
    private final RepositoryVendedor repositoryVendedor; 
    private final RepositoryProduto repositoryProduto;   
    private final HttpSession session;
    
    public ControllerVendedor(RepositoryVendedor repositoryVendedor, HttpSession session, RepositoryLoja repositoryLoja, RepositoryProduto repositoryProduto) {
        this.repositoryVendedor = repositoryVendedor;
        this.session = session;
        this.repositoryLoja = repositoryLoja;
        this.repositoryProduto = repositoryProduto;
    }


      
    @GetMapping("/create/view")
    public String createView(Model m) {
        m.addAttribute("vendedor", new Vendedor());
        return "createVendedor";
    }
    

    @PostMapping("/create")
    public String createVendedor(Model m, Vendedor vendedor, String csenha) {
        if(vendedor==null ||
            repositoryVendedor.findByEmail(vendedor.getEmail())!=null){
                
                m.addAttribute("msg", "Erro: Alguns campos não estão preenchidos ou o e-mail já está cadastrado");
                return "createVendedor";
        }
        if(!vendedor.getSenha().equals(csenha)){
            m.addAttribute("msg", "Erro: As senhas não coincidem");
            return "createVendedor";
        }
        String senhaHash = new BCryptPasswordEncoder().encode(vendedor.getSenha());
        vendedor.setSenha(senhaHash);
        Vendedor vendedorCadastrado = repositoryVendedor.save(vendedor);

        if(!Vendedor.isNull(vendedorCadastrado)){
            m.addAttribute("msg", "Comprador cadastrado com sucesso!");
            return "loginVendedor";
        }
        m.addAttribute("msg", "Erro ao cadastrar comprador");
        return "createVendedor";
    }

    @PostMapping("/login")
    public String loginVendedor(Model m, LoginDTO data) {
        if(data.email() == null || data.senha() == null){
            m.addAttribute("msg", "Erro: Alguns campos não estão preenchidos");
            return "loginVendedor";
        }
        
        Vendedor vendedorLogin = repositoryVendedor.findByEmail(data.email());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(encoder.matches(data.senha(), vendedorLogin.getSenha())){
            m.addAttribute("msg", "Login efetuado com sucesso!");
            vendedorLogin.setSenha(null);
            session.setAttribute("vLogado", vendedorLogin);
            m.addAttribute("vLogado", vendedorLogin);
            List<Loja> lojas = repositoryLoja.findByVendedorId(vendedorLogin.getId());
            m.addAttribute("lojas", lojas);
            return "homeVendedor";
        }

        m.addAttribute("msg", "Erro ao efetuar login");
        return "loginVendedor";
    }
    
    @GetMapping("/home/view")
    public String perfilVendedor(Model m){
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if(v!=null){
            m.addAttribute("vLogado", v);
            m.addAttribute("lojas", repositoryLoja.findByVendedorId(v.getId()));
            return "homeVendedor";
        }
        return "loginVendedor";
    }

    @GetMapping("/perfil/view")
    public String homeVendedor(Model m){
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if(v!=null){
            m.addAttribute("vLogado", v);
            m.addAttribute("object", new Vendedor());
            return "perfil";
        }
        return "loginVendedor";
    }

    @GetMapping("/logout")
    public String logoutVendedor(Model m){
        session.invalidate();
        m.addAttribute("msg", "Logout efetuado com sucesso!");
        return "loginVendedor";
    }
    
    @PostMapping("/update")
    public String updateVendedor(Model m, Vendedor object, String csenha){
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if(v==null){
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        if(!object.getSenha().equals(csenha)){
            m.addAttribute("msg", "Erro: As senhas não coincidem");
            return "perfil";
        }
        v.setNome(object.getNome());
        v.setEmail(object.getEmail());
        v.setWhatsapp(object.getWhatsapp());
        v.setDescricao(object.getDescricao());
        String senhaHash = new BCryptPasswordEncoder().encode(object.getSenha());
        v.setSenha(senhaHash);
        Vendedor vendedorSalvo = repositoryVendedor.save(v);
        if(!Vendedor.isNull(vendedorSalvo)){
            vendedorSalvo.setSenha(null);
            m.addAttribute("vLogado", vendedorSalvo);
            m.addAttribute("msg", "Perfil atualizado com sucesso!");
            session.setAttribute("vLogado", vendedorSalvo);
            return "perfil";
        }
        m.addAttribute("msg", "Erro ao atualizar perfil");
        return "perfil";
    }

    @GetMapping("/loja/create/view")
    public String createLojaView(Model m) {
        m.addAttribute("loja", new Loja());
        return "criarLoja";
    }

    @PostMapping("/loja/create")
    public String createLoja(Model m, @ModelAttribute Loja loja) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        loja.setVendedor(v);
        // Salvar a loja no repositório (RepositoryLoja)
        repositoryLoja.save(loja);
        m.addAttribute("msg", "Loja cadastrada com sucesso!");
        List<Loja> lojas = repositoryLoja.findByVendedorId(v.getId());
        m.addAttribute("lojas", lojas);
        m.addAttribute("vLogado", v);
        return "homeVendedor";
    }

    @GetMapping("/lojas/view")
    public String cardLojas(Model m) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        List<Loja> lojas = repositoryLoja.findByVendedorId(v.getId());
        m.addAttribute("lojas", lojas);
        m.addAttribute("vLogado", v);
        return "homeVendedor";
    }

    @GetMapping("/loja/{id}/view")
    public String viewLoja(Model m, @PathVariable ObjectId id) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        Optional<Loja> lojaOpt = repositoryLoja.findById(id);
        if (lojaOpt.isPresent()) {
            Loja loja = lojaOpt.get();
            List<Produto> produtos = repositoryProduto.findByLojaId(loja.getId());
            List<Produto> produtosDisponiveis = repositoryProduto.findByVendedorId(v.getId());
            produtosDisponiveis.removeAll(produtos);
            m.addAttribute("loja", loja);
            m.addAttribute("produtos", produtos);
            m.addAttribute("produtosDisponiveis", produtosDisponiveis);
            m.addAttribute("vLogado", v);
            return "homeLoja";
        }
        m.addAttribute("msg", "Erro: Loja não encontrada");
        return "homeVendedor";
    }
    @GetMapping("/loja/{lojaId}/addProduto/{produtoId}")
    public String addProdutoToLoja(@PathVariable ObjectId lojaId, @PathVariable ObjectId produtoId, Model m) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        Optional<Produto> produtoOpt = repositoryProduto.findById(produtoId);
        Optional<Loja> lojaOpt = repositoryLoja.findById(lojaId);
        if (produtoOpt.isPresent() && lojaOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            Loja loja = lojaOpt.get();
            repositoryLoja.adicionarItem(loja.getId(), produto.getId());
            m.addAttribute("msg", "Produto adicionado à loja com sucesso!");
        } else {
            m.addAttribute("msg", "Erro: Produto ou loja não encontrado");
        }
        Optional<Loja> lojaOpt1 = repositoryLoja.findById(lojaId);
        if (lojaOpt1.isPresent()) {
            Loja loja = lojaOpt1.get();
            List<Produto> produtos = repositoryProduto.findByLojaId(loja.getId());
            List<Produto> produtosDisponiveis = repositoryProduto.findByVendedorId(v.getId());
            produtosDisponiveis.removeAll(produtos);
            m.addAttribute("loja", loja);
            m.addAttribute("produtos", produtos);
            m.addAttribute("produtosDisponiveis", produtosDisponiveis);
            m.addAttribute("vLogado", v);
            return "homeLoja";
        }
    } 
    @GetMapping("/loja/{lojaId}/removeProduto/{produtoId}")
    public String removeProdutoFromLoja(@PathVariable ObjectId lojaId, @PathVariable ObjectId produtoId, Model m) {
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if (v == null) {
            m.addAttribute("msg", "Erro: Vendedor não logado");
            return "loginVendedor";
        }
        Optional<Loja> lojaOpt = repositoryLoja.findById(lojaId);
        if (lojaOpt.isPresent()) {
            Loja loja = lojaOpt.get();
            loja.removerProduto(produtoId);
            repositoryLoja.save(loja);
            m.addAttribute("msg", "Produto removido da loja com sucesso!");
        } else {
            m.addAttribute("msg", "Erro: Loja não encontrada");
        }
        return "redirect:/vendedor/loja/" + lojaId + "/view";
    }

    
}
