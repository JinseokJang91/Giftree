package com.kh.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.Validation.Validation;
import com.kh.member.model.service.UserService;
import com.kh.member.model.vo.UserVO;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserService us;
	Validation vd;
	boolean regCheck = false;
	public UserServlet() {
		super();
		us = new UserService();
		vd = new Validation();
	}

	protected void processing(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("command").equals("join")) {
			join(request, response);
		} else if (request.getParameter("command").equals("login")) {
			login(request, response);
		} else if (request.getParameter("command").equals("logout")) {
			logout(request, response);
		} else if (request.getParameter("command").equals("userUpdate.do")) {
			userUpdate(request, response);
		} else if (request.getParameter("command").equals("findId")) {
			findId(request, response);
		} else if (request.getParameter("command").equals("userDelete")) {
			userDelete(request, response);
		} else if (request.getParameter("command").equals("findPw")) {
			findPw(request, response);
		} else if (request.getParameter("command").equals("pwCheck")) {
			pwCheck(request, response);
		} else if (request.getParameter("command").equals("findPwUpdate")) {
			findPwUpdate(request, response);
		} else if (request.getParameter("command").equals("idCheck")) {
			idCheck(request, response);
		} else if (request.getParameter("command").equals("userPwUpdate")) {
			userPwUpdate(request, response);
		} else if (request.getParameter("command").equals("userPwUpdateAction")) {
			userPwUpdateAction(request, response);

		}
	}

	private void idCheck(HttpServletRequest request, HttpServletResponse response) {
		String user_id = request.getParameter("user_id");
		UserVO vo = new UserVO(user_id);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			if (us.idCheck(vo) != 0) {

				out.print("<span style = 'color:red;'> ?????? ???????????? ????????? ?????????</span>");
			} else {
				out.print("<span style = 'color:blue;'> ?????? ????????? ????????? ?????????");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processing(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		processing(request, response);
	}

	protected void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String user_pwCheck = request.getParameter("user_pwCheck");
		String user_name = request.getParameter("user_name");
		String user_no = request.getParameter("user_no") + "-" + request.getParameter("user_no1");
		String gender = request.getParameter("gender");
		String address = request.getParameter("userPost") + "&" + request.getParameter("userAddress") + "&"
				+ request.getParameter("userAddress1");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");

		UserVO vo = new UserVO(user_id, user_pw, user_pwCheck, user_name, user_no, gender, address, phone, email);

		String msg = vd.validationMsg(vo);

		if (!msg.equals("")) {
			request.setAttribute("msg", msg);
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		} else {
			int result = us.join(vo);
			if (result == 1) {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println(
						"<script>alert('??????????????? ??????????????? ????????????????????? ????????????????????? ???????????????');location.href='views/member/login.jsp';</script>");
				out.flush();
				out.close();

			} else {
				request.setAttribute("msg", "???????????? ");
				RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
				view.forward(request, response);
			}
		}
	}

	protected void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");

		String pw = us.loginCheck(user_id);

		if (pw.equals("")) {
			request.setAttribute("msg", "???????????? ?????? ????????? ?????????");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		} else if (!user_pw.equals(pw)) {
			request.setAttribute("msg", "??????????????? ??????????????? ");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		} else {
			HttpSession session = request.getSession();
			UserVO vo = us.getUser(user_id);
			session.setAttribute("loginUser", vo);
			RequestDispatcher view = request.getRequestDispatcher("index.jsp");
			view.forward(request, response);

		}

	}

	protected void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		RequestDispatcher view = request.getRequestDispatcher("index.jsp");
		view.forward(request, response);

	}

	protected void userUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String address = request.getParameter("userPost") + "&" + request.getParameter("userAddress") + "&"
				+ request.getParameter("userAddress1");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		UserVO vo = new UserVO(user_id, user_pw, address, phone, email);
		String pw = us.loginCheck(user_id);
		if (!user_pw.equals(pw)) {
			request.setAttribute("msg", "???????????? ??????????????? ???????????? ???????????? ");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		} else {
			String msg = vd.updateValidationMsg(vo);

			if (!msg.equals("")) {
				request.setAttribute("msg", msg);
				RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
				view.forward(request, response);
			} else {
				int result = us.userUpdate(vo);
				if (result == 1) {
					HttpSession session = request.getSession();
					vo = us.getUser(user_id);
					session.setAttribute("loginUser", vo);
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter out = response.getWriter();

					out.println("<script>alert('???????????? ????????? ???????????????  ?????????????????????!');location.href='index.jsp';</script>");
					out.flush();
					out.close();

				} else {
					request.setAttribute("msg", "???????????? ");
					RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
					view.forward(request, response);
				}
			}

		}

	}

	protected void findId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user_name = request.getParameter("user_name");
		String user_no = request.getParameter("user_no") + "-" + request.getParameter("user_no1");
		String findId = us.findId(user_name, user_no);
		if (!findId.equals("")) {
			request.setAttribute("findId", findId);
			RequestDispatcher view = request.getRequestDispatcher("views/member/findIdAction.jsp");
			view.forward(request, response);
		} else {
			request.setAttribute("msg", "???????????? ??????????????? ???????????? ???????????? ");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		}

	}

	protected void userDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String pw = us.loginCheck(user_id);
		if (pw.equals("")) {
			out.println("<script>alert('???????????? ???????????? ????????????');history.back();</script>");
			out.flush();
			out.close();
		} else if (!user_pw.equals(pw)) {
			out.println("<script>alert('??????????????? ???????????????');history.back();</script>");
			out.flush();
			out.close();
		} else {
			int result = us.userDelete(user_id);
			if (result == 1) {
				HttpSession session = request.getSession();
				session.invalidate();
				out.println("<script>alert('??????????????? ?????? ???????????????');location.href='index.jsp';</script>");
				out.flush();
				out.close();

			} else {
				out.println("<script>alert('?????? ??????');history.back();</script>");
				out.flush();
				out.close();
			}

		}

	}

	protected void findPw(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_name = request.getParameter("user_name");
		String user_id = request.getParameter("user_id");
		String user_no = request.getParameter("user_no") + "-" + request.getParameter("user_no1");
		String findPw = us.findPw(user_name, user_id, user_no);
		if (!findPw.equals("")) {
			request.setAttribute("user_id", user_id);
			request.setAttribute("msg", "????????? ??????????????? ????????? ????????? ");
			RequestDispatcher view = request.getRequestDispatcher("views/member/findPwAction.jsp");
			view.forward(request, response);
		} else {
			request.setAttribute("msg", "???????????? ??????????????? ?????????????????? ");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		}

	}

	protected void pwCheck(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String PwCheck = us.PwCheck(user_id);
		if (!PwCheck.equals(user_pw)) {
			request.setAttribute("msg", "??????????????? ???????????? ????????????");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		} else {
			HttpSession session = request.getSession();
			UserVO vo = us.getUser(user_id);
			session.setAttribute("loginUser", vo);
			RequestDispatcher view = request.getRequestDispatcher("views/member/userUpdate1.jsp");
			view.forward(request, response);

		}
	}

	protected void userPwUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String PwCheck = us.PwCheck(user_id);
		if (!PwCheck.equals(user_pw)) {
			request.setAttribute("msg", "??????????????? ???????????? ????????????");
			RequestDispatcher view = request.getRequestDispatcher("views/member/error.jsp");
			view.forward(request, response);
		} else {
			request.setAttribute("user_id", user_id);
			RequestDispatcher view = request.getRequestDispatcher("views/member/userPwUpdateAction.jsp");
			view.forward(request, response);

		}
	}

	protected void findPwUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_pw = request.getParameter("user_pw");
		String user_pwCheck = request.getParameter("user_pwCheck");
		String user_id = request.getParameter("user_id");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String userPwCheck = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
		regCheck = Pattern.matches(userPwCheck, user_pw);
		if(regCheck == false) {
			out.println("<script>alert('??????????????? ????????? ????????? ???????????? 8~20????????? ???????????? ?????????');history.back();</script>");
			out.flush();
			out.close();
		}
		if (user_pw.equals(user_pwCheck)) {
			int result = us.findPwUpdate(user_pw, user_id);
			if (result == 1) {
				out.println("<script>alert('??????????????? ???????????????  ?????????????????????!');window.close();</script>");
				out.flush();
				out.close();
			} else {
				out.println("<script> alert('???????????? ');history.back();</script>");
				out.flush();
				out.close();
			}
		} else {
			out.println("<script> alert('??????????????? ???????????? ???????????? ');history.back();</script>");
			out.flush();
			out.close();
		}

	}

	protected void userPwUpdateAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String user_pw = request.getParameter("user_pw");
		String user_pwCheck = request.getParameter("user_pwCheck");
		String user_id = request.getParameter("user_id");
		System.out.println(user_pw + user_pwCheck + user_id +  "333");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();		
		
		String userPwCheck = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
		regCheck = Pattern.matches(userPwCheck, user_pw);
		if(regCheck == false) {
			out.println("<script>alert('??????????????? ????????? ????????? ???????????? 8~20????????? ???????????? ?????????');history.back();</script>");
			out.flush();
			out.close();
		}
		if (user_pw.equals(user_pwCheck)) {
			int result = us.findPwUpdate(user_pw, user_id);
			if (result == 1) {				
				out.println("<script>alert('??????????????? ???????????????  ?????????????????????!');window.close();</script>");
				out.flush();
				out.close();
			} else {
				out.println("<script> alert('???????????? ');history.back();</script>");
				out.flush();
				out.close();
			}
		} else {
			out.println("<script> alert('??????????????? ???????????? ???????????? ');history.back();</script>");
			out.flush();
			out.close();
		}

	}

}
