package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.user.UserService;



public class ProductServiceImpl implements ProductService{
	
	private ProductDao productDao;
	
	public ProductServiceImpl() {
		productDao = new ProductDao();
	}
	
	public void addProduct(Product product) throws Exception {
		productDao.insertProduct(product);
		System.out.println("insertProduct �Ϸ�");
		
	}

	@Override
	public Product getProduct(int productNo) throws Exception {
		return productDao.getProduct(productNo);
		
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		return productDao.getProductList(search);   
		//���ǿ� �´� product�������� select-> vo ->list�� ��� mapping���� ���ذ� ==> map �� ���������� return �ؿ´�.
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		productDao.updateProduct(product);
		//browser���� ��ǰ�������� ���� �Է¹��� �� �����س��� productVO�� ���ڷ� ����
	}
	
	
	
	/*
	public HashMap<String,Object> getUserList(SearchVO searchVO) throws Exception {
		return userDAO.getUserList(searchVO);
	}

	public void addUser(UserVO userVO) throws Exception {
		userDAO.insertUser(userVO);
	}

	
	public UserVO loginUser(UserVO userVO) throws Exception {    //userVO������ -> userId, userPassword ���õ� ����
			UserVO dbUser = userDAO.findUser(userVO.getUserId()); //userId�� ������ sql�� ����� userId�� �Ӽ����� find �ؿ���. 
											//WHERE = userId �� �̿��� ȸ�� ������ ������ 'userVO' -> dbUser�� ����.

			if(! dbUser.getPassword().equals(userVO.getPassword())) //DB(sql)���� �ҷ��� ������ �Է��� ������ ��ġ�ϴ��� Ȯ�� 
				throw new Exception("�α��ο� �����߽��ϴ�.");
			
			return dbUser;
	}

	public UserVO getUser(String userId) throws Exception {
		return userDAO.findUser(userId);
	}

	

	public void updateUser(UserVO userVO) throws Exception {
		userDAO.updateUser(userVO);
	}

	public boolean checkDuplication(String userId) throws Exception {
		boolean result=true;
		UserVO userVO=userDAO.findUser(userId);
		if(userVO != null) {
			result=false;
		}
		return result;
	}

	@Override
	*/
}