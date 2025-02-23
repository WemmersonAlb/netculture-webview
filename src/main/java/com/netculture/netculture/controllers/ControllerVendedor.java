package com.netculture.netculture.controllers;

import com.netculture.netculture.models.LoginDTO;
import com.netculture.netculture.models.Vendedor;
import com.netculture.netculture.repositories.RepositoryVendedor;
import com.netculture.netculture.repositories.RepositoryLoja;
import com.netculture.netculture.models.Loja;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/vendedor")
public class ControllerVendedor {
    @Autowired
    private RepositoryVendedor repositoryVendedor;
    @Autowired
    private HttpSession session;  
    @Autowired
    private RepositoryLoja repositoryLoja;


      
    @GetMapping("/create/view")
    public String createView(Model m) {
        m.addAttribute("vendedor", new Vendedor());
        return "createVendedor";
    }
    

    @PostMapping("/create")
    public String createVendedor(Model m, Vendedor vendedor) {
        if(vendedor==null ||
            repositoryVendedor.findByEmail(vendedor.getEmail())!=null){
                
                m.addAttribute("msg", "Erro: Alguns campos não estão preenchidos ou o e-mail já está cadastrado");
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
            List<Loja> lojas = repositoryLoja.findByVendedorId(v.getId());
            m.addAttribute("lojas", lojas);
            return "homeVendedor";
        }
        return "loginVendedor";
    }

    @GetMapping("/perfil/view")
    public String homeVendedor(Model m){
        Vendedor v = (Vendedor) session.getAttribute("vLogado");
        if(v!=null){
            m.addAttribute("vLogado", v);
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
    
    
// ############## Codígo adicionado por mim Anderson Guilherme ###############

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
    return "homeVendedor";
}

//###############################################################################








    @PutMapping("update/{id}")
    public ResponseEntity<Vendedor> updateVendedor(@PathVariable ObjectId id, @RequestBody Vendedor vendedorUpdate) {
        Optional<Vendedor> vendedorExistente = repositoryVendedor.findById(id);
        if (vendedorExistente.isPresent()) {
            Vendedor v = vendedorExistente.get();
            v.setNome(vendedorUpdate.getNome());
            v.setEmail(vendedorUpdate.getEmail());
            v.setWhatsapp(vendedorUpdate.getWhatsapp());
            v.setSenha(vendedorUpdate.getSenha());
            v.setDescricao(vendedorUpdate.getDescricao());
            Vendedor vendedorSalvo = repositoryVendedor.save(v); 
            if(!Vendedor.isNull(vendedorSalvo)){
                return ResponseEntity.ok(vendedorSalvo);
            }
        }       
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteVendedor(@PathVariable ObjectId id){
        Optional<Vendedor> vendedorExistente = repositoryVendedor.findById(id);
        if(vendedorExistente.isPresent()){
            repositoryVendedor.deleteById(id);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }
    
    
}
