package com.netculture.netculture.controllers;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netculture.netculture.models.Comprador;
import com.netculture.netculture.models.LoginDTO;
import com.netculture.netculture.repositories.RepositoryComprador;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/comprador")
public class ControllerComprador {
    @Autowired
    private  RepositoryComprador repositoryComprador;
    @Autowired
    private HttpSession session;

   
    
    @GetMapping("/create/view")
    public String createView(Model m) {
        m.addAttribute("comprador", new Comprador());
        return "createComprador";
    }
    

    @PostMapping("/create")
    public String createComprador(Model m, @ModelAttribute Comprador comprador) {
        if(comprador == null ||
            repositoryComprador.findByEmail(comprador.getEmail())!=null){
                
                m.addAttribute("msg", "Erro: Alguns campos não estão preenchidos ou o e-mail já está cadastrado");
                return "createComprador";
        }
        String senhaHash = new BCryptPasswordEncoder().encode(comprador.getSenha());
        comprador.setSenha(senhaHash);
        Comprador compradorCadastrado = repositoryComprador.save(comprador);

        if(!Comprador.isNull(compradorCadastrado)){
            m.addAttribute("msg", "Comprador cadastrado com sucesso!");
            return "loginComprador";
        }
        m.addAttribute("msg", "Erro ao cadastrar comprador");
        return "createComprador";
    }

    @PostMapping("/login")
    public String loginComprador(Model m, LoginDTO data) {
        if(data.email() == null || data.senha() == null){
            m.addAttribute("msg", "Erro: Alguns campos não estão preenchidos");
            return "loginComprador";
        }
        
        Comprador compradorLogin = repositoryComprador.findByEmail(data.email());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(data.senha(), compradorLogin.getSenha())){
            m.addAttribute("msg", "Login efetuado com sucesso!");
            compradorLogin.setSenha(null);
            session.setAttribute("cLogado", compradorLogin);
            m.addAttribute("cLogado", compradorLogin);
            return "homeComprador";
        }

        m.addAttribute("msg", "Erro ao efetuar login");
        return "loginComprador";
    }

    @GetMapping("/home/view")
    public String homeComprador(Model m) {
        Comprador c = (Comprador) session.getAttribute("cLogado");
        if(c == null){
            m.addAttribute("msg", "Erro: Comprador não logado");
            return "loginComprador";
        }
        m.addAttribute("cLogado", c);
        return "homeComprador";
    }


    @GetMapping("/perfil/view")
    public String perfilComprador(Model m) {
        Comprador c = (Comprador) session.getAttribute("cLogado");
        if(c == null){
            m.addAttribute("msg", "Erro: Comprador não logado");
            return "loginComprador";
        }
        m.addAttribute("cLogado", c);
        return "perfil";
    }
    
    @GetMapping("/logout")
    public String logoutComprador(Model m) {
        session.invalidate();
        m.addAttribute("msg", "Logout efetuado com sucesso!");
        return "loginComprador";
    }
    












    @GetMapping("/readall")
    public ResponseEntity<List<Comprador>> postMethodName() {
        List<Comprador> compradores = repositoryComprador.findAll();
        if(compradores != null){
            return ResponseEntity.ok(compradores);
        }
        
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Comprador> updateComprador(@PathVariable ObjectId id, @RequestBody Comprador compradorUpdate) {
        Optional<Comprador> compradorExistente = repositoryComprador.findById(id);
        if (compradorExistente.isPresent()) {
            Comprador c = compradorExistente.get();
            c.setNome(compradorUpdate.getNome());
            c.setEmail(compradorUpdate.getEmail());
            c.setWhatsapp(compradorUpdate.getWhatsapp());
            c.setSenha(compradorUpdate.getSenha());
            Comprador compradorSalvo = repositoryComprador.save(c); 
            if(!Comprador.isNull(compradorSalvo)){
                return ResponseEntity.ok(compradorSalvo);
            }
        }       
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteComprador(@PathVariable ObjectId id){
        Optional<Comprador> compradorExistente = repositoryComprador.findById(id);
        if(compradorExistente.isPresent()){
            repositoryComprador.deleteById(id);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }
}
