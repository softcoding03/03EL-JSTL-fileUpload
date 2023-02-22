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
		//url�� ����� prodNo�� �����ͼ� ����Ѵ�. 
		System.out.println("���� ~~"+prodNo);
		
		ProductService service = new ProductServiceImpl();
		
		Product product = service.getProduct(prodNo);
		
		//getProduct()�� ����� ���� ? updateprodutView��, 'UpdateProduct'�ϴ� ��ư�� �ִ� ������'�̴�.
		//UpdateProductView ���������� ������ ��ȸ�� ��ǰ�� �������� select�ؼ� default������ ��������Ѵ�.
		//==> ��ȸ������(getProduct)�� �������� request.setAttribute("getvo", vo);�� �����صξ��� !!  
		//==> request.getAttribute("getvo"); �� ����� �� �ִ� ! -> ���� �ö󰡺���...
		
		request.setAttribute("select", product);

		return "forward:/product/UpdateProductView.jsp";   
	}
}
