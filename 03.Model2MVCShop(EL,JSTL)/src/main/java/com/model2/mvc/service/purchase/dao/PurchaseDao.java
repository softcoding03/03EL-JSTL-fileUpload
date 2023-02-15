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
import com.model2.mvc.service.product.dao.ProductDao;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.dao.UserDao;
import com.model2.mvc.service.user.impl.UserServiceImpl;


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

		String sql = "select * from transaction where tran_No=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();

		Purchase purchase = null;
		
		
		while (rs.next()) {
			purchase = new Purchase();
			
			purchase.setReceiverPhone(rs.getString("Receiver_Phone"));
			purchase.setReceiverName(rs.getString("Receiver_Name"));
			purchase.setPaymentOption(rs.getString("Payment_Option").trim());
			purchase.setOrderDate(rs.getString("Order_Date"));
			purchase.setDlvyRequest(rs.getString("Dlvy_Request"));
			purchase.setDlvyDate(rs.getString("Dlvy_Date")); 
			purchase.setDlvyAddr(rs.getString("Dlvy_Addr"));
			purchase.setTranNo(tranNo);
			purchase.setPurchaseProd(new ProductServiceImpl().getProduct(rs.getInt("prod_no"))); 
			purchase.setBuyer(new UserServiceImpl().getUser(rs.getString("buyer_id")));
		}
		rs.close();
		con.close();

		return purchase;
	}
	
	
	//���Ÿ� ���� ���� ���� ���� ��ư �� �Է� â ?
	public void insertPurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
	
		//���� �� �Է� ������ sql�� insert ���ֱ�.
		//2�� 3(1,2) column�� Product(prod_no), Users(user_id)�� �����ؿ;��Ѵ�.
		//(sql table���� prod_no�� buyer_id�� foriegn Ű�� �������־��� !)
		
		//�Ʒ��� Action���� ������ �� !
		//JSP���� user_id �� prodNo ������ְ� �װ� getParameter�ؿ��� �ȴ�.
		//JSP���� java ���� UserVO uservo = session VO �� ��������
		//JSP���� user_ID ������ָ� �ȴ�. �׷��� ���⼭�� �׳� getParameter�� ��������
		//request.getParameter("prodNo")
		//request.getParameter("userid") Action���� int, String ��ü�� �����ؼ� ���⼭ ����ϱ�	
		
		//String sql = "insert into TRANSACTION values (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),to_date(?,'yyyy-mm-dd')";
		String sql = "insert into TRANSACTION values (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,sysdate,?)";
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());  //product �� prod_no �����;���.
		stmt.setString(2, purchase.getBuyer().getUserId());		//user�� buyer_id(������ id) �����;��� 
		stmt.setString(3, purchase.getPaymentOption()); //table���� char �����̴�. ��� �ؾ�����...
		stmt.setString(4, purchase.getReceiverName());
		stmt.setString(5, purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDlvyAddr());
		stmt.setString(7, purchase.getDlvyRequest());
		stmt.setString(8, purchase.getTranCode());
		//stmt.setString(9, purchase.getOrderDate());   sysdate
		stmt.setString(9, purchase.getDlvyDate()); 
		
		System.out.println("insert���������� �Ϸ� 1");
		stmt.executeUpdate();
		
		con.close();
		stmt.close();
	}


	
	//���Ÿ�Ϻ��⸦ ����(User����)
	public Map<String,Object> getPurchaseList(Search search, String userId) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from transaction where buyer_Id ='"+ userId+"'";
		
		sql += " order by tran_no";     
		
		System.out.println("PurchaseDAO :: Original SQL :: " + sql);
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql); //ProductDao.getTotalCount ���� -> ���ڵ� �� row �� ��ȯ
		System.out.println("PurchaseDAO :: totalCount :: " + totalCount);
		
		//==> CurrentPage �Խù��� �޵��� Query �ٽñ���(��絥���� ������ �ʿ� ���� ������) ROWNUM
		sql = makeCurrentPageSql(sql, search); //ProductDao.makeCurrentPageSql ����
		
		PreparedStatement stmt = con.prepareStatement(sql);

		ResultSet rs = stmt.executeQuery();

		System.out.println(search);
		
		List<Purchase> list = new ArrayList<Purchase>();
	
		while(rs.next()){
				
				Purchase purchase = new Purchase();
				purchase.setDlvyAddr(rs.getString("Dlvy_Addr"));
				purchase.setDlvyDate(rs.getString("dlvy_date"));
				purchase.setDlvyRequest(rs.getString("dlvy_request"));
				purchase.setOrderDate(rs.getString("order_Date"));
				purchase.setPaymentOption(rs.getString("Payment_Option"));
				purchase.setReceiverName(rs.getString("Receiver_Name"));
				purchase.setReceiverPhone(rs.getString("Receiver_Phone"));
				purchase.setTranCode(rs.getString("tran_status_code").trim());
				purchase.setTranNo(rs.getInt("tran_no"));
				purchase.setPurchaseProd(new ProductServiceImpl().getProduct(rs.getInt("prod_no"))); 
				purchase.setBuyer(new UserServiceImpl().getUser(rs.getString("buyer_id")));
				list.add(purchase);  //list��, select�ؿ� �������� ������ vo�� ��´�.
		}
		
		//==> totalCount ���� ����
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage �� �Խù� ���� ���� List ����
		map.put("list2", list);
		
		rs.close();
		stmt.close();
		con.close();
			
		return map; //totalCount�� 'list'(select�ؿ� vo �������� ���� list) 
	}
	

	//�������������� ����
	public void updatePurchase(Purchase purchase) throws Exception {
		//browser���� ��ǰ�������� ���� �Է¹��� �� �����س��� productVO�� ���ڷ� ����
		Connection con = DBUtil.getConnection();
		
		String sql = "update TRANSACTION set payment_option=?,receiver_name=?,receiver_phone=?,dlvy_addr=?,dlvy_request=?,dlvy_date=? where tran_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchase.getPaymentOption());
		stmt.setString(2, purchase.getReceiverName());
		stmt.setString(3, purchase.getReceiverPhone());
		stmt.setString(4, purchase.getDlvyAddr());
		stmt.setString(5, purchase.getDlvyRequest());
		stmt.setString(6, purchase.getDlvyDate());
		stmt.setInt(7, purchase.getTranNo());
		stmt.executeUpdate();
		
		System.out.println("update ���� ������ �Ϸ� !");
		
		stmt.close();
		con.close();
	}
	

	//���Ż��� �ڵ������ ����
	public void updateTranCode(Purchase purchase) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "update Transaction set tran_status_code=? where tran_NO=?";
		//tranCode String , tranNo int ���õǾ�����
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, purchase.getTranCode());
		stmt.setInt(2, purchase.getTranNo());
		
		stmt.executeUpdate();
		
		System.out.println("update ���� ������ �Ϸ� !");
		
		stmt.close();
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
		
		System.out.println("purchaseDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
	/*
	//�ǸŸ�Ϻ��⸦ ����(Admin����)
	public Map<String,Object> getSaleList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT ";
	
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
*/
}
