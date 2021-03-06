package com.kh.campaign.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.campaign.model.service.CampService;
import com.kh.campaign.model.vo.Campaign;
import com.kh.member.model.vo.UserVO;

/**
 * Servlet implementation class CampaignMypageServlet
 */
@WebServlet("/mypage.cam")
public class CampaignMypageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CampaignMypageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userId = ((UserVO)request.getSession().getAttribute("loginUser")).getUser_id();
		
		ArrayList<Campaign> joinList = new CampService().selectJoinList(userId);

		
		if(joinList != null) {
			request.setAttribute("joinList", joinList);
		}else {
			request.setAttribute("msg", "참여 캠페인 리스트를 불러오지 못했습니다.");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
		if(joinList.isEmpty()) {
			request.setAttribute("message", "참여 캠페인이 없습니다.");
		}
		
		request.getRequestDispatcher("views/campaign/campaignMypageView.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
