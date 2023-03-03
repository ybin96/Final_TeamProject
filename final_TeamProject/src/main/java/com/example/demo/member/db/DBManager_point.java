package com.example.demo.member.db;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.example.demo.member.vo.AccommoPhotoVO;
import com.example.demo.member.vo.AccommodationVO;
import com.example.demo.member.vo.EventVO;
import com.example.demo.member.vo.InquiryVO;
import com.example.demo.member.vo.LikeVO;
import com.example.demo.member.vo.MemberVO;
import com.example.demo.member.vo.ReplyVO;
import com.example.demo.member.vo.ReservationVO;
import com.example.demo.member.vo.ReviewVO;

public class DBManager_point {

public static SqlSessionFactory sqlSessionFactory;
	
	static {
		try {
			String resource = "com/example/demo/member/db/SqlMapConfig.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			
		}catch (Exception e) {
			System.out.println("예외:"+e.getMessage());
		}
	}
	
	public static int countReset() {
		int re = -1;
		SqlSession session = sqlSessionFactory.openSession(true);
		re = session.update("point.countReset");
		session.close();
		return re;
	}
	
	
	
}
