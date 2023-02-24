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
		System.out.println("prodNo�� �Ѿ������? " + prodNo);
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
								System.out.println("prodNo�� �����ΰ���?"+prodNo);
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
				System.out.println("�����productVO�� ??? -> "+productVO);
				
				HttpSession session=request.getSession();
				session.setAttribute("upcom", service.getProduct(prodNo));		
				
				//request.setAttribute("upcom", );
				
				
				System.out.println("get�ؿ� VO�� ?? " +productVO);
				
			
			} else {
				int overSize = (request.getContentLength() / 1000000);
				System.out.println("<script>alert('������ ũ��� 1MB���� �Դϴ�. �ø��� ���� �뷮��"+overSize+"MB�Դϴ�');");
				System.out.println("history.back();</script>");
			}
		}else {
			System.out.println("���ڵ� Ÿ���� multipartform-data�� �ƴմϴ�.");
		}
		return "foward:/product/UpdateProduct.jsp";
	}
}

/*
public class UpdateProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		//getparameter�� ���������� UpdateProductView.jsp���� prodNo �Ǿ�;��Ѵ�.fnc���� ���� �� �־���Ѵ� ..?
		System.out.println("������!!���� ~~"+prodNo);
		System.out.println(request.getParameter("regdate"));
		
		Product product = new Product();
		//View �Է°� ������� VO ���� ==> request.getParameter ���
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		
		System.out.println("VO�� view���� �Է��Ѱ� ���� �Ϸ�");
		
		ProductService service = new ProductServiceImpl();
		
		service.updateProduct(product);//'View �Է°� ������� �������� VO'�� ������ Update���� ���� !
		
		request.setAttribute("upcom",service.getProduct(prodNo));//update�Ϸ��(�Ϸῡ ����) VO
		
		return "forward:/product/UpdateProduct.jsp"; 
	}
}
*/