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
		
		//addpurchaseView.jsp���� ��ǰ��ȣ�� ��ġ�ϴ� 
		//��ǰ�� �������
		//user�� ������ �����ش�.
		

		//������Ʈ������ prodNo ��������
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
			System.out.println("prodNo�� ?"+prodNo);
		
		//Session�� ����� user ������ ��������(userId������� ���� �����ص�)
		HttpSession session=request.getSession();
		User user = (User)session.getAttribute("user");
		System.out.println(user.getUserId());
		
		//prodNo�� Product ���� ���� ��������
		//ProductService service1 = new ProductServiceImpl();
		Product product = new ProductServiceImpl().getProduct(prodNo); //prodNo�� select�ؿ� product ������ product�� ���Ϲ���.
	
		
		request.setAttribute("product", product);
		request.setAttribute("user", user); //jsp���� user������ ������ �� �ֵ��� set
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
}
