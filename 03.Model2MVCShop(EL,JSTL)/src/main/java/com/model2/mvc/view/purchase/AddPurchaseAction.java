package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;



public class AddPurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//addPurchaseView에서 보여준 값들을 addPurchase 실제로 값들을 Purchase에 세팅
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		Product product = new ProductServiceImpl().getProduct(prodNo);//proNo로 product세팅
		
		HttpSession session=request.getSession();
		User user = (User)session.getAttribute("user");

		System.out.println("userId는? ="+ user.getUserId());
		
		//purchase 세팅
		Purchase purchase = new Purchase();
		
		purchase.setBuyer(user);    //user 정보 purchase에 세팅
		purchase.setPurchaseProd(product); //product 정보 purchase에 세팅
		purchase.setDlvyAddr(request.getParameter("receiverAddr"));
		purchase.setDlvyDate(request.getParameter("receiverDate"));
		purchase.setDlvyRequest(request.getParameter("receiverRequest"));  
		//purchase.setOrderDate(); => insert 쿼리에서 sysdate?   
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setTranCode("1");   // => 구매 직후는 '구매완료' / '배송중(배송하기)' 2 / '물건도착' 3
				
		System.out.println("userId  = "+ purchase.getBuyer().getUserId());
		System.out.println("prodNo는?  = "+ purchase.getPurchaseProd().getProdNo());
		System.out.println("Purchase 세팅 완료");
		
		
		
		new PurchaseServiceImpl().addPurchase(purchase);
		
		System.out.println("insert쿼리 날리기 완료(구매등록 완료)");
		
		//request.setAttribute("add", product);
		
		request.setAttribute("purchase", purchase);
		return "forward:/purchase/addPurchase.jsp";
	}
}