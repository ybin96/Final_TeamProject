package com.example.demo.attraction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.attraction.dao.AttractionDAO;
import com.example.demo.attraction.vo.AttractionInfoVO;
import com.example.demo.attraction.vo.AttractionPhotoVO;
import com.example.demo.attraction.vo.AttractionVO;
import com.example.demo.attraction.vo.InfoListVO;
import com.example.demo.attraction.vo.LikeVO;

import lombok.Setter;

@Controller
@RequestMapping("/attract")
@Setter
public class AttractController {
	
	public int totPage = 1;		// 페이징 처리 위한 변수
	public int totCnt = 0;		// 페이징 처리 위한 변수
	public int pageSize = 6;	// 페이징 처리 위한 변수(검색 시 한페이지당 보여줄 결과 갯수)
	public int pageGroup = 10;	// 페이징 처리 위한 변수(페이징 번호 그룹 갯수)
	
	@Autowired
	private AttractionDAO dao;

	@GetMapping("/main")
	public String attractMain(Model model, HttpSession session) {
		session.removeAttribute("keyword");		// 페이징 처리 세션 제거
		session.removeAttribute("category");	// 페이징 처리 세션 제거
		
		// 인기숙소
		List<LikeVO> like_list = dao.findMostLike(5);	// Top 5 나열
		LikeVO l = new LikeVO();
		List<AttractionVO> attract_list = new ArrayList<>();
		AttractionVO a = new AttractionVO();
		for(int i=0;i<like_list.size();i++){
			l = like_list.get(i);
			int refNo = l.getRefNo();
			a = dao.findById(refNo);
			
			List<AttractionVO> photo_list = dao.findAllPhotoById(refNo);
			String realPath = "";
			String category = "";
			String name = "";
			String path = "";
			if(photo_list.size() > 0) {
				for(int j=0;j<photo_list.size();j++) {
					// 대표 이미지
					AttractionVO forPhoto = new AttractionVO();
					forPhoto = photo_list.get(0);
					category = a.getCategory();
					name = forPhoto.getName();
					path = forPhoto.getPath();
					realPath = "photo/Attraction/"+name+"/"+path;
					a.setRealPath(realPath);
				}
			}else {
				realPath = "photo/Attraction/노리매/att1.jpeg";
				a.setRealPath(realPath);
			}
			attract_list.add(a);
		}
		// 관리자 여부 확인
//		int role = 1;
//		session.setAttribute("role", role);
		model.addAttribute("Attract_list", attract_list);
		return "Attraction/Main";
	}
	
	// 키워드 버튼 누를때
	@GetMapping("/searchC")
	public ModelAndView searchC(String keyword, int pageNum, HttpSession session) {
//		ModelAndView mav = new ModelAndView("Attraction/Search");
		ModelAndView mav = new ModelAndView("Attraction/Search");	// 사이드바 테스트 버전
//		System.out.println("keyword:"+keyword);
//		System.out.println("category:"+category);
		
		if(session.getAttribute("keyword") != null) {
			keyword = (String) session.getAttribute("keyword");
		}
		
		int start = 1;
		int end = 1;
		if(pageNum == 1) {
			start = 1;
			end = start + pageSize - 1;
		}else {
			start = (pageNum-1)*pageSize+1;
			end = start + pageSize - 1;
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("keyword", keyword);
		map.put("start", start);
		map.put("end", end);
		List<AttractionVO> list = dao.findByAny(map);
		
		for(int i =0;i<list.size();i++) {
			int refNo = list.get(i).getAttractNo();
			List<AttractionVO> photo_list = dao.findAllPhotoById(refNo);
			String arealPath = "";
			String acategory = list.get(i).getCategory();
			String aname = list.get(i).getName();
			String apath = "";
			System.out.println(photo_list.size());
			System.out.println("===================");
			
			if(photo_list.size() > 0) {
				for(int j=0;j<photo_list.size();j++) {
					AttractionVO forPhoto = new AttractionVO();
					forPhoto = photo_list.get(0);
					apath = forPhoto.getPath();
					arealPath = "photo/Attraction/"+acategory+"/"+aname+"/"+apath;
					list.get(i).setRealPath(arealPath);
				}
			}else {
				System.out.println("=====else===");
				Random rand = new Random();
				String parklList[] = {"노리매","동백포레스트","휴애리"};
				String museumList[] = {"양금석 가옥","의귀리 김만일묘역"};
				String forestList[] = {"마흐니 숲길","큰엉해안경승지"};
				String riseList[] = {"물영아리 오름","사라오름"};
				String themeParkList[] = {"코코몽 에코파크"};
					switch (acategory) {
						case "공원":{
							for(int j=0;j<5;j++) {
								arealPath = "photo/Attraction/"+acategory+"/"+parklList[rand.nextInt(3)]+"/att"+(j+1)+".jpg";
								list.get(i).setRealPath(arealPath);
								System.out.println("사진Test");
							}
						}break;
						case "박물관":{
							for(int j=0;j<5;j++) {
							arealPath = "photo/Attraction/"+acategory+"/"+museumList[rand.nextInt(2)]+"/att"+(j+1)+".jpg";
							list.get(i).setRealPath(arealPath);
							}
						}break;
						case "숲":{
							for(int j=0;j<5;j++) {
							arealPath = "photo/Attraction/"+acategory+"/"+forestList[rand.nextInt(2)]+"/att"+(j+1)+".jpg";
							list.get(i).setRealPath(arealPath);
							}
						}break;
						case "오름":{
							for(int j=0;j<5;j++) {
							arealPath = "photo/Attraction/"+acategory+"/"+riseList[rand.nextInt(2)]+"/att"+(j+1)+".jpg";
							list.get(i).setRealPath(arealPath);
							}
						}break;
						case "테마파크":{
							for(int j=0;j<5;j++) {
							arealPath = "photo/Attraction/"+acategory+"/"+themeParkList[rand.nextInt(1)]+"/att"+(j+1)+".jpg";
							list.get(i).setRealPath(arealPath);
							}
						}break;
					}
			}
			
	}
		
		totCnt = dao.findCountByAny(keyword);
		totPage = (int) Math.ceil(totCnt/pageSize);
		int startPage = (pageNum-1)/pageGroup*pageGroup+1;
		int endPage = startPage+pageGroup-1;
		if(totPage < endPage) {
			endPage = totPage;
		}
		session.setAttribute("keyword", keyword);
		mav.addObject("totPage", totPage);
		mav.addObject("startPage", startPage);
		mav.addObject("endPage", endPage);
		mav.addObject("list",list);
		return mav;
	}
	
	// 키워드 검색
		@GetMapping("/main/search")
		public ModelAndView search(String keyword, String category, int pageNum, HttpSession session) {
			ModelAndView mav = new ModelAndView("Attraction/Search");
//			System.out.println("keyword:"+keyword);
//			System.out.println("category:"+category);
			
			if(session.getAttribute("keyword") != null) {
				keyword = (String) session.getAttribute("keyword");
			}
			if(session.getAttribute("category") != null) {
				category = (String) session.getAttribute("category");
			}
			
			int start = 1;
			int end = 1;
			if(pageNum == 1) {
				start = 1;
				end = start + pageSize - 1;
			}else {
				start = (pageNum-1)*pageSize+1;
				end = start + pageSize - 1;
			}
			HashMap<String, Object> map = new HashMap<>();
			map.put("keyword", keyword);
			map.put("start", start);
			map.put("end", end);
			List<AttractionVO> list = dao.findByAny(map);
			
			for(int i =0;i<list.size();i++) {
				int refNo = list.get(i).getAttractNo();
				List<AttractionVO> photo_list = dao.findAllPhotoById(refNo);
				String arealPath = "";
				String acategory = list.get(i).getCategory();
				String aname = list.get(i).getName();
				String apath = "";
				
				if(photo_list.size() > 0) {
					for(int j=0;j<photo_list.size();j++) {
						AttractionVO forPhoto = new AttractionVO();
						forPhoto = photo_list.get(0);
						apath = forPhoto.getPath();
						arealPath = "photo/Attraction/"+acategory+"/"+aname+"/"+apath;
						list.get(i).setRealPath(arealPath);
					}
				}else {
					Random rand = new Random();
					String parklList[] = {"노리매","동백포레스트","휴애리"};
					String museumList[] = {"양금석 가옥","의귀리 김만일묘역"};
					String forestList[] = {"마흐니 숲길","큰엉해안경승지"};
					String riseList[] = {"물영아리 오름","사라오름"};
					String themeParkList[] = {"코코몽 에코파크"};
						switch (acategory) {
							case "공원":{
								for(int j=0;j<5;j++) {
									arealPath = "photo/Attraction/"+acategory+"/"+parklList[rand.nextInt(3)]+"/att"+(j+1)+".jpg";
									list.get(i).setRealPath(arealPath);
								}
							}break;
							case "박물관":{
								for(int j=0;j<5;j++) {
								arealPath = "photo/Attraction/"+acategory+"/"+museumList[rand.nextInt(2)]+"/att"+(j+1)+".jpg";
								list.get(i).setRealPath(arealPath);
								}
							}break;
							case "숲":{
								for(int j=0;j<5;j++) {
								arealPath = "photo/Attraction/"+acategory+"/"+forestList[rand.nextInt(2)]+"/att"+(j+1)+".jpg";
								list.get(i).setRealPath(arealPath);
								}
							}break;
							case "오름":{
								for(int j=0;j<5;j++) {
								arealPath = "photo/Attraction/"+acategory+"/"+riseList[rand.nextInt(2)]+"/att"+(j+1)+".jpg";
								list.get(i).setRealPath(arealPath);
								System.out.println(arealPath);
								}
							}break;
							case "테마파크":{
								for(int j=0;j<5;j++) {
								arealPath = "photo/Attraction/"+acategory+"/"+themeParkList[rand.nextInt(1)]+"/att"+(j+1)+".jpg";
								list.get(i).setRealPath(arealPath);
								}
							}break;
						}
				}
				
			}
			
			
			totCnt = dao.findCountByAny(keyword);
			totPage = (int) Math.ceil(totCnt/pageSize);
			int startPage = (pageNum-1)/pageGroup*pageGroup+1;
			int endPage = startPage+pageGroup-1;
			if(totPage < endPage) {
				endPage = totPage;
			}
			session.setAttribute("keyword", keyword);
			session.setAttribute("category", category);
			mav.addObject("totPage", totPage);
			mav.addObject("startPage", startPage);
			mav.addObject("endPage", endPage);
			mav.addObject("list",list);
			return mav;
		}
	
	// 사진 보유 여부 검색
	// 없으면 랜덤이미지 사용
	@GetMapping("/cnt")
	public String cntP() {
		AttractionPhotoVO a = new AttractionPhotoVO();
		a.setAttractNo(999);
		int re = dao.findPCnt(a);
		System.out.println(re);
		return "Attraction/Main";
	}
	
	// 사진 리스트 검색
	@GetMapping("/findById")
	public String findById(Model model) {
		List<AttractionVO> list = dao.findAllPhotoById(148);
		AttractionVO a = new AttractionVO();
		String realPath = "";
		String category = "";
		String name = "";
		String path = "";
		if(list.size() > 0) {
//			System.out.println(list);
			for(int i=0;i<list.size();i++) {
				//대표 이미지
				a = list.get(0);
				category = a.getCategory();
				name = a.getName();
				path = a.getPath();
				realPath = "photo/Attraction/"+category+"/"+name+"/"+path;
//				System.out.println(realPath);
//				System.out.println("next");
			}
		}else {
			System.out.println("이미지 없음");
		}
//		System.out.println(realPath);
		model.addAttribute("realPath",realPath);
		return "Attraction/Main";
	}
	
	// 찜 많은 관광지 찾기
	@GetMapping("/findMostLike")
	public String findMostLike() {
		List<LikeVO> list = dao.findMostLike(5);
		LikeVO l = new LikeVO();
		for(int i=0;i<list.size();i++){
			l = list.get(i);
			int refNo = l.getRefNo();
			System.out.println(i+": "+refNo);
		}
		return "Attraction/Main";
	}
	
	// 관광지 상세페이지
		@GetMapping("/detail")
		public ModelAndView detail(int attractNo) {
			ModelAndView mav = new ModelAndView("Attraction/Detail");
			
			// 로그인한 멤버
//			MemberVO m = mdao.findByNo(7);
//			System.out.println(m);
			List<InfoListVO> infoList = new ArrayList<>();
			AttractionVO a = dao.findById(attractNo);
			List<AttractionInfoVO> atin = dao.findInfoById(attractNo);
//			atin.get(0).getOrders();
			
				InfoListVO invo = new InfoListVO();
				System.out.println("atin : "+atin);
				invo.setIntroduction(atin.get(0).getInfo().toString());
				invo.setCloseddays(atin.get(1).getInfo().toString());
				invo.setOperatingtime(atin.get(2).getInfo().toString());
				invo.setRateinformation(atin.get(3).getInfo().toString());
				invo.setMainpurpose(atin.get(4).getInfo().toString());
				invo.setParkingamount(atin.get(5).getInfo().toString());
				invo.setFacilities(atin.get(6).getInfo().toString());
				System.out.println(invo);
	
			List<AttractionVO> list = dao.findAllPhotoById(attractNo);
			List<String> photoList = new ArrayList<>();
			String realPath = "";
			String category = a.getCategory();
			String name = "";
			String path = "";
			
			if(list.size() > 0) {
				for(int i=0;i<list.size();i++) {
					a = list.get(i);
					name = a.getName();
					path = a.getPath();
					realPath = "photo/Attraction/"+category+"/"+name+"/"+path;
					photoList.add(realPath);
				}
			}else {
				// 이미지 없을때 랜덤이미지
				Random rand = new Random();
				String parklList[] = {"노리매","동백포레스트","휴애리"};
				String museumList[] = {"양금석 가옥","의귀리 김만일묘역"};
				String forestList[] = {"마흐니 숲길","큰엉해안경승지"};
				String riseList[] = {"물영아리 오름","사라오름"};
				String themeParkList[] = {"코코몽 에코파크"};
					switch (category) {
						case "숲":{						
							String k = forestList[rand.nextInt(3)];
							for(int i=0;i<5;i++) {
								realPath = "photo/Attraction/"+category+"/"+k+"/"+"att"+(i+1)+".jpg";
								photoList.add(realPath);
							}
						}break;
						case "오름":{
							String k = riseList[rand.nextInt(2)];
							for(int i=0;i<5;i++) {
								realPath = "photo/Attraction/"+category+"/"+k+"/"+"att"+(i+1)+".jpg";
							photoList.add(realPath);
							}
						}break;
						case "테마파크":{
							String k = themeParkList[rand.nextInt(2)];
							for(int i=0;i<5;i++) {
								realPath = "photo/Attraction/"+category+"/"+k+"/"+"att"+(i+1)+".jpg";
							photoList.add(realPath);
							}
						}break;
						case "공원":{
							String k = parklList[rand.nextInt(2)];
							for(int i=0;i<5;i++) {
								realPath = "photo/Attraction/"+category+"/"+k+"/"+"att"+(i+1)+".jpg";
							photoList.add(realPath);
							}
						}break;
						case "박물관":{
							String k = museumList[rand.nextInt(1)];
							for(int i=0;i<5;i++) {
								realPath = "photo/Attraction/"+category+"/"+k+"/"+"att"+(i+1)+".jpg";
							photoList.add(realPath);
							}
						}break;
					}
				
//				System.out.println("대체 이미지: "+photoList);
			}
//			System.out.println(photoList);
			mav.addObject("infoList", invo);
			mav.addObject("a", a);
			mav.addObject("photoList", photoList);
			return mav;
		}
	
	
}
