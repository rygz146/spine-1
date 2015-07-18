package vardevs.vivalab.spine;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;

import vardevs.vivalab.spine.servlet.AdminServlet;
import vardevs.vivalab.spine.SpineBinder;

public class Spine {

    private final Server server;
    private final ServletContextHandler context;

    public Spine(int port, String repo) throws IOException, GitAPIException, URISyntaxException {
        System.out.println("[SPINE] Using port: " + port);
        System.out.println("[SPINE] Attempting to serve remote repo: " + repo);

        server = new Server(port);
        context = new ServletContextHandler
            (ServletContextHandler.SESSIONS);

        Path localPath = Files.createTempFile("temp", "");
        Files.delete(localPath);

        Path publicPath = Paths.get("public");
        
        if (!Files.exists(publicPath)) {
          publicPath = Files.createDirectory(publicPath);
        }

        Git.cloneRepository()
          .setURI(repo)
          .setDirectory(localPath.toFile())
          .call().close();

        String servePath = SpineBinder.compile(localPath, publicPath);

        context.setResourceBase(servePath);
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
