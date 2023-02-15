package com.model2.mvc.view.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ListPurchaseAction extends Action {
	
public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session=request.getSession();
		User user = (User)session.getAttribute("user");
		
		System.out.println(user.getUserId());
		String userId = user.getUserId();
		Search search = new Search();
		
		int currentPage=1; //첫 페이지
		
		if(request.getParameter("currentPage") != null) {
			currentPage=Integer.parseInt(request.getParameter("currentPage")); //page의 번호를 int 화 //getparameter는 string이다 !
		}
		
		search.setCurrentPage(currentPage);   	//currentpage 번호 세팅

		// web.xml  meta-data 로 부터 상수 추출 
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));//3,한페이지의 row
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));//5,선택 가능한 페이지쪽 수 
		search.setPageSize(pageSize);//SerchVO 세팅
		
		// Business logic 수행
		PurchaseService service=new PurchaseServiceImpl();
		//인자로 userId도 받는 이유? user별로 구매이력이 다르므로 
		Map<String,Object> map = service.getPurchaseList(search, userId); //ProductServiceImpl의 getProductList 실행
		//map이 갖고 있는 것 = `totalCount`와 `select해온 정보들은 담은 vo들을 list 배열에 넣은 list`

		Page resultPage = new Page (currentPage,((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		//현재 페이지 정보들을 가지고 Page bean을 세팅한다 ...
		
		System.out.println("ListUserAction ::"+resultPage);
		System.out.println("나는야 list이다 !!!!"+map.get("list2"));
		
		request.setAttribute("list3", map.get("list2")); // `select해온 정보들은 담은 vo들을 list 배열에 넣은 list`
		request.setAttribute("resultPage", resultPage); //현재 페이지 정보
		request.setAttribute("search", search); // page 검색 조건들을 저장한 search
		
		return "forward:/purchase/listPurchase.jsp";
		
	}

}
