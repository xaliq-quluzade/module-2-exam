import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var writer = resp.getWriter();
        String actualUsername = req.getParameter("user");
        String actualPass = req.getParameter("pass");

        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    select * from "public".person where username = ? and password = ?
                    """);
            ps.setString(1, actualUsername);
            ps.setString(2, actualPass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cookie cookieUser = new Cookie("username", actualUsername);
                cookieUser.setMaxAge(300);
                resp.addCookie(cookieUser);
                writer.write("User logged in  with username : " + actualUsername);
            } else {
                writer.write("Incorrect username or password");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
