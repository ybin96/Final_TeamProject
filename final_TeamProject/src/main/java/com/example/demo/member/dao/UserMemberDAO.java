package com.example.demo.member.dao;

import org.springframework.stereotype.Repository;

import com.example.demo.member.db.DBManager_member;
import com.example.demo.member.vo.MemberVO;

@Repository
public class UserMemberDAO {
	public int insertMember(MemberVO m) {
		return DBManager_member.insertMember(m);
	}
	
	public int chk_id(String id) {
		return DBManager_member.chk_id(id);
	}
	
	public MemberVO findById(String id) {
		return DBManager_member.findById(id);
	}
	
	public String findId(String name, String email) {
		return DBManager_member.findId(name, email);
	}
	
	public int chagePwd(String id, String pwd) {
		return DBManager_member.chagePwd(id, pwd);
	}
	
	

}
