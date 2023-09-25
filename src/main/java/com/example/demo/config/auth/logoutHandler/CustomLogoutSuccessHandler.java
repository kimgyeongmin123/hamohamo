package com.example.demo.config.auth.logoutHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.demo.config.auth.PrincipalDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{



	private String kakaoClientId = "206caf77477fe5ba91dafb10da8d95d2";
	private String LOGOUT_REDIRECT_URI ="http://localhost:8080/login";

	private String naverClientId ="myCONIrrsJHFIPcgl9OQ";

	private String naverClientSecret="ngJQs03WkY";

	private String googleAPIKEY = "141949035610-n9jkso1n0kebcgoj6luls6g206s1660u.apps.googleusercontent.com";

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		System.out.println("CustomLogoutSuccessHandler's onLogoutSuccess!");


		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		String provider = principalDetails.getUser().getProvider();
		if(StringUtils.contains(provider,"kakao"))
		{
			System.out.println("GET /th/kakao/logoutWithKakao");
			//URL
			String url = "https://kauth.kakao.com/oauth/logout?client_id="+kakaoClientId+"&logout_redirect_uri="+LOGOUT_REDIRECT_URI;
			response.sendRedirect(url);
			return ;
		}
		else if(StringUtils.contains(provider,"google"))
		{
			String url = "https://accounts.google.com/Logout";
			response.sendRedirect(url);
			return;

		}
		else if(StringUtils.contains(provider,"naver"))
		{
			String url = "http://nid.naver.com/nidlogin.logout";
			response.sendRedirect(url);
			return ;
		}



		response.sendRedirect("/login");
	}

}