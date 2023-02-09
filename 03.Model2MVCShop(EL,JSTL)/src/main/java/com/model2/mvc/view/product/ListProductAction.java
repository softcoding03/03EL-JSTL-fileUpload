package com.model2.mvc.view.product;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class ListProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Search search = new Search();   
		//�˻� �� ��� ȭ�� ���� !
		int currentPage=1; //ù ������
		
		if(request.getParameter("currentPage") != null && !request.getParameter("currentPage").equals("")) {//listUset.do?page=1�� page ��ȣ�� �����´�.
			currentPage=Integer.parseInt(request.getParameter("currentPage")); //page�� ��ȣ�� int ȭ //getparameter�� string�̴� !
		}
		search.setCurrentPage(currentPage);   	//page ��ȣ ����
		search.setSearchCondition(request.getParameter("searchCondition")); //��ǰ��ȣ, ��ǰ��, ����
		search.setSearchKeyword(request.getParameter("searchKeyword")); //���� �Է��ϴ� ��

		// web.xml  meta-data �� ���� ��� ���� 
		int pageSize = Integer.parseInt( getServletContext().getInitParameter("pageSize"));//3,���������� row
		int pageUnit  =  Integer.parseInt(getServletContext().getInitParameter("pageUnit"));//5,���� ������ �������� �� 
		search.setPageSize(pageSize);//SerchVO ����
		
		// Business logic ����
		ProductService service=new ProductServiceImpl();
		Map<String,Object> map = service.getProductList(search); //ProductServiceImpl�� getProductList ����
		//map�� ���� �ִ� �� = `totalCount`�� `select�ؿ� �������� ���� vo���� list �迭�� ���� list`

		Page resultPage = new Page (currentPage,((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		//���� ������ �������� ������ Page bean�� �����Ѵ� ...
		
		System.out.println("ListUserAction ::"+resultPage);
		
		request.setAttribute("list", map.get("list")); // `select�ؿ� �������� ���� vo���� list �迭�� ���� list`
		request.setAttribute("resultPage", resultPage); //���� ������ ����
		request.setAttribute("search", search); // page �˻� ���ǵ��� ������ search
		
		char c = request.getParameter("menu").charAt(0);
		
		if(c == 'm') {
			return "forward:/product/listProductManage.jsp";
		}
		return "forward:/product/listProductSearch.jsp";
		
	}
}