package com.kh.admin.adminOrder.model.dao;
import static com.kh.common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.admin.adminOrder.model.vo.adminOrder;
import com.kh.admin.adminOrder.model.vo.adminOrderList;
public class adminOrderDao {
	
	private Properties prop = new Properties();
	
	public adminOrderDao() {
		
		String fileName = adminOrderDao.class.getResource("/sql/admin/adminOrder/adminOrder-query.properties").getPath();
		
		try {
			prop.load(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<adminOrderList> selectList(Connection conn) {
		
		ArrayList<adminOrderList> list = new ArrayList<adminOrderList>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectDetailList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				adminOrderList aol = new adminOrderList();
				
				aol.setOrderDetailNo(rset.getInt("ORDER_DETAIL_NO"));
				aol.setProdName(rset.getString("PROD_NAME"));
				aol.setUserId(rset.getString("USER_ID"));
				aol.setOrderDate(rset.getDate("ORDER_DATE"));
				aol.setOrderAmount(rset.getInt("ORDERS_AMOUNT"));
				aol.setOrdersStatus(rset.getInt("ORDERS_STATUS"));
				aol.setOrderNo(rset.getInt("ORDER_NO"));
				aol.setProdNo(rset.getInt("PROD_NO"));
				aol.setOrdersPrice(rset.getInt("ORDERS_PRICE"));
				aol.setForestName(rset.getString("FOREST_NAME"));
				
				list.add(aol);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}		
		
		return list;
	}

	public ArrayList<adminOrder> selectOrderList(Connection conn) {

		ArrayList<adminOrder> orderList = new ArrayList<adminOrder>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectOrderList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				adminOrder ao = new adminOrder();
				
				ao.setOrderNo(rset.getInt("ORDER_NO"));
				ao.setUserId(rset.getString("USER_ID"));
				ao.setOrderName(rset.getString("ORDER_NAME"));
				ao.setOrderPhone(rset.getString("ORDER_PHONE"));
				ao.setOrderAddress(rset.getString("ORDER_ADDRESS"));
				ao.setOrderMessage(rset.getString("ORDER_MESSAGE"));
				ao.setOrderTotalPrice(rset.getInt("ORDER_TOTAL_PRICE"));				
				ao.setOrderDate(rset.getDate("ORDER_DATE"));				
				
				orderList.add(ao);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(rset);
			close(pstmt);
		}		
		
		return orderList;
	}
	
	public int updateOrder(Connection conn, int orderDetailNo, int selectResult) {
		
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updateOrder");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, selectResult);
			pstmt.setInt(2, orderDetailNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(pstmt);
		}	
		
		return result;
	}

}
