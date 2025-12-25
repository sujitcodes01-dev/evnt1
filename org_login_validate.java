import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class org_login_validate extends HttpServlet
{
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
       res.setContentType("text/html");
       PrintWriter pw1=res.getWriter();
       String email=req.getParameter("log_eml");
       String pass=req.getParameter("log_pass");
       
       try{
           Class.forName("oracle.jdbc.driver.OracleDriver");
           Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "Manager");
           Statement stmt=con.createStatement();
           String q1="select * from organizer_registration where email='"+email+"' and password='"+pass+"'";
           
           ResultSet rs=stmt.executeQuery(q1);
           if(rs.next()){
               pw1.println("Welcome "+rs.getString(1));
           }
           else{
               pw1.println("Login Failed!!!!");
           }
           con.close();
       }
       catch(Exception e){
           pw1.println(e);
       }
    }
}
