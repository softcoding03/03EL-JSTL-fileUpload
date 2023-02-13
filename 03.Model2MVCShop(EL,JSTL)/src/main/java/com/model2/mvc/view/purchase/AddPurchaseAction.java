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
		//addPurchaseView���� ������ ������ addPurchase ������ ������ Purchase�� ����
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		Product product = new ProductServiceImpl().getProduct(prodNo);//proNo�� product����
		
		HttpSession session=request.getSession();
		User user = (User)session.getAttribute("user");

		System.out.println("userId��? ="+ user.getUserId());
		
		//purchase ����
		Purchase purchase = new Purchase();
		
		purchase.setBuyer(user);    //user ���� purchase�� ����
		purchase.setPurchaseProd(product); //product ���� purchase�� ����
		purchase.setDlvyAddr(request.getParameter("receiverAddr"));
		purchase.setDlvyDate(request.getParameter("receiverDate"));
		purchase.setDlvyRequest(request.getParameter("receiverRequest"));  
		//purchase.setOrderDate(); => insert �������� sysdate?   
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setTranCode("1");   // => ���� ���Ĵ� '���ſϷ�' / '�����(����ϱ�)' 2 / '���ǵ���' 3
				
		System.out.println("userId  = "+ purchase.getBuyer().getUserId());
		System.out.println("prodNo��?  = "+ purchase.getPurchaseProd().getProdNo());
		System.out.println("Purchase ���� �Ϸ�");
		
		
		
		new PurchaseServiceImpl().addPurchase(purchase);
		
		System.out.println("insert���� ������ �Ϸ�(���ŵ�� �Ϸ�)");
		
		//request.setAttribute("add", product);
		
		request.setAttribute("purchase", purchase);
		return "forward:/purchase/addPurchase.jsp";
	}
}