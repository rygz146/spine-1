package vardevs.vivalab;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import vardevs.vivalab.servlet.Servlet;

/**
 * Start
 */
public class Start
{
    public static void main
        (String[] args)
        throws Exception
    {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler
            (ServletContextHandler.SESSIONS);

        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet
            (new ServletHolder
                (new Servlet()), "/movies");

        server.start();
        server.join();
    }
}
