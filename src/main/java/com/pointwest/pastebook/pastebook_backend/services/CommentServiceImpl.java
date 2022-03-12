package com.pointwest.pastebook.pastebook_backend.services;

import com.pointwest.pastebook.pastebook_backend.config.JwtToken;
import com.pointwest.pastebook.pastebook_backend.models.Comment;
import com.pointwest.pastebook.pastebook_backend.models.LikedPost;
import com.pointwest.pastebook.pastebook_backend.models.Post;
import com.pointwest.pastebook.pastebook_backend.models.User;
import com.pointwest.pastebook.pastebook_backend.repositories.CommentRepository;
import com.pointwest.pastebook.pastebook_backend.repositories.LikedPostRepository;
import com.pointwest.pastebook.pastebook_backend.repositories.PostRepository;
import com.pointwest.pastebook.pastebook_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtToken jwtToken;

    @Override
    public ResponseEntity commentPost(Long postId, String content, String token) {
        Optional<Post> postToComment = postRepository.findById(postId);
        if(postToComment.isPresent()){
            Comment comment = new Comment();
            comment.setPost(postToComment.get());
            Post post = postToComment.get();
            Long authenticatedId = Long.parseLong(jwtToken.getIdFromToken(token));
            User user = userRepository.findById(authenticatedId).get();
            comment.setUser(user);
            comment.setContent(content);
            //comment.setDatetimeCreated();

            post.getComments().add(comment);
            postRepository.save(post);
            
            commentRepository.save(comment);
            return new ResponseEntity("Commented on post", HttpStatus.OK);
        }else{
            return new ResponseEntity("Post not found!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity unlikePost(Long postId, String token) {
        return null;
    }
}
