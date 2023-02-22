package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.*;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class AddPurchaseViewAction extends Action {
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//addpurchaseView.jsp에서 상품번호와 일치하는 
		//상품의 정보들과
		//user의 정보를 보여준다.
		

		//쿼리스트링에서 prodNo 가져오기
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
			System.out.println("prodNo는 ?"+prodNo);
		
		//Session에 저장된 user 정보들 가져오기(userId기반으로 정보 저장해둠)
		HttpSession session=request.getSession();
		User user = (User)session.getAttribute("user");
		System.out.println(user.getUserId());
		
		//prodNo로 Product 세팅 값들 가져오기
		//ProductService service1 = new ProductServiceImpl();
		Product product = new ProductServiceImpl().getProduct(prodNo); //prodNo로 select해온 product 정보들 product로 리턴받음.
	
		
		request.setAttribute("product", product);
		request.setAttribute("user", user); //jsp에서 user정보들 가져올 수 있도록 set
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
}
