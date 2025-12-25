import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class org_register_validate extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();

        String nm   = req.getParameter("org_nm");
        String eml  = req.getParameter("org_eml");
        String phn  = req.getParameter("org_contact");
        String pw   = req.getParameter("org_reg_pass");
        String desc = req.getParameter("org_desc");
        String sq   = req.getParameter("org_security");

        // ✅ UPDATED PART (Checkbox values)
        String[] servArr = req.getParameterValues("org_serv[]");
        String serv = "";
        if (servArr != null) {
            serv = String.join(", ", servArr);
        }

        pw1.println("<html><head><title>Registration Status</title>");

        pw1.println("<style>");
        pw1.println("body { font-family: Arial; background: linear-gradient(135deg,#667eea,#764ba2); display:flex; justify-content:center; align-items:center; height:100vh; }");
        pw1.println(".status-box { background:#fff; padding:40px; width:400px; border-radius:12px; box-shadow:0 12px 30px rgba(0,0,0,0.25); text-align:center; }");
        pw1.println(".success { color:#2ecc71; } .error { color:#e74c3c; }");
        pw1.println("a { display:inline-block; padding:12px 25px; background:#667eea; color:#fff; text-decoration:none; border-radius:6px; }");
        pw1.println("</style></head><body><div class='status-box'>");

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "system", "Manager");

            Statement stmt = con.createStatement();

            String q1 = "INSERT INTO organizer_registration VALUES('"
                    + nm + "','" + eml + "','" + phn + "','" + pw + "','"
                    + serv + "','" + desc + "','" + sq + "')";

            int x = stmt.executeUpdate(q1);

            if (x > 0) {
                pw1.println("<h3 class='success'>Registration Successful!</h3>");
                pw1.println("<a href='org_login.html'>Go to Login Page</a>");
            } else {
                pw1.println("<h3 class='error'>Registration Failed.</h3>");
            }

            con.close();

        } catch (Exception e) {
            pw1.println("<h3 class='error'>Error: " + e + "</h3>");
        }

        pw1.println("</div></body></html>");
        pw1.close();
    }
}
