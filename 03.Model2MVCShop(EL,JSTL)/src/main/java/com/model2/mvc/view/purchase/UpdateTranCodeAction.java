package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeAction extends Action{
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		String tranCode = request.getParameter("tranCode");
		System.out.println("tranNo�� tranCode��??? = "+tranNo+"��"+tranCode);
		
		Purchase purchase = new Purchase();
		purchase.setTranNo(tranNo);
		purchase.setTranCode(tranCode);
		
		PurchaseService service = new PurchaseServiceImpl();

		service.updateTranCode(purchase); 
		
		System.out.println("tranCode ����Ϸ�");
	
		
		return "forward:/listPurchase.do";
		
	}
}
