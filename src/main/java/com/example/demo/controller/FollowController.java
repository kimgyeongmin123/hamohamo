package com.example.demo.controller;

import com.example.demo.domain.entity.User;
import com.example.demo.domain.service.FollowService;
import com.example.demo.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @GetMapping("/list/follow/status")
    public String getFollowStatus(@RequestParam("followerId") String followerId, @RequestParam("followingId") String followingId) {

        User follower = userService.getUserByEmail(followerId); // userService에서 이메일을 기반으로 User 객체를 가져오는 메서드 사용
        User following = userService.getUserByEmail(followingId);

        if (followService.isFollowing(follower, following)) {
            return "following";
        } else {
            return "not_following";
        }
    }


    @PostMapping("/follow")
    public RedirectView follow(@RequestParam("followerId") String followerId, @RequestParam("followingId") String followingId, Model model) {

        User follower = userService.getUserByEmail(followerId); // userService에서 이메일을 기반으로 User 객체를 가져오는 메서드 사용
        User following = userService.getUserByEmail(followingId);

        System.out.println("follower : " + follower);
        System.out.println("following : "+ following);

        if (followService.isFollowing(follower, following)) {
            model.addAttribute("message", "Already following");
        } else {
            followService.follow(follower, following);
            model.addAttribute("message", "Followed successfully");
        }
        return new RedirectView("/list/search-nickname");
    }

    @PostMapping("/unfollow")
    public RedirectView unfollow(@RequestParam("followerId") String followerId, @RequestParam("followingId") String followingId, Model model) {

        System.out.println("언팔할거임!?!?!?!?!?!?!?");
        User follower = userService.getUserByEmail(followerId); // userService에서 이메일을 기반으로 User 객체를 가져오는 메서드 사용
        User following = userService.getUserByEmail(followingId);

        if (!followService.isFollowing(follower, following)) {
            model.addAttribute("message", "Not following");
        } else {
            followService.unfollow(follower, following);
            model.addAttribute("message", "Unfollowed successfully");
        }
        return new RedirectView("/list/search-nickname");
    }
}