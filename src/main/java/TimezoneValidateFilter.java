import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req,
                            HttpServletResponse resp,
                            FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");

        if (timezone == null || !isValidTimezone(timezone)) {
            resp.setStatus(400);
            resp.getWriter().write("Invalid timezone");
        } else {
            chain.doFilter(req, resp);
        }
    }

    private boolean isValidTimezone(String timezone) {
        try {
            timezone = timezone.replace(" ", "+");
            ZoneId.of(timezone);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
