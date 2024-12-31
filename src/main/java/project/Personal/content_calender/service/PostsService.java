package project.Personal.content_calender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.Personal.content_calender.entity.PostEntity;
import project.Personal.content_calender.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    @Autowired
    private PostRepository postRepository;

    /**
     * Save a post to the database
     * @param postEntity the post entity to save
     */
    public void savePost(PostEntity postEntity) {
        postRepository.save(postEntity);
    }

    /**
     * Get all posts from the database
     * @return a list of all posts
     */
    public List<PostEntity> getAllPostsService() {
        return postRepository.findAll();
    }

    /**
     * Get a single post by its ID
     * @param id the ID of the post
     * @return the post entity if found, otherwise null
     */
    public PostEntity getPostById(String id) {
        Optional<PostEntity> post = postRepository.findById(id);
        return post.orElse(null);
    }

    /**
     * Update an existing post in the database
     * @param postEntity the post entity with updated details
     * @return the updated post entity
     */
    public PostEntity updatePost(PostEntity postEntity) {
        Optional<PostEntity> existingPost = postRepository.findById(postEntity.getId());
        if (existingPost.isPresent()) {
            return postRepository.save(postEntity); // Save updated post
        } else {
            throw new RuntimeException("Post with ID " + postEntity.getId() + " not found.");
        }
    }

    /**
     * Delete a post by its ID
     * @param id the ID of the post to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePostById(String id) {
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
