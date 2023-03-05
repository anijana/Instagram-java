package com.example.instagram.service;

import com.example.instagram.dao.PostRepository;
import com.example.instagram.model.Post;
import com.example.instagram.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public int savePost(Post post) {
        Post savePost = postRepository.save(post);
        return savePost.getPostId();
    }

    public JSONArray getPost(int userId, String postId) {

        JSONArray postArr = new JSONArray();

        if(null != postId && postRepository.findById(Integer.valueOf(postId)).isPresent()){
            Post post = postRepository.findById(Integer.valueOf(postId)).get();
            JSONObject postObj = setPostData(post);
            postArr.put(postObj);
        }else {
            List<Post> postList = postRepository.findAll();
            for (Post post: postList) {
                JSONObject postObj = setPostData(post);
                postArr.put(postObj);
            }
        }
        return postArr;
    }

    private JSONObject setPostData(Post post) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("postId",post.getPostId());
        jsonObject.put("postData",post.getPostData());

        User user = post.getUser();

        JSONObject userJsonObj = new JSONObject();
        userJsonObj.put("userId",user.getUserId());
        userJsonObj.put("firstName",user.getFirstName());
        userJsonObj.put("age",user.getAge());

        jsonObject.put("user",userJsonObj);

        return jsonObject;
    }

    public void updatePost(String postId, Post updatePost) {
        if(postRepository.findById(Integer.valueOf(postId)).isPresent()){
            Post olderPost = postRepository.findById(Integer.valueOf(postId)).get();
            updatePost.setPostId(olderPost.getPostId());
            updatePost.setUser(olderPost.getUser());
            updatePost.setCreateDate(olderPost.getCreateDate());
            Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
            updatePost.setUpdateDate(updatedDate);
            postRepository.save(updatePost);
        }
    }
}
