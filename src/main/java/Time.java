import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/")
public class Time extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezone = req.getParameter("timezone");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ");
        String time;
        ZoneId zoneId;

        if (timezone==null) {
            timezone = "UTC";
        } else {
            timezone = timezone.replace(" ", "+");
        }

        zoneId = ZoneId.of(timezone);
        time = OffsetDateTime.now(zoneId).format(formatter) + timezone;
        resp.getWriter().write(time);
        resp.getWriter().close();
    }
}

