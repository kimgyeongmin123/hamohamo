package com.example.demo.domain.service;

import com.example.demo.domain.entity.Follow;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    public boolean isFollowing(User follower, User following){
        return followRepository.findByFollowerAndFollowing(follower, following) != null;
    }

    public void follow(User follower, User following){
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }

    public void unfollow(User follower, User following){
        System.out.println("언팔할거임!?!??!?!?!");
        Follow follow = followRepository.findByFollowerAndFollowing(follower, following);
        System.out.println(follow);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    public List<User> getFollowList(String currentUser) {
        List<User> list = followRepository.findByFollowNickname(currentUser);
        System.out.println("list : " + list);

        return list;
    }

//    public List<String> getFollowListProfile(String currentUser) {
//        List<String> Profilelist = followRepository.findByFollowProfile(currentUser);
//        System.out.println("list : " + Profilelist);
//
//        return Profilelist;
//    }
}