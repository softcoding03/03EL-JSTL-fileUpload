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
		System.out.println("insertProduct 완료");
		
	}

	@Override
	public Product getProduct(int productNo) throws Exception {
		return productDao.getProduct(productNo);
		
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		return productDao.getProductList(search);   
		//조건에 맞는 product정보들을 select-> vo ->list에 담고 mapping까지 해준것 ==> map 을 최종적으로 return 해온다.
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		productDao.updateProduct(product);
		//browser에서 상품정보수정 값들 입력받은 것 세팅해놓은 productVO를 인자로 보냄
	}
	
	
	
	/*
	public HashMap<String,Object> getUserList(SearchVO searchVO) throws Exception {
		return userDAO.getUserList(searchVO);
	}

	public void addUser(UserVO userVO) throws Exception {
		userDAO.insertUser(userVO);
	}

	
	public UserVO loginUser(UserVO userVO) throws Exception {    //userVO변수에 -> userId, userPassword 세팅된 상태
			UserVO dbUser = userDAO.findUser(userVO.getUserId()); //userId를 가지고 sql에 저장된 userId의 속성들을 find 해오다. 
											//WHERE = userId 를 이용해 회원 정보를 저장한 'userVO' -> dbUser에 저장.

			if(! dbUser.getPassword().equals(userVO.getPassword())) //DB(sql)에서 불러온 정보와 입력한 정보가 일치하는지 확인 
				throw new Exception("로그인에 실패했습니다.");
			
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