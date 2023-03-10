package com.example.demo.admin.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.admin.db.DBManager;
import com.example.demo.admin.vo.MemberVO;
import com.example.demo.admin.vo.ReservationVO;
import com.example.demo.admin.vo.ReviewVO;

@Repository
public class MemberDAO {
	
	// 관리자 회원 조회
	public List<MemberVO> findAll(HashMap<String, Object> map){
		return DBManager.listMember(map);
	}
	
	// 회원 총 레코드 수
	public int getTotalRecord(HashMap<String, Object> map2) {
		return DBManager.getTotalRecord(map2);
	}
	
	// 각 회원마다 리뷰 조회 
	public List<ReviewVO> memberReviewList(int memberNo){
		return DBManager.memberReviewList(memberNo);
	}
	
	// 각 회원마다 예약 조회 
	public List<ReservationVO> memberReservationList(int memberNo){
		return DBManager.memberReservationList(memberNo);
	}
	
	public MemberVO findByNo(int memberNo) {
		return DBManager.findByNo(memberNo);
	}
	
}


















