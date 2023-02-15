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
	
	//구매정보 상세 조회(구매정보 입력후 뜨는 창)
	public Purchase findPurchase(int tranNo) throws Exception {
		//상품 구매 후 뜨는 '구매완료되었습니다' 창을 위한 findPurchase
		//(prodNo를 인자로 받아서 select 실행 ~(ProductVO 자체가 세팅되어 있음.)
		
		//fncAddPurchase에서 prodNo를 넘겨주고 Action에서 request.getParameter해온뒤
		//ProductVO.setprodNo에 세팅하고 그걸 ProductVO.getprodNo로 가져와서 int pordNo변수에 넣어줄것.
		
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
	
	
	//구매를 위한 정보 삽입 구매 버튼 후 입력 창 ?
	public void insertPurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();
	
		//구매 시 입력 정보들 sql에 insert 해주기.
		//2와 3(1,2) column은 Product(prod_no), Users(user_id)를 참조해와야한다.
		//(sql table에서 prod_no와 buyer_id는 foriegn 키로 설정해주었음 !)
		
		//아래는 Action에서 실행할 것 !
		//JSP에서 user_id 및 prodNo 출력해주고 그걸 getParameter해오면 된다.
		//JSP에서 java 언어로 UserVO uservo = session VO 로 가져오기
		//JSP에서 user_ID 출력해주면 된다. 그래서 여기서는 그냥 getParameter로 가져오기
		//request.getParameter("prodNo")
		//request.getParameter("userid") Action에서 int, String 객체에 저장해서 여기서 사용하기	
		
		//String sql = "insert into TRANSACTION values (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),to_date(?,'yyyy-mm-dd')";
		String sql = "insert into TRANSACTION values (seq_transaction_tran_no.nextval,?,?,?,?,?,?,?,?,sysdate,?)";
		PreparedStatement stmt = con.prepareStatement(sql);

		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());  //product 의 prod_no 가져와야함.
		stmt.setString(2, purchase.getBuyer().getUserId());		//user의 buyer_id(구매자 id) 가져와야함 
		stmt.setString(3, purchase.getPaymentOption()); //table에는 char 형식이다. 어떻게 해야할지...
		stmt.setString(4, purchase.getReceiverName());
		stmt.setString(5, purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDlvyAddr());
		stmt.setString(7, purchase.getDlvyRequest());
		stmt.setString(8, purchase.getTranCode());
		//stmt.setString(9, purchase.getOrderDate());   sysdate
		stmt.setString(9, purchase.getDlvyDate()); 
		
		System.out.println("insert쿼리날리기 완료 1");
		stmt.executeUpdate();
		
		con.close();
		stmt.close();
	}


	
	//구매목록보기를 위한(User입장)
	public Map<String,Object> getPurchaseList(Search search, String userId) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from transaction where buyer_Id ='"+ userId+"'";
		
		sql += " order by tran_no";     
		
		System.out.println("PurchaseDAO :: Original SQL :: " + sql);
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql); //ProductDao.getTotalCount 실행 -> 레코드 총 row 수 반환
		System.out.println("PurchaseDAO :: totalCount :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성(모든데이터 가져올 필요 없기 때문에) ROWNUM
		sql = makeCurrentPageSql(sql, search); //ProductDao.makeCurrentPageSql 실행
		
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
				list.add(purchase);  //list에, select해온 정보들을 저장한 vo를 담는다.
		}
		
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list2", list);
		
		rs.close();
		stmt.close();
		con.close();
			
		return map; //totalCount와 'list'(select해온 vo 정보들을 담은 list) 
	}
	

	//구매정보수정을 위한
	public void updatePurchase(Purchase purchase) throws Exception {
		//browser에서 상품정보수정 값들 입력받은 것 세팅해놓은 productVO를 인자로 받음
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
		
		System.out.println("update 쿼리 날리기 완료 !");
		
		stmt.close();
		con.close();
	}
	

	//구매상태 코드수정을 위한
	public void updateTranCode(Purchase purchase) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "update Transaction set tran_status_code=? where tran_NO=?";
		//tranCode String , tranNo int 세팅되어있음
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, purchase.getTranCode());
		stmt.setInt(2, purchase.getTranNo());
		
		stmt.executeUpdate();
		
		System.out.println("update 쿼리 날리기 완료 !");
		
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
			totalCount = rs.getInt(1);// 첫 열의 int ?
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount; //레코드 row 수 반환
	}
	
	// 게시판 currentPage Row 만  return 
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
	//판매목록보기를 위한(Admin입장)
	public Map<String,Object> getSaleList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT ";
	
		sql += " order by prod_no";     
		
		System.out.println("ProductDAO :: Original SQL :: " + sql);
		//==> TotalCount GET
		int totalCount = this.getTotalCount(sql); //ProductDao.getTotalCount 실행 -> 레코드 총 row 수 반환
		System.out.println("ProductDAO :: totalCount :: " + totalCount);
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성(모든데이터 가져올 필요 없기 때문에)
		sql = makeCurrentPageSql(sql, search); //ProductDao.makeCurrentPageSql 실행
		
		PreparedStatement stmt = con.prepareStatement(sql);
		//TYPE_SCROLL_INSENSITIVE = rs.next()은 다음 row를 가져오고 이전의 데이터로 돌아가진 못한다. 커서가 지나간 자리에도 다시 돌아갈 수 있게 만들어줌.
		//CONCUR_UPDATABLE result로 가져온 값을 updateable 할 수 있다.
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
				list.add(vo);  //list에, select해온 정보들을 저장한 vo를 담는다.
		}
		
	
		//==> totalCount 정보 저장
		map.put("totalCount", new Integer(totalCount));
		//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);
		
		rs.close();
		stmt.close();
		con.close();
			
		return map; //totalCount와 'list'(select해온 vo 정보들을 담은 list) 
	}
*/
}
