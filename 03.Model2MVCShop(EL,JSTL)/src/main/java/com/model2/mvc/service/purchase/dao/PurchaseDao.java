package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;


public class PurchaseDao {
	
	public PurchaseDao() {
	}
	
	//�������� �� ��ȸ(�������� �Է��� �ߴ� â)
	public Purchase findPurchase(int tranNo) throws Exception {
		//��ǰ ���� �� �ߴ� '���ſϷ�Ǿ����ϴ�' â�� ���� findPurchase
		//(prodNo�� ���ڷ� �޾Ƽ� select ���� ~(ProductVO ��ü�� ���õǾ� ����.)
		
		//fncAddPurchase���� prodNo�� �Ѱ��ְ� Action���� request.getParameter�ؿµ�
		//ProductVO.setprodNo�� �����ϰ� �װ� ProductVO.getprodNo�� �����ͼ� int pordNo������ �־��ٰ�.
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from transaction where prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		Purchase purchase = null;
		
		while (rs.next()) {
			purchase = new Purchase();
			
			purchase.setUserId(rs.getString("USER_ID"));
			purchase.setUserName(rs.getString("USER_NAME"));
			purchase.setPassword(rs.getString("PASSWORD"));
			purchase.setRole(rs.getString("ROLE"));
			purchase.setSsn(rs.getString("SSN"));
			purchase.setPhone(rs.getString("CELL_PHONE")); 
			purchase.setAddr(rs.getString("ADDR"));
			purchase.setEmail(rs.getString("EMAIL"));
			purchase.setRegDate(rs.getDate("REG_DATE"));
		}
		
		con.close();

		return userVO; //WHERE = userId �� �̿��� ȸ�� ������ ������ userVO
	}
	
	
	//���Ÿ� ���� ���� ���� ���� ��ư �� �Է� â ?
	public void insertPurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
	
		//���� �� �Է� ������ sql�� insert ���ֱ�.
		//2�� 3(1,2) column�� ProductVO(prod_no), UsersVO(user_id)�� �����ؿ;��Ѵ�.
		//(sql table���� prod_no�� buyer_id�� foriegn Ű�� �������־��� !)
		
		//�Ʒ��� Action���� ������ �� !
		//JSP���� user_id �� prodNo ������ְ� �װ� getParameter�ؿ��� �ȴ�.
		//JSP���� java ���� UserVO uservo = session VO �� ��������
		//JSP���� user_ID ������ָ� �ȴ�. �׷��� ���⼭�� �׳� getParameter�� ��������
		//request.getParameter("prodNo")
		//request.getParameter("userid") Action���� int, String ��ü�� �����ؼ� ���⼭ ����ϱ�	
		
