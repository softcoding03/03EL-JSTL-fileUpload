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
		//jsp에서 설정해준 링크에서 request.setParameter("prodNo","10000") 되어있는 것!!!!@@
		//언제든 request.getParameter로 꺼내쓸 수 있음 ! 
		//but String return 해준다는 점 기억할 것 !
		System.out.println(productNo);
		
		ProductService service = new ProductServiceImpl();
		
		Product vo = service.getProduct(productNo); //ProductServiceImpl의 getProduct()
								//'vo'에 where = productNo 맞는 값들 
		
		request.setAttribute("getVO", vo); 
		//'whwere= productNo에 맞는 나머지 정보들을 select 한것'을 vo에 집어넣고  
		//"vo"라는 request Scope를 사용하여 이 vo와  매핑 주었다.		
		//나중에 UpdateProductView에서 초기값 보여줄때에도 getVO 사용한다.

		
		return "forward:/product/getProduct.jsp";
	}
}