package com.example.demo.config.auth.loginHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		System.out.println("로로그그인인 실실패패 : " + exception);
		System.out.println("로로그그인인 실실패패 MSG : " + exception.getMessage());
		
		request.setAttribute("msg", exception.getMessage());
		
		response.sendRedirect(request.getContextPath() + "/login?error=" + exception.getMessage());
	}
}
