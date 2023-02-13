package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdatePurchaseAction extends Action {
	

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		System.out.println("tranNo는??? = "+tranNo);
		
		Purchase purchase = new Purchase();
		
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDlvyAddr(request.getParameter("receiverAddr"));
		purchase.setDlvyRequest(request.getParameter("receiverRequest"));
		purchase.setDlvyDate(request.getParameter("dlvyDate"));
		purchase.setTranNo(tranNo);
		
		System.out.println("view에서 입력한거 update전 세팅 완료");
		
		PurchaseService service = new PurchaseServiceImpl();

		service.updatePurchase(purchase); 
		
		request.setAttribute("upPurchase", service.getPurchase(tranNo));
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
}
