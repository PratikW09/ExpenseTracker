package project.Personal.content_calender.entity;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
// import java.util.Date;

@Getter
@Setter
@Document(collection = "posts")
public class PostEntity {

    @Id 
    private String id;
    private String title;
    private String content;
    private LocalDateTime date; // Added date field

   
}
