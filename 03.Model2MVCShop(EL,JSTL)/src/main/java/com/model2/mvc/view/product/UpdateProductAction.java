package com.model2.mvc.view.product;

import java.text.SimpleDateFormat;
import java.util.Date;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;

import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		//getparameter로 가져오려면 UpdateProductView.jsp에서 prodNo 실어와야한다.fnc에도 실을 수 있어야한다 ..?
		System.out.println("마지막!!성공 ~~"+prodNo);
		System.out.println(request.getParameter("regdate"));
		
		Product product = new Product();
		//View 입력값 기반으로 VO 세팅 ==> request.getParameter 사용
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		
		System.out.println("VO에 view에서 입력한거 세팅 완료");
		
		ProductService service = new ProductServiceImpl();
		
		service.updateProduct(product);//'View 입력값 기반으로 세팅해준 VO'를 가지고 Update쿼리 날림 !
		
		request.setAttribute("upcom",service.getProduct(prodNo));//update완료된(완료에 사용된) VO
		
		return "forward:/product/UpdateProduct.jsp"; 
	}
}