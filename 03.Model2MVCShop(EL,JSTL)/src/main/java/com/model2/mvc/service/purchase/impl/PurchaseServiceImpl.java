package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDao;
import com.model2.mvc.service.user.UserService;



public class PurchaseServiceImpl implements PurchaseService{
	
	private ProductDao productDao;
	private PurchaseDao purchaseDao;
	
	public PurchaseServiceImpl() {
		purchaseDao = new PurchaseDao();
	}
	
	
	public void addPurchase(Purchase purchase) throws Exception {
		purchaseDao.insertPurchase(purchase);
		System.out.println("��ǰ �������� insert�Ϸ�");
	}

	
	public Purchase getPurchase(int tranNo) throws Exception {
		return purchaseDao.findPurchase(tranNo);
	}

	
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		return purchaseDao.getPurchaseList(search, userId);
	}

	
	public void updatePurchase(Purchase purchase) throws Exception {
		purchaseDao.updatePurchase(purchase);
		System.out.println("�������� �����Ϸ�");
	}

	
	public void updateTranCode(Purchase purchase) throws Exception {
		purchaseDao.updateTranCode(purchase);
		System.out.println("���� ���� �ڵ� �����Ϸ�");
	}

	/*
	public Map<String, Object> getSaleList(Search search) throws Exception {
		return purchaseDao.getSaleList(search);
	}
	*/

	
	
	

}