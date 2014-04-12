package vardevs.vivalab;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.markdown4j.Markdown4jProcessor;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.*;
import java.util.*;

/**
 * The compiler, indeed, just does what you might expect. It takes all
 * vertabrae and combines them into a spine. I call it, the spine binder.
 */
public class SpineBinder
{

    public static final VelocityEngine velocity_engine = new VelocityEngine();

    /**
     * Compile takes an absolute path of the app and returns an absolute path
     * of the resulting Vertabrae.
     *
     * @param abs_path_from the absolute path where the resources of an application
     *                      lives.
     * @param abs_path_to   the absolute path to where the application should be
     *                      built to and served from.
     * @return              the absolute path to serve the application from
     */
    public static String
        compile(Path abs_path_from, Path abs_path_to)
    {
        Markdown4jProcessor md = new Markdown4jProcessor();
        velocity_engine.init();

        try {
            Path dir = FileSystems.getDefault().getPath
                (abs_path_from.toAbsolutePath().toString(), "content");

            Map<String, Vertabrae> file_names =
                extract_files(dir);

            generate_markup(abs_path_to, md, file_names);
            static_files_copy(abs_path_from, abs_path_to);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return abs_path_to.toAbsolutePath().toString();
    }

    private static Map<String, Vertabrae> extract_files
        (Path dir)
        throws IOException
    {
        Map<String, Vertabrae> file_names = new TreeMap<>();

        DirectoryStream<Path> directory_stream =
            java.nio.file.Files.newDirectoryStream(dir, "*.md");

        for
            (Path from_file : directory_stream)
        {

            Vertabrae vertabrae = new Vertabrae
                (from_file);

            file_names.put(vertabrae.file_name(), vertabrae);

        }
        directory_stream.close();
        return file_names;
    }

    private static void generate_markup
        (Path abs_path_to, Markdown4jProcessor md, Map<String, Vertabrae> file_names)
        throws IOException
    {
        Function<Vertabrae, String> title =
            Vertabrae::title;

        Map<String, String> toc = Maps.transformValues(file_names, title);

        for
            (String key : file_names.keySet())
        {
            Vertabrae vertabrae = file_names.get(key);

            ByteSource bs = Files.asByteSource(vertabrae.path().toFile());
            String html = md.process(bs.openBufferedStream());
            String template = "default.vm";

            VelocityContext velocity_context = new VelocityContext();
            velocity_context.put("name", vertabrae.title());
            velocity_context.put("content", html);
            velocity_context.put("pages", toc);

            String page = merge(template, velocity_context);

            File to_file = new File
                (abs_path_to
                    + File.separator
                    + vertabrae.file_name() + ".html");

            Files.write(page, to_file, Charsets.UTF_8);
        }
    }

    private static void static_files_copy
        (Path abs_path_from, Path abs_path_to)
        throws IOException
    {
        Path from = FileSystems.getDefault()
            .getPath
                (abs_path_from.toAbsolutePath().toString(), "static");

        Path to = FileSystems.getDefault()
            .getPath
                (abs_path_to.toAbsolutePath().toString(), "static");

        java.nio.file.Files.walkFileTree
            (from,
                EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE, new CopyDirectoryVisitor
                    (from, to)
            );
    }

    private static String merge
        (String template, VelocityContext velocity_context)
    {
        Template def = velocity_engine.getTemplate
            (FileSystems
                .getDefault()
                .getPath("resources", "templates", template)
                .toString()
            );

        StringWriter sw = new StringWriter();
        def.merge(velocity_context, sw);
        return sw.toString();
    }
}
