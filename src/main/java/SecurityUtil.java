import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class SecurityUtil {
    private SecurityUtil() {
        throw new IllegalStateException();
    }

    public static Cookie getCookieByName(Cookie[] cks, String cookieName) {
        return Optional.ofNullable(cks)
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> cookie.getName().equalsIgnoreCase(cookieName))
                .filter(cookie -> Objects.nonNull(cookie.getValue()) && !cookie.getValue().isBlank())
                .findFirst()
                .orElse(null);
    }

    public static HttpSession getSessionByAttributeName(HttpSession httpSession, String attributeName) {
        if (httpSession != null && attributeName != null) {
            Object attributeValue = httpSession.getAttribute(attributeName);
            if (attributeValue != null && !attributeValue.toString().trim().isEmpty()) {
                return httpSession;
            }
        }
        return null;
    }
}
