package com.kh.admin.adminProduct.model.dao;
import static com.kh.common.JDBCTemplate.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.admin.adminProduct.model.vo.adminProduct;
import com.kh.product.model.vo.Attachment;

public class adminProductDao {
	
	private Properties prop = new Properties();
	
	public adminProductDao() {
		String fileName = adminProductDao.class.getResource("/sql/admin/adminProduct/adminProduct-query.properties").getPath();
		//System.out.println("fileName : " + fileName);
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

	public ArrayList<adminProduct> selectList(Connection conn) {
		
		ArrayList<adminProduct> list = new ArrayList<adminProduct>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectProdList");		
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();			
			
			while(rset.next()) {
				adminProduct ap = new adminProduct();
				
				ap.setProdNo(rset.getInt("PROD_NO"));
				ap.setProdName(rset.getString("PROD_NAME"));
				ap.setProdCategory(rset.getString("PROD_CATEGORY"));
				ap.setProdPrice(rset.getInt("PROD_PRICE"));
				ap.setProdAmount(rset.getInt("PROD_AMOUNT"));
								
				list.add(ap);
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

	public int insertProduct(Connection conn, adminProduct ap) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("insertProduct");

		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ap.getProdName());
			pstmt.setString(2, ap.getProdDetail());
			pstmt.setString(3, ap.getProdCategory());
			pstmt.setInt(4, ap.getProdPrice());
			pstmt.setInt(5, ap.getProdAmount());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}		
		
		return result;
	}

	public int insertAttachment(Connection conn, Attachment at) {
		
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("insertAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}	
		
		return result;
	}

	public adminProduct selectProduct(Connection conn, int prodNo) {
		
		adminProduct ap = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("selectProduct");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prodNo);
			
			rset = pstmt.executeQuery();
			if(rset.next()) {
				/*
				 * ap = new adminProduct( rset.getString("PROD_NAME"),
				 * rset.getString("PROD_DETAIL"), rset.getString("PROD_CATEGORY"),
				 * rset.getInt("PROD_PRICE"), rset.getInt("PROD_AMOUNT")
				 * 
				 * );
				 */
				
				ap = new adminProduct();
				
				ap.setProdNo(rset.getInt("PROD_NO"));
				ap.setProdCategory(rset.getString("PROD_CATEGORY"));
				ap.setProdName(rset.getString("PROD_NAME"));
				ap.setProdPrice(rset.getInt("PROD_PRICE"));
				ap.setProdAmount(rset.getInt("PROD_AMOUNT"));
				ap.setProdDetail(rset.getString("PROD_DETAIL"));
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return ap;
	}

	public Attachment selectAttachment(Connection conn, int prodNo) {
		
		Attachment at = null;		
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prodNo);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				at = new Attachment();
				
				at.setFileNo(rset.getInt("FILE_NO"));
				at.setOriginName(rset.getString("ORIGIN_NAME"));
				at.setChangeName(rset.getString("CHANGE_NAME"));
				at.setFilePath(rset.getString("FILE_PATH"));
				at.setRef_pno(rset.getInt("REF_PNO"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return at;
	}

	public int updateProduct(Connection conn, adminProduct ap) {
		
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updateProduct");		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, ap.getProdName());
			pstmt.setString(2, ap.getProdDetail());
			pstmt.setString(3, ap.getProdCategory());
			pstmt.setInt(4, ap.getProdPrice());
			pstmt.setInt(5, ap.getProdAmount());
			pstmt.setInt(6, ap.getProdNo());
			
			result = pstmt.executeUpdate();			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updateAttachment");		
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getRef_pno());
			pstmt.setInt(5, at.getFileNo());
			
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}	
		
		return result;
	}

	public int deleteProduct(Connection conn, int prodNo) {

		int result =0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("deleteProduct");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, prodNo);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}	
		
		return result;
	}

	public int deleteAttachment(Connection conn, int ref_pno) {		
		
		int result =0;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("deleteAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ref_pno);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}	
		
		return result;
	}

}
