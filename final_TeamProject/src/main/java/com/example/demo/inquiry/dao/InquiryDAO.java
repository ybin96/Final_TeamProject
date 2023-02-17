package com.example.demo.inquiry.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.admin.db.DBManager;
import com.example.demo.admin.vo.MemberVO;

@Repository
public class InquiryDAO {
	
	// 관리자 회원 조회
	public List<MemberVO> findAll(HashMap<String, Object> map){
		return DBManager.listMember(map);
	}
	
	// 회원 총 레코드 수
	public int getTotalRecord(HashMap<String, Object> map2) {
		return DBManager.getTotalRecord(map2);
	}
}


















