import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class forgot_password_validate extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        res.setContentType("text/html");
        PrintWriter pw1 = res.getWriter();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:XE", "system", "Manager");

            Statement stmt = con.createStatement();

            String email = req.getParameter("forgot_eml");
            String sq = req.getParameter("forgot_sq");

            /* ======================
               STEP 1: VERIFY EMAIL + SECURITY ANSWER
               ====================== */
            if (email != null && sq != null) {

                ResultSet rs = stmt.executeQuery(
                    "select * from organizer_registration where email='" +
                    email + "' and security_answer='" + sq + "'");

                if (rs.next()) {

                    pw1.println("<!DOCTYPE html>");
                    pw1.println("<html>");
                    pw1.println("<head>");
                    pw1.println("<title>Reset Password</title>");

                    /* ===== MODERN CSS ===== */
                    pw1.println("<style>");
                    pw1.println("body {");
                    pw1.println("  margin: 0;");
                    pw1.println("  font-family: 'Segoe UI', Arial;");
                    pw1.println("  background: linear-gradient(120deg, #667eea, #764ba2);");
                    pw1.println("  height: 100vh;");
                    pw1.println("  display: flex;");
                    pw1.println("  justify-content: center;");
                    pw1.println("  align-items: center;");
                    pw1.println("}");

                    pw1.println(".card {");
                    pw1.println("  background: #ffffff;");
                    pw1.println("  width: 360px;");
                    pw1.println("  padding: 30px;");
                    pw1.println("  border-radius: 10px;");
                    pw1.println("  box-shadow: 0 10px 25px rgba(0,0,0,0.2);");
                    pw1.println("}");

                    pw1.println(".card h2 {");
                    pw1.println("  text-align: center;");
                    pw1.println("  margin-bottom: 20px;");
                    pw1.println("  color: #333;");
                    pw1.println("}");

                    pw1.println(".card input {");
                    pw1.println("  width: 100%;");
                    pw1.println("  padding: 12px;");
                    pw1.println("  margin: 10px 0;");
                    pw1.println("  border: 1px solid #ccc;");
                    pw1.println("  border-radius: 6px;");
                    pw1.println("  font-size: 14px;");
                    pw1.println("}");

                    pw1.println(".card input[type='submit'] {");
                    pw1.println("  background: #667eea;");
                    pw1.println("  color: white;");
                    pw1.println("  border: none;");
                    pw1.println("  cursor: pointer;");
                    pw1.println("  font-weight: bold;");
                    pw1.println("}");

                    pw1.println(".card input[type='submit']:hover {");
                    pw1.println("  background: #5a67d8;");
                    pw1.println("}");
                    pw1.println("</style>");

                    pw1.println("</head>");
                    pw1.println("<body>");

                    pw1.println("<div class='card'>");
                    pw1.println("<h2>Reset Password</h2>");

                    pw1.println("<form method='post' action='./forgot_password_validate'>");
                    pw1.println("<input type='hidden' name='email' value='" + email + "'>");
                    pw1.println("<input type='password' name='new_pass' placeholder='New Password' required>");
                    pw1.println("<input type='password' name='confirm_pass' placeholder='Confirm Password' required>");
                    pw1.println("<input type='submit' value='Update Password'>");
                    pw1.println("</form>");

                    pw1.println("</div>");
                    pw1.println("</body>");
                    pw1.println("</html>");

                } else {
                    pw1.println("<h3 style='color:red;'>Invalid Email or Security Answer!</h3>");
                }

                con.close();
                return;
            }

            /* ======================
               STEP 2: UPDATE PASSWORD
               ====================== */
            String newPass = req.getParameter("new_pass");
            String confirmPass = req.getParameter("confirm_pass");
            String hiddenEmail = req.getParameter("email");

            if (newPass != null && confirmPass != null && hiddenEmail != null) {

                if (!newPass.equals(confirmPass)) {
                    pw1.println("<h3 style='color:red;'>Passwords do not match!</h3>");
                    return;
                }

                stmt.executeUpdate(
                    "update organizer_registration set password='" +
                    newPass + "' where email='" + hiddenEmail + "'");

                pw1.println("<h3 style='color:green;'>Password updated successfully!</h3>");
                pw1.println("<a href='org_login.html'>Login</a>");

                con.close();
            }

        } catch (Exception e) {
            pw1.println(e);
        }
    }
}
