import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class Time extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        String timezone = req.getParameter("timezone");
        timezone = timezoneValidation(timezone, req, resp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ZoneId zoneId = ZoneId.of(timezone);
        String time = OffsetDateTime.now(zoneId).format(formatter);

        Context context = new Context(req.getLocale());
        context.setVariable("currentTime", time);
        context.setVariable("timezone", timezone);
        engine.process("time", context, resp.getWriter());
    }

    private String timezoneValidation(String timezone, HttpServletRequest req, HttpServletResponse resp){
        if (timezone==null) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("timezone".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            timezone = "UTC";
        } else {
            timezone = timezone.replace(" ", "+");
            resp.addCookie(new Cookie("timezone", timezone));
        }
        return timezone;
    }
}

