package com.example.demo.config.auth;

import com.example.demo.config.auth.provider.KakaoUserInfo;
import com.example.demo.config.auth.provider.NaverUserInfo;
import com.example.demo.config.auth.provider.OAuth2UserInfo;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.domain.entity.User;
import com.example.demo.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService   implements UserDetailsService {



    private PasswordEncoder passwordEncoder;

    public PrincipalDetailsOAuth2Service(){
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("[OAUTH2 Service] loadUser CALL");
        System.out.println("userRequest : "+ userRequest);
        System.out.println("userRequest.getClientRegistration() : "+ userRequest.getClientRegistration());
        System.out.println("userRequest.getAccessToken() : "+ userRequest.getAccessToken());
        System.out.println("userRequest.getAdditionalParameters() : "+ userRequest.getAdditionalParameters());

        System.out.println("getAccessToken().getTokenValue() : "+ userRequest.getAccessToken().getTokenValue());
        System.out.println("getAccessToken().getTokenType() : "+ userRequest.getAccessToken().getTokenType().getValue());
        System.out.println("getAccessToken().getScopes() : "+ userRequest.getAccessToken().getScopes());



        String profileimage = null;
        String name = null;
        String email = null;


//      System.out.println("super.loadUser : " +super.loadUser(userRequest));
        OAuth2User oauth2User =   super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        System.out.println("auth2User.getAttributes() : " +oauth2User.getAttributes());

        if(userRequest.getClientRegistration().getRegistrationId().equals("kakao"))
        {
            System.out.println("[] 카카오 로그인");


            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo((Map<String, Object>) oauth2User.getAttributes().get("properties"));
            KakaoUserInfo kakaoUserInfo1 = new KakaoUserInfo((Map<String, Object>) oauth2User.getAttributes().get("kakao_account"));


            kakaoUserInfo.setId(userRequest.getClientRegistration().getClientId());
            oAuth2UserInfo = kakaoUserInfo;


            System.out.println("oa : ~~!~!!~@!~!@~" + oAuth2UserInfo);                              //nickname(이름), 프로필사진


            profileimage = kakaoUserInfo.getAttributes().get("profile_image").toString();
            System.out.println("PROFILEIMAGE : " + profileimage);
            name = kakaoUserInfo.getAttributes().get("nickname").toString();
            System.out.println("name : " + name);
            email = kakaoUserInfo1.getAttributes().get("email").toString();
            System.out.println("email : " + email);
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver"))
        {
            System.out.println("[] 네이버 로그인");
            NaverUserInfo naverUserInfo = new NaverUserInfo((Map<String, Object>) oauth2User.getAttributes().get("response"));
            oAuth2UserInfo = naverUserInfo;
            //프로필ㅇ ㅣ미지
            profileimage = naverUserInfo.getAttributes().get("profile_image").toString();
            System.out.println("PROFILEIMAGE : " + profileimage);
            name = naverUserInfo.getAttributes().get("name").toString();
            System.out.println("name : " + name);
            email = naverUserInfo.getAttributes().get("email").toString();
            System.out.println("email : " + email);


        }

        //OAuth2UserInfo 확인
        String provider  = oAuth2UserInfo.getProvider();
//        String providerId = oAuth2UserInfo.getProviderId().substring(0,5);
        String password = passwordEncoder.encode("1234");
        String role = "ROLE_USER";

        //DB 저장
        Optional<User> optional =   userRepository.findById(email);
        if(optional.isEmpty()) {
            User user = User.builder()
                    .password(password)
                    .email(email)
                    .name(name)
                    .role(role)
                    .provider(provider)
//                    .providerId(providerId)
                    .profile(profileimage)
                    .build();
            userRepository.save(user);
            System.out.println("[OAUTH] "+provider +" 최초 로그인 요청!");
        }else{
            System.out.println("[OAUTH] 기존 계정"+optional.get().getEmail() +"으로 로그인!");
        }


        //AccessToken 정보를 Authentication에 저장하기
        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setAttributes(oauth2User.getAttributes());
        UserDto dto = new UserDto();
        dto.setPassword(password);
        dto.setEmail(email);
        dto.setRole(role);
        //OAUTH2 LOGOUT
        dto.setProvider(provider);
//        dto.setProviderId(providerId);
        dto.setProfile(profileimage);
        principalDetails.setUser(dto);
        principalDetails.setAccessToken(userRequest.getAccessToken().getTokenValue());

        return principalDetails;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findById(email);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        UserDto dto = new UserDto();
        dto.setPassword(user.get().getPassword());
        dto.setEmail(user.get().getEmail());
        dto.setRole(user.get().getRole());
        dto.setNickname(user.get().getNickname());
        dto.setName(user.get().getName());
        dto.setBirth(user.get().getBirth());
        dto.setZipcode(user.get().getZipcode());
        dto.setAddr1(user.get().getAddr1());
        dto.setAddr2(user.get().getAddr2());
        dto.setProfile(user.get().getProfile());

        PrincipalDetails principalDetails = new PrincipalDetails();
        principalDetails.setUser(dto);

        return principalDetails;
    }
}