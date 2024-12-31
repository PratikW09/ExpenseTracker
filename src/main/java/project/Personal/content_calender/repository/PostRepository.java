package project.Personal.content_calender.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import project.Personal.content_calender.entity.PostEntity;

public interface PostRepository extends MongoRepository<PostEntity, String>{
    
}