		String sql = "insert into TRANSACTION values (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),to_date(?,'yyyy-mm-dd')";
		
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setString(1, );  //product �� prod_no �����;���.
		stmt.setString(2, );		//user�� buyer_id(������ id) �����;��� 
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getReceiverPhone());
		stmt.setString(6, purchaseVO.getDivyAddr());
		stmt.setString(7, purchaseVO.getDivyRequest());
		stmt.setString(8, purchaseVO.getTranCode());
		stmt.setString(9, purchaseVO.getOrderDate());
		stmt.setString(10, purchaseVO.getDivyDate());
		
		stmt.executeUpdate();
		
		con.close();
	}


	
	//���Ÿ�Ϻ��⸦ ����(User����)
	public Map<String,Object> getPurchaseList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT ";
	
		
		if (search.getSearchCondition() != null) {
			if ( search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE PROD_NO = '" + search.getSearchKeyword()+"'";
			} else if ( search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {// ��ǰ��ȣ�� serchVO�� ��ȣ�� ���õǾ��־����
				sql += " WHERE PROD_NAME LIKE '%" + search.getSearchKeyword()+"%'";
			} else if ( search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {// ��ǰ���� serchVO�� ��ȣ�� ���õǾ��־����
				sql += " WHERE PRICE ='" + search.getSearchKeyword()+"'";//���� ������
			}
		}
		sql += " order by prod_no";     
		
		System.out.println("ProductDAO :: Original SQL :: " + sql);
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql); //ProductDao.getTotalCount ���� -> ���ڵ� �� row �� ��ȯ
		System.out.println("ProductDAO :: totalCount :: " + totalCount);
		
		//==> CurrentPage �Խù��� �޵��� Query �ٽñ���(��絥���� ������ �ʿ� ���� ������)
		sql = makeCurrentPageSql(sql, search); //ProductDao.makeCurrentPageSql ����
		
		PreparedStatement stmt = con.prepareStatement(sql);
		//TYPE_SCROLL_INSENSITIVE = rs.next()�� ���� row�� �������� ������ �����ͷ� ���ư��� ���Ѵ�. Ŀ���� ������ �ڸ����� �ٽ� ���ư� �� �ְ� �������.
		//CONCUR_UPDATABLE result�� ������ ���� updateable �� �� �ִ�.
		ResultSet rs = stmt.executeQuery();

		System.out.println(search);
		
		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()){
				Product vo = new Product();
				
				vo.setProdNo(rs.getInt("PROD_NO"));
				vo.setProdName(rs.getString("PROD_NAME"));
				vo.setProdDetail(rs.getString("PROD_DETAIL"));
				vo.setManuDate(rs.getString("MANUFACTURE_DAY"));
				vo.setPrice(rs.getInt("PRICE"));
				vo.setFileName(rs.getString("IMAGE_FILE"));
				vo.setRegDate(rs.getDate("REG_DATE"));
				list.add(vo);  //list��, select�ؿ� �������� ������ vo�� ��´�.
		}
		
	
		//==> totalCount ���� ����
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage �� �Խù� ���� ���� List ����
		map.put("list", list);
		
		rs.close();
		stmt.close();
		con.close();
			
		return map; //totalCount�� 'list'(select�ؿ� vo �������� ���� list) 
	}
	
	
	//�ǸŸ�Ϻ��⸦ ����(Admin����)
	public Map<String,Object> getSaleList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT ";
	
		
		if (search.getSearchCondition() != null) {
			if ( search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE PROD_NO = '" + search.getSearchKeyword()+"'";
			} else if ( search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {// ��ǰ��ȣ�� serchVO�� ��ȣ�� ���õǾ��־����
				sql += " WHERE PROD_NAME LIKE '%" + search.getSearchKeyword()+"%'";
			} else if ( search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {// ��ǰ���� serchVO�� ��ȣ�� ���õǾ��־����
				sql += " WHERE PRICE ='" + search.getSearchKeyword()+"'";//���� ������
			}
		}
		sql += " order by prod_no";     
		
		System.out.println("ProductDAO :: Original SQL :: " + sql);
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql); //ProductDao.getTotalCount ���� -> ���ڵ� �� row �� ��ȯ
		System.out.println("ProductDAO :: totalCount :: " + totalCount);
		
		//==> CurrentPage �Խù��� �޵��� Query �ٽñ���(��絥���� ������ �ʿ� ���� ������)
		sql = makeCurrentPageSql(sql, search); //ProductDao.makeCurrentPageSql ����
		
		PreparedStatement stmt = con.prepareStatement(sql);
		//TYPE_SCROLL_INSENSITIVE = rs.next()�� ���� row�� �������� ������ �����ͷ� ���ư��� ���Ѵ�. Ŀ���� ������ �ڸ����� �ٽ� ���ư� �� �ְ� �������.
		//CONCUR_UPDATABLE result�� ������ ���� updateable �� �� �ִ�.
		ResultSet rs = stmt.executeQuery();

		System.out.println(search);
		
		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()){
				Product vo = new Product();
				
				vo.setProdNo(rs.getInt("PROD_NO"));
				vo.setProdName(rs.getString("PROD_NAME"));
				vo.setProdDetail(rs.getString("PROD_DETAIL"));
				vo.setManuDate(rs.getString("MANUFACTURE_DAY"));
				vo.setPrice(rs.getInt("PRICE"));
				vo.setFileName(rs.getString("IMAGE_FILE"));
				vo.setRegDate(rs.getDate("REG_DATE"));
				list.add(vo);  //list��, select�ؿ� �������� ������ vo�� ��´�.
		}
		
	
		//==> totalCount ���� ����
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage �� �Խù� ���� ���� List ����
		map.put("list", list);
		
		rs.close();
		stmt.close();
		con.close();
			
		return map; //totalCount�� 'list'(select�ؿ� vo �������� ���� list) 
	}

	//�������������� ����
	public void updatePurchase(Purchase purchase) throws Exception {
		//browser���� ��ǰ�������� ���� �Է¹��� �� �����س��� productVO�� ���ڷ� ����
		Connection con = DBUtil.getConnection();
		
		String sql = "update PRODUCT set PROD_NAME=?,PROD_DETAIL=?,MANUFACTURE_DAY=to_char(to_date(?,'yyyy-mm-dd'),'yyyymmdd'),price=?,IMAGE_FILE=? where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		stmt.setInt(6, productVO.getProdNo());
		stmt.executeUpdate();
		
		System.out.println("update ���� ������ �Ϸ� !");
		
		con.close();
	}
	

	//���Ż��� �ڵ������ ����
	public void updateTranCode(Purchase purchase) throws Exception {
		//browser���� ��ǰ�������� ���� �Է¹��� �� �����س��� productVO�� ���ڷ� ����
		Connection con = DBUtil.getConnection();
		
		String sql = "update PRODUCT set PROD_NAME=?,PROD_DETAIL=?,MANUFACTURE_DAY=to_char(to_date(?,'yyyy-mm-dd'),'yyyymmdd'),price=?,IMAGE_FILE=? where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		stmt.setInt(6, productVO.getProdNo());
		stmt.executeUpdate();
		
		System.out.println("update ���� ������ �Ϸ� !");
		
		con.close();
	}
	
	
	
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);// ù ���� int ?
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount; //���ڵ� row �� ��ȯ
	}
	
	// �Խ��� currentPage Row ��  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (SELECT inner_table. * , ROWNUM AS row_seq" +
							" FROM ("+sql+") inner_table"+
							" WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("UserDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
	
}
