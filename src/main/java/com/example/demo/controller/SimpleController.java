package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SimpleController {

//	@GetMapping("/list")
//	public void home(Authentication authentication) {
//		log.info("GET /list...");
//		System.out.println("authentication : " + authentication);
//		System.out.println("name : " + authentication.getName());
//		System.out.println("principal : " + authentication.getPrincipal());
//		System.out.println("authorities : " + authentication.getAuthorities());
//		System.out.println("detail : " + authentication.getDetails());
//		System.out.println("credential : " + authentication.getCredentials());
//
//	}


	@GetMapping("/member")
	public void member() {
		log.info("GET /member...");
	}

	@GetMapping("/admin")
	public void admin() {
		log.info("GET /admin...");
	}
	@GetMapping("/login")
	public void mylogin() {
		log.info("GET /login...");
	}

	@GetMapping("/error")
	public void error() {
		log.info("GET /error...");
	}

	@GetMapping("/findid")
	public void findid() {log.info("GET/findid...");}
	@GetMapping("/emailcheck")
	public void emailcheck() {log.info("GET/emailcheck...");}
	@GetMapping("/resetpw")
	public void resetpw() {log.info("GET/resetpw...");}
	@GetMapping("/checkNicknameDuplicate")
	public void checkNicknameDuplicate_get(){ log.info("GET/checkNicknameDuplicate");}
	@GetMapping("/checkDuplicate")
	public void checkDuplicate_get(){
		log.info("GET/checkDuplicate");
	}
	@GetMapping("/checkPhoneDuplicate")
	public void checkPhoneDuplicate_get(){
		log.info("GET/checkPhoneDuplicate");
	}
	@GetMapping("/sendemail")
	public void sendemail_get() {log.info("GET/sendemail"); }
	@GetMapping("/checkcode")
	public void checkCode_get() {log.info("GET/checkcode"); }
	@GetMapping("/button")
	public void button_get() {log.info("GET/button"); }

}