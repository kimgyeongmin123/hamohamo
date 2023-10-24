package com.example.demo.config.auth.provider;

import lombok.Data;

import java.util.Map;

@Data
public class NaverUserInfo implements OAuth2UserInfo{


    private Map<String,Object> attributes;

    public NaverUserInfo(Map<String,Object> attributes){

        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {

        return (String)attributes.get("id");
    }

    @Override
    public String getProvider() {

        return "naver";
    }

    @Override
    public String getEmail() {

        return (String)attributes.get("email");
    }

    @Override
    public String getName() {

        return (String)attributes.get("name");
    }


}