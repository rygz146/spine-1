package vardevs.vivalab.spine;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.nio.file.*;

import java.net.URISyntaxException;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

public class Spine {

    private final Server server;
    private final ServletContextHandler context;
    private final Path localPath = Files.createTempFile("temp", "");

    public Spine(int port, String repo)
            throws IOException, GitAPIException, URISyntaxException {
        System.out.println("[SPINE] Using port: " + port);
        System.out.println("[SPINE] Attempting to serve remote repo: " + repo);

        server = new Server(port);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Path publicPath = Paths.get("public");

        if (!Files.exists(publicPath)) {
            publicPath = Files.createDirectory(publicPath);
        }

        Files.delete(localPath);
        Git.cloneRepository()
                .setURI(repo)
                .setDirectory(localPath.toFile())
                .call().close();

        String servePath = SpineBinder.compile(localPath, publicPath);

        context.setResourceBase(servePath);
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new DefaultServlet()), "/*");

        server.setHandler(context);
    }

    public void up() {
        System.out.println("[SPINE] Starting application at " + server.getURI());
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void down() {
        System.out.println("[SPINE] Stopping application...");
        try {
            Files.walkFileTree(localPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });

            if (server.isStarted()) {
                server.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
