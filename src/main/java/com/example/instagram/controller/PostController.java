package com.example.instagram.controller;

import com.example.instagram.dao.UserRepository;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import com.example.instagram.service.PostService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class PostController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostService postService;

    @PostMapping(value = "/post")
    public ResponseEntity<String> savePost(@RequestBody String postReq){
        Post post = setPost(postReq);
        int postId = postService.savePost(post);

        return new ResponseEntity<String>(String.valueOf(postId), HttpStatus.CREATED);
    }


    @GetMapping(value = "/post")
    public ResponseEntity<String> getPost(@RequestParam String userId,@Nullable @RequestParam String postId){
        JSONArray postArr = postService.getPost(Integer.valueOf(userId),postId);
        return new ResponseEntity<String>(postArr.toString(), HttpStatus.OK);
    }

    @PutMapping(value = "/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable String postId, @RequestBody String postReq){
        Post post = setPost(postReq);
        postService.updatePost(postId,post);

        return new ResponseEntity<String>("post updated", HttpStatus.OK);
    }

    private Post setPost(String postReq) {
        JSONObject jsonObject = new JSONObject(postReq);

        User user = null;

        int userId = jsonObject.getInt("userId");

        if(userRepository.findById(userId).isPresent()){
            user = userRepository.findById(userId).get();
        }else {
            return null;
        }

        Post post = new Post();
        post.setUser(user);
        post.setPostData(jsonObject.getString("postData"));
        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        post.setCreateDate(createdTime);

        return post;
    }
}
