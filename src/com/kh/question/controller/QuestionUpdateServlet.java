package com.kh.question.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.question.model.service.QuesService;
import com.kh.question.model.vo.QNA;

/**
 * Servlet implementation class QuestionUpdateServlet
 */
@WebServlet("/update.que")
public class QuestionUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int qno = Integer.parseInt(request.getParameter("qno"));
		String qnaCategory = request.getParameter("qnaCategory");
		String qnaTitle = request.getParameter("qnaTitle");
		String qnaContent = request.getParameter("qnaContent").replaceAll("\n", "<br>");
		
		QNA qna = new QNA(qno, qnaTitle, qnaContent, qnaCategory);
		
		int result = new QuesService().updateQuestion(qna);
		
		if(result > 0) {
			request.getSession().setAttribute("msg", "문의사항 수정이 완료되었습니다.");
		}else {
			request.getSession().setAttribute("msg", "문의사항 수정에 실패했습니다.");
		}
		
		response.sendRedirect("list.que");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
