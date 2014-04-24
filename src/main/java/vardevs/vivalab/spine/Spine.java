package vardevs.vivalab.spine;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import vardevs.vivalab.spine.servlet.AdminServlet;

import java.io.IOException;

public class Spine {

    private final Server server;
    private final ServletContextHandler context;

    public Spine(int port) throws IOException {
        server = new Server(port);
        context = new ServletContextHandler
            (ServletContextHandler.SESSIONS);

        String path = this.getClass().getClassLoader().getResource("public").toExternalForm();

        context.setResourceBase(path);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        context.addServlet(new ServletHolder(new AdminServlet()), "/admin");
    }

    public void up() throws Exception {
        if (!server.isStarted()) {
            System.out.println("[SPINE] Starting application at " + server.getURI());
            server.start();
            server.join();
        }
    }

    public void down() throws Exception {
        server.stop();
    }

}
