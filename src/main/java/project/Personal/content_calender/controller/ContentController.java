package project.Personal.content_calender.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import project.Personal.content_calender.repository.ContentCollectionRepository;
import project.Personal.content_calender.model.Content;

import java.util.List;
// import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    
    private final ContentCollectionRepository repository;

    public ContentController(ContentCollectionRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Content>findAll(){
        return repository.findAll();
    }


    @GetMapping("/{id}")
    public Content findById(@PathVariable Integer id){
        return repository.findById(id)
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Content Not Found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public void create(@Valid @RequestBody Content content){
        repository.save(content);
    }

}