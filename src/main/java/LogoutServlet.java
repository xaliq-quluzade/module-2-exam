import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Cookie userCookie =
                SecurityUtil.getCookieByName(req.getCookies(), "username");

        if (Objects.nonNull(userCookie)) {
            userCookie.setValue("");
            userCookie.setMaxAge(0);
            resp.addCookie(userCookie);
        }
    }

}
