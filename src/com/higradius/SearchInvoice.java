package com.higradius;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



@WebServlet("/SearchInvoice")
public class SearchInvoice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SearchInvoice() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			Connection con = DBConnection.createConnect();
			
			String searchInvoice = request.getParameter("searchInvoice");
			String page = request.getParameter("page");

			
			Statement st = con.createStatement();
			String sql_statement = "SELECT * FROM mytable WHERE invoice_id=" + "'"+searchInvoice+"'";// + "%' LIMIT " + page +"," + rowCount;
			ResultSet rs = st.executeQuery(sql_statement);

			ArrayList<Response> data = new ArrayList<>();
			while(rs.next()) {
				Response inv = new Response();
				inv.setCustomerName(rs.getString("name_customer"));
				inv.setCustomerId(rs.getString("cust_number"));
				inv.setInvoiceId(rs.getString("invoice_id"));

				inv.setDueDate(rs.getString("due_in_date"));

				inv.setInvoiceAmt(rs.getFloat("total_open_amount"));
				
				data.add(inv);
			}
			
			
			Gson gson = new GsonBuilder().serializeNulls().create();
			String invoices  = gson.toJson(data);
			response.setContentType("application/json");
			try {
				response.getWriter().write(invoices);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			rs.close();
			st.close();
			con.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
