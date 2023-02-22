package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

import com.model2.mvc.view.product.GetProductAction;


public class UpdateProductViewAction extends Action{

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		//url에 담아준 prodNo를 가져와서 사용한다. 
		System.out.println("성공 ~~"+prodNo);
		
		ProductService service = new ProductServiceImpl();
		
		Product product = service.getProduct(prodNo);
		
		//getProduct()를 사용한 이유 ? updateprodutView는, 'UpdateProduct'하는 버튼이 있는 페이지'이다.
		//UpdateProductView 페이지에서 이전에 조회한 상품의 정보들을 select해서 default값으로 보여줘야한다.
		//==> 조회페이지(getProduct)의 정보들을 request.setAttribute("getvo", vo);로 맵핑해두었다 !!  
		//==> request.getAttribute("getvo"); 로 사용할 수 있다 ! -> 위로 올라가보면...
		
		request.setAttribute("select", product);

		return "forward:/product/UpdateProductView.jsp";   
	}
}
