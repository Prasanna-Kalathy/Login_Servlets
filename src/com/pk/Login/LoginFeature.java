package com.pk.Login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/loginServlet")
public class LoginFeature extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;
	PreparedStatement stmt;

	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			ServletContext context = config.getServletContext();
			con = DriverManager.getConnection(context.getInitParameter("dbUrl"), context.getInitParameter("dbUser"),
					context.getInitParameter("dbPass"));
			stmt = con.prepareStatement("select * from user where email=? and password=?");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String pass = request.getParameter("pass");

		try {
			stmt.setString(1, email);
			stmt.setString(2, pass);

			ResultSet rs = stmt.executeQuery();

			RequestDispatcher rd = request.getRequestDispatcher("homeServlet");

			if (rs.next()) {
				request.setAttribute("message", "Welcome to Sathiyam cinemas " + email);
				rd.forward(request, response);
			} else {
				rd = request.getRequestDispatcher("Login.html");
				rd.include(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
