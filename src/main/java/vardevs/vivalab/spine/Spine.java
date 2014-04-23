package vardevs.vivalab.spine;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import vardevs.vivalab.spine.servlet.AdminServlet;

import java.io.IOException;

/**
 * Spine
 */
public class Spine {

    private final Server server;
    private final ServletContextHandler context;

    public Spine(int port, String path) throws IOException {
        server = new Server(port);
        context = new ServletContextHandler
            (ServletContextHandler.SESSIONS);

        context.setResourceBase(path);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        context.addServlet(new ServletHolder(new AdminServlet()), "/admin");
    }

    public void up() throws Exception {
        if (!server.isStarted()) {
            server.start();
            server.join();
        }
    }

    public void down() throws Exception {
        server.stop();
    }

}
