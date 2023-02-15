package com.model2.mvc.view.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class ListPurchaseAction extends Action {
	
public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		HttpSession session=request.getSession();
		User user = (User)session.getAttribute("user");
		
		System.out.println(user.getUserId());
		String userId = user.getUserId();
		Search search = new Search();
		
		int currentPage=1; //ù ������
		
		if(request.getParameter("currentPage") != null) {
			currentPage=Integer.parseInt(request.getParameter("currentPage")); //page�� ��ȣ�� int ȭ //getparameter�� string�̴� !
		}
		
		search.setCurrentPage(currentPage);   	//currentpage ��ȣ ����

		// web.xml  meta-data �� ���� ��� ���� 
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));//3,���������� row
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));//5,���� ������ �������� �� 
		search.setPageSize(pageSize);//SerchVO ����
		
		// Business logic ����
		PurchaseService service=new PurchaseServiceImpl();
		//���ڷ� userId�� �޴� ����? user���� �����̷��� �ٸ��Ƿ� 
		Map<String,Object> map = service.getPurchaseList(search, userId); //ProductServiceImpl�� getProductList ����
		//map�� ���� �ִ� �� = `totalCount`�� `select�ؿ� �������� ���� vo���� list �迭�� ���� list`

		Page resultPage = new Page (currentPage,((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		//���� ������ �������� ������ Page bean�� �����Ѵ� ...
		
		System.out.println("ListUserAction ::"+resultPage);
		System.out.println("���¾� list�̴� !!!!"+map.get("list2"));
		
		request.setAttribute("list3", map.get("list2")); // `select�ؿ� �������� ���� vo���� list �迭�� ���� list`
		request.setAttribute("resultPage", resultPage); //���� ������ ����
		request.setAttribute("search", search); // page �˻� ���ǵ��� ������ search
		
		return "forward:/purchase/listPurchase.jsp";
		
	}

}
