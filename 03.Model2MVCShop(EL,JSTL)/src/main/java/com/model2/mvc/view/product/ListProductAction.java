package com.model2.mvc.view.product;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class ListProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Search search = new Search();   
		//검색 후 결과 화면 세팅 !
		int currentPage=1; //첫 페이지
		
		if(request.getParameter("currentPage") != null && !request.getParameter("currentPage").equals("")) {//listUset.do?page=1의 page 번호를 가져온다.
			currentPage=Integer.parseInt(request.getParameter("currentPage")); //page의 번호를 int 화 //getparameter는 string이다 !
		}
		search.setCurrentPage(currentPage);   	//page 번호 세팅
		search.setSearchCondition(request.getParameter("searchCondition")); //상품번호, 상품명, 가격
		search.setSearchKeyword(request.getParameter("searchKeyword")); //직접 입력하는 란

		// web.xml  meta-data 로 부터 상수 추출 
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));//3,한페이지의 row
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));//5,선택 가능한 페이지쪽 수 
		search.setPageSize(pageSize);//SerchVO 세팅
		
		// Business logic 수행
		ProductService service=new ProductServiceImpl();
		Map<String,Object> map = service.getProductList(search); //ProductServiceImpl의 getProductList 실행
		//map이 갖고 있는 것 = `totalCount`와 `select해온 정보들은 담은 vo들을 list 배열에 넣은 list`

		Page resultPage = new Page (currentPage,((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		//현재 페이지 정보들을 가지고 Page bean을 세팅한다 ...
		
		System.out.println("ListUserAction ::"+resultPage);
		
		request.setAttribute("list", map.get("list")); // `select해온 정보들은 담은 vo들을 list 배열에 넣은 list`
		request.setAttribute("resultPage", resultPage); //현재 페이지 정보
		request.setAttribute("search", search); // page 검색 조건들을 저장한 search
		
		char c = request.getParameter("menu").charAt(0);
		
		if(c == 'm') {
			return "forward:/product/listProductManage.jsp";
		}
		return "forward:/product/listProductSearch.jsp";
		
	}
}