package com.model2.mvc.view.product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileItem;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class UpdateProductAction extends Action {
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		System.out.println("prodNo잘 넘어오나요? " + prodNo);
		if(FileUpload.isMultipartContent(request)) {
			
			String temDir = "C:\\Users\\user\\git\\03.Model2MVCShop(EL,JSTL)\\03.Model2MVCShop(EL,JSTL)\\src\\main\\webapp\\images\\uploadFiles";
			//String temDir2 = "/uploadFiles/";
			
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(temDir);
			fileUpload.setSizeMax(1024*1024*10);
			fileUpload.setSizeThreshold(1024*100);
			
			if(request.getContentLength() < fileUpload.getSizeMax()) {
				Product productVO = new Product();
				
				StringTokenizer token = null;
				
				ProductServiceImpl service = new ProductServiceImpl();
				
				List fileItemList = fileUpload.parseRequest(request);
				int Size = fileItemList.size();
				for ( int i =0;i<Size;i++) {
					FileItem fileItem = (FileItem) fileItemList.get(i);
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("manuDate")) {
							token = new StringTokenizer(fileItem.getString("euc-kr"),"-");
							String manuDate = token.nextToken();
								while(token.hasMoreTokens())
									manuDate +=token.nextToken();
								productVO.setManuDate(manuDate);
								
						}else if(fileItem.getFieldName().equals("prodName"))
							productVO.setProdName(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("prodDetail"))
							productVO.setProdDetail(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("price"))
							productVO.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
						else if(fileItem.getFieldName().equals("prodNo")) {
							prodNo = Integer.parseInt(fileItem.getString("euc-kr"));
								System.out.println("prodNo는 무엇인가요?"+prodNo);
							productVO.setProdNo(prodNo);
						}
					
					}else {
						
						if(fileItem.getSize() > 0) {
							int idx = fileItem.getName().lastIndexOf("\\");
							if(idx==-1) {
								idx=fileItem.getName().lastIndexOf("/");
							}
							String fileName = fileItem.getName().substring(idx+1);
							productVO.setFileName(fileName);
							try {
								File uploadedFile = new File(temDir, fileName);
								fileItem.write(uploadedFile);
							} catch (IOException e) {
								System.out.println(e);
							}
						} else {
							productVO.setFileName("../../images/empty.GIF");
						}
					} //else
				} //for
				
				service.updateProduct(productVO);
				System.out.println("변경된productVO는 ??? -> "+productVO);
				
				HttpSession session=request.getSession();
				session.setAttribute("upcom", service.getProduct(prodNo));		
				
				//request.setAttribute("upcom", );
				
				
				System.out.println("get해온 VO는 ?? " +productVO);
				
			
			} else {
				int overSize = (request.getContentLength() / 1000000);
				System.out.println("<script>alert('파일의 크기는 1MB까지 입니다. 올리신 파일 용량은"+overSize+"MB입니다');");
				System.out.println("history.back();</script>");
			}
		}else {
			System.out.println("인코딩 타입이 multipartform-data가 아닙니다.");
		}
		return "foward:/product/UpdateProduct.jsp";
	}
}

/*
public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		//getparameter로 가져오려면 UpdateProductView.jsp에서 prodNo 실어와야한다.fnc에도 실을 수 있어야한다 ..?
		System.out.println("마지막!!성공 ~~"+prodNo);
		System.out.println(request.getParameter("regdate"));
		
		Product product = new Product();
		//View 입력값 기반으로 VO 세팅 ==> request.getParameter 사용
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		
		System.out.println("VO에 view에서 입력한거 세팅 완료");
		
		ProductService service = new ProductServiceImpl();
		
		service.updateProduct(product);//'View 입력값 기반으로 세팅해준 VO'를 가지고 Update쿼리 날림 !
		
		request.setAttribute("upcom",service.getProduct(prodNo));//update완료된(완료에 사용된) VO
		
		return "forward:/product/UpdateProduct.jsp"; 
	}
}
*/