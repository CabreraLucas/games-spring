package application.controller;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.model.Jogo;
import application.model.Plataforma;
import application.repository.CategoriaRepository;
import application.repository.JogoRepository;
import application.repository.PlataformaRepository;

@Controller
@RequestMapping("/jogo")
public class CategoriaController {
    @Autowired
    private JogoRepository jogoRepo;
    @Autowired
    private CategoriaRepository categoriaRepo;
    @Autowired
    private PlataformaRepository plataformaRepo;

    @RequestMapping("/list")
    public String list(Model ui){
        ui.addAttribute("jogos", jogoRepo.findAll());
        return "jogo/list";
    }

    @RequestMapping("/insert")
    public String insert(Model ui){
        ui.addAttribute("categorias", categoriaRepo.findAll());
        ui.addAttribute("plataforma", plataformaRepo.findAll());
        return "jogo/insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(
        @RequestParam("titulo") String titulo,
        @RequestParam("categoria") long IdCategoria,
        @RequestParam("plataforma") long[] idsPplataformas) {

            Jogo jogo = new Jogo();
            jogo.setTitulo(titulo);
            jogo.setCategoria(categoriaRepo.findById(IdCategoria).get());
            for(long p : idsPplataformas){
                Optional<Plataforma> plataforma = plataformaRepo.findById(p);
                if(plataforma.isPresent()){
                    jogo.getPlataforma().add(plataforma.get());
                }
            }
            jogoRepo.save(jogo);
            return "redirect:/jogo/list";
    }

    @RequestMapping("/update")
    public String update(
        @RequestParam("id") long id, 
        Model ui) {

        Optional<Jogo> jogo = jogoRepo.findById(id);

        if(jogo.isPresent()){
            ui.addAttribute("jogo", jogo.get());
            ui.addAttribute("categorias", categoriaRepo.findAll());
            ui.addAttribute("plataformas", plataformaRepo.findAll());
            return "jogo/update";
        }
        return "redirect:/jogo/list";
    }
    

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
        @RequestParam("id") long id, 
        @RequestParam("titulo") String titulo,
        @RequestParam("categoria") long IdCategoria,
        @RequestParam("plataformas") long[] idsPplataformas) {

        Optional<Jogo> jogo = jogoRepo.findById(id);

        if(jogo.isPresent()){
            jogo.get().setTitulo(titulo);
            jogo.get().setCategoria(categoriaRepo.findById(idCategoria).get());
            Set<Plataforma> updatePlataforma = new HashSet<>();
            for(long p : idsPplataformas){
                Optional<Plataforma> plataforma = plataformaRepo.findById(p);
                if(plataforma.isPresent()) {
                    updatePlataforma.add(plataforma.get());
                }
            }
            jogo.get().setPlataformas(updatePlataforma);
            jogoRepo.save(jogo.get());
        }
        return "redirect:/jogo/list";
    }

    @RequestMapping(value = "/delete")
    public String delete(
        @RequestParam("id") long id, 
        Model ui) {

        Optional<Jogo> jogo = jogoRepo.findById(id);

        if(jogo.isPresent()){
            ui.addAttribute("jogo", jogo.get());
            return "jogo/delete";
        }
        return "redirect:/jogo/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("id") long id){
        jogoRepo.deleteById(id);

        return "redirect:/jogo/list";
        
    }
    
}