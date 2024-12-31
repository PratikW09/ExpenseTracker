package project.Personal.content_calender.repository;

import org.springframework.stereotype.Repository;

import project.Personal.content_calender.model.Content;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


@Repository
public class ContentCollectionRepository {
    
    private final List<Content> content = new ArrayList<>();

    public List<Content> findAll(){
        return content;
   }

   public Optional<Content> findById(Integer id){
    return content.stream().filter(c -> c.id().equals(id)).findFirst();
   }

   public void save(Content c){
    content.add(c);
   }
}
