package com.netculture.netculture.controllers;

import com.netculture.netculture.models.LoginDTO;
import com.netculture.netculture.models.Vendedor;
import com.netculture.netculture.repositories.RepositoryVendedor;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
