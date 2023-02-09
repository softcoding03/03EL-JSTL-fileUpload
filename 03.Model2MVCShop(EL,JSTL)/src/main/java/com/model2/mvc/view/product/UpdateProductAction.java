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
		//getparameter�� ���������� UpdateProductView.jsp���� prodNo �Ǿ�;��Ѵ�.fnc���� ���� �� �־���Ѵ� ..?
		System.out.println("������!!���� ~~"+prodNo);
		System.out.println(request.getParameter("regdate"));
		
		Product product = new Product();
		//View �Է°� ������� VO ���� ==> request.getParameter ���
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		
		System.out.println("VO�� view���� �Է��Ѱ� ���� �Ϸ�");
		
		ProductService service = new ProductServiceImpl();
		
		service.updateProduct(product);//'View �Է°� ������� �������� VO'�� ������ Update���� ���� !
		
		request.setAttribute("upcom",service.getProduct(prodNo));//update�Ϸ��(�Ϸῡ ����) VO
		
		return "forward:/product/UpdateProduct.jsp"; 
	}
}