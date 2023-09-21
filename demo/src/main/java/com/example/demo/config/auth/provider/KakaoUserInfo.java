package com.example.demo.config.auth.provider;

import lombok.Data;

import java.util.Map;

@Data
public class KakaoUserInfo implements OAuth2UserInfo{

    private String id;

    private Map<String,Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }
    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }
    @Override
    public String getName() {
        return (String)attributes.get("nickname");
    }
}