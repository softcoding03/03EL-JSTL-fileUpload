package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class GetProductAction extends Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int productNo = Integer.parseInt(request.getParameter("prodNo")); 
		//listProductManage.jsp ===> <a href="/getProduct.do?prodNo=<%=vo.getProdNo()%>&menu=manage"><%= vo.getProdName() %></a>
		//jsp���� �������� ��ũ���� request.setParameter("prodNo","10000") �Ǿ��ִ� ��!!!!@@
		//������ request.getParameter�� ������ �� ���� ! 
		//but String return ���شٴ� �� ����� �� !
		System.out.println(productNo);
		
		ProductService service = new ProductServiceImpl();
		
		Product vo = service.getProduct(productNo); //ProductServiceImpl�� getProduct()
								//'vo'�� where = productNo �´� ���� 
		
		request.setAttribute("getVO", vo); 
		//'whwere= productNo�� �´� ������ �������� select �Ѱ�'�� vo�� ����ְ�  
		//"vo"��� request Scope�� ����Ͽ� �� vo��  ���� �־���.		
		//���߿� UpdateProductView���� �ʱⰪ �����ٶ����� getVO ����Ѵ�.

		
		return "forward:/product/getProduct.jsp";
	}
}