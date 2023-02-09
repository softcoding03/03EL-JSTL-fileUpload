package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.*;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class AddPurchaseViewAction extends Action {
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Purchase purchase = new Purchase();
		
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt((request.getParameter("price"))));  
		product.setFileName(request.getParameter("fileName"));

		System.out.println("AddProductAction의 세팅 값: "+product);
		
		ProductService service = new ProductServiceImpl();
		
		service.addProduct(product);
		
		System.out.println("AddProductActino완료 (상품등록 완료)");
		
		request.setAttribute("add", product);
				
		return "forward:/product/addProduct.jsp";
	}
}
