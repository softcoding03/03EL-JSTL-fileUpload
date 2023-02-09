package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;



public class AddProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Product product = new Product();
		
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