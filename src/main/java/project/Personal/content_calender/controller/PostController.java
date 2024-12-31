package project.Personal.content_calender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import project.Personal.content_calender.entity.PostEntity;
import project.Personal.content_calender.service.PostsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    // Logger for the PostController
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostsService postsService;

    /**
     * Create a new post
     * @param postContent the post details
     * @return ResponseEntity with the created post or an error message
     */
    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@RequestBody PostEntity postContent) {
        logger.info("Received request to create a post: {}", postContent);

        try {
            postContent.setDate(LocalDateTime.now());
            postsService.savePost(postContent);
            logger.info("Post successfully created: {}", postContent);
            return ResponseEntity.status(HttpStatus.CREATED).body(postContent);
        } catch (Exception e) {
            logger.error("Error occurred while creating a post: {}", postContent, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create post");
        }
    }

    /**
     * Get all posts
     * @return a list of all posts
     */
    @GetMapping("/get-all-posts")
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        logger.info("Fetching all posts...");
        try {
            List<PostEntity> posts = postsService.getAllPostsService();
            logger.info("Successfully retrieved {} posts", posts.size());
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("Error occurred while fetching posts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get a post by ID
     * @param id the ID of the post
     * @return the requested post or an error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable String id) {
        logger.info("Fetching post with ID: {}", id);

        try {
            PostEntity post = postsService.getPostById(id);
            if (post == null) {
                logger.warn("Post with ID {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            logger.info("Post with ID {} successfully retrieved", id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            logger.error("Error occurred while fetching post with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching post");
        }
    }

    /**
     * Update a post by ID
     * @param id the ID of the post
     * @param updatedPost the updated post details
     * @return ResponseEntity with the updated post or an error message
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody PostEntity updatedPost) {
        logger.info("Received request to update post with ID: {}", id);

        try {
            PostEntity existingPost = postsService.getPostById(id);
            if (existingPost == null) {
                logger.warn("Post with ID {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            updatedPost.setId(id);
            updatedPost.setDate(existingPost.getDate()); // Keep original creation date
            postsService.savePost(updatedPost);
            logger.info("Post with ID {} successfully updated: {}", id, updatedPost);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            logger.error("Error occurred while updating post with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update post");
        }
    }

    /**
     * Delete a post by ID
     * @param id the ID of the post to delete
     * @return ResponseEntity with a success or error message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        logger.info("Received request to delete post with ID: {}", id);

        try {
            boolean deleted = postsService.deletePostById(id);
            if (!deleted) {
                logger.warn("Post with ID {} not found for deletion", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            logger.info("Post with ID {} successfully deleted", id);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            logger.error("Error occurred while deleting post with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete post");
        }
    }

    /**
     * Health check for the service
     * @return a simple OK message
     */
    @GetMapping("/healthcheck")
    public String healthCheck() {
        logger.info("Health check endpoint accessed");
        return "OK..!";
    }
}
