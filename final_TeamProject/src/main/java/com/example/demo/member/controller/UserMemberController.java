package com.example.demo.member.controller;

import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.member.dao.UserMemberDAO;
import com.example.demo.member.vo.MemberVO;

@Controller
public class UserMemberController {
	@Autowired
	private UserMemberDAO dao;

	public void setDao(UserMemberDAO dao) {
		this.dao = dao;
	}
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@GetMapping("/member/changePwd")
	public void chagePwdForm(){
		
	}
	
	@PostMapping("/member/changePwd")
	public ModelAndView chagePwdSubmit(String id, String pwd) {
		ModelAndView mav = new ModelAndView("redirect:/member/loginMember");
		String pwd_enc = passwordEncoder.encode(pwd);
		System.out.println("id="+id);
		System.out.println("pwd="+pwd);
		
		dao.chagePwd(id, pwd_enc);
		
		
		return mav;
	}
	
	
	@RequestMapping("/member/sendCode")
	@ResponseBody
	public String sendCode(String email) {
		String code  = "";
		Random r = new Random();
		code += r.nextInt(10);
		code += r.nextInt(10);
		code += r.nextInt(10);
		code += r.nextInt(10);
		
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("shjay0306@gmail.com");   //????????? gmail??? ??? ?????????.
		mailMessage.setTo(email);
		mailMessage.setSubject("???????????? ??????");
		mailMessage.setText(code);
		
		try {
			mailSender.send(mailMessage);
		}catch (Exception e) {
			System.out.println("????????????:"+e.getMessage());
		}
		
		
		return code;
	}
	
	@GetMapping("/member/findPwd")
	public void findPwdForm() {}
	
	@RequestMapping("/member/findPwdOK")
	@ResponseBody
	public String findPwdSubmit(String id){
		MemberVO m = null;
		m = dao.findById(id);
		return m.getEmail();
		
	}
	
	
	
	
	
	@GetMapping("/member/findId")
	public void findIdForm() {}
	
	@PostMapping("/member/findId")
	public ModelAndView findIdSubmit(String name, String email) {
		ModelAndView mav = new ModelAndView("/member/resultFindId");
		String id = dao.findId(name, email);
		System.out.println("?????????:"+id);
		mav.addObject("name", name);
		mav.addObject("id",id);
		
		return mav;
	}
	
	
	
	@GetMapping("/member/loginMember")
	public void loginForm(HttpServletRequest request, HttpSession session) {
		String referer = request.getHeader("Referer");
		if(!referer.equals("http://localhost:8088/member/loginMember")){
			if(referer.equals("http://localhost:8088/member/changePwd")||
				referer.equals("http://localhost:8088/member/changePwd?id="+session.getAttribute("id"))) {
				referer = "http://localhost:8088/";
			}
			
			session.setAttribute("preUrl", referer);
		}
	}
	
	@RequestMapping("/myPage/loginok")
	public String getLoginSession(HttpSession session) {
		System.out.println(1);
		//????????? ????????? ????????? ???????????? ????????? ?????? ??????????????? ??????????????? ??????
		Authentication authentication = 
							SecurityContextHolder.getContext().getAuthentication();
		//??? ?????? ????????? ????????? ????????? ?????? ????????? ?????????
		User user = (User) authentication.getPrincipal();
				
		String id = user.getUsername();
				
		//????????? ????????????
		session.setAttribute("id",id);
		MemberVO m = dao.findById(id);
		session.setAttribute("loginM", m);
		System.out.println("??????");
	    return "redirect:"+ session.getAttribute("preUrl");
	}
	
	@GetMapping("/member/insertMember")
	public void insertForm() {
	}
	
	@PostMapping("/member/insertMember")
	public ModelAndView insertSubmit(MemberVO m,int jumin_f, int jumin_b, String addr_f, String addr_b) {
		ModelAndView mav = new ModelAndView("redirect:/");
		m.setJumin(jumin_f+"-"+jumin_b);
		m.setAddr(addr_f+"/"+addr_b);
		String pwd = passwordEncoder.encode(m.getPwd());
		m.setPwd(pwd);

		int re = dao.insertMember(m);
		System.out.println(re);
		
		return mav;
	}
	
	@RequestMapping("/member/dup_chk")
	@ResponseBody
	public int dub_chk(HttpServletRequest request) {
		int re = 0;
		String id = request.getParameter("id");
		re = dao.chk_id(id);
		return re;
	}
	
	
}
