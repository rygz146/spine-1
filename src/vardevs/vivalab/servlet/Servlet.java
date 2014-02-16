package vardevs.vivalab.servlet;

import vardevs.vivalab.meta.Movie;
import vardevs.vivalab.service.CouchService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Just a basic servlet.
 */
public class Servlet extends HttpServlet {
    CouchService patata = new CouchService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        for (Movie movie : patata.allMovies()) {
            resp
                .getWriter()
                .println(movie+"<br/>");
        }
    }

    @Override
    protected void doPut
        (HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doPut(req, resp);
    }

    @Override
    protected void doPost
        (HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete
        (HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doDelete(req, resp);
    }

}
