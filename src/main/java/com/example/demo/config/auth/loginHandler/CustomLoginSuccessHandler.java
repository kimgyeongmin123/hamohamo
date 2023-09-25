package com.example.demo.config.auth.loginHandler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("CustomLoginSuccessHandler's onAuthenticationSuccess!!!!!!!!!!!!!! ");
		Collection<? extends GrantedAuthority> collection = authentication.getAuthorities();
		
		collection.forEach((role) -> {
			try {
				System.out.println("role : " + role.getAuthority());
				String role_str = role.getAuthority();
				if(role_str.equals("ROLE_USER"))
				{
					System.out.println("메인 페이지로 이동");
					response.sendRedirect(request.getContextPath()+"/list");
					return;
				}
				else if(role_str.equals("ROLE_MEMBER"))
				{
					System.out.println("MEMBER 페이지로 이동");
					response.sendRedirect(request.getContextPath()+"/member");
					return;
				}
				else if(role_str.equals("ROLE_ADMIN"))
				{
					System.out.println("ADMIN 페이지로 이동");
					response.sendRedirect(request.getContextPath()+"/admin");
					return;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		
	}

}
