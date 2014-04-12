package vardevs.vivalab;

import com.google.common.base.Charsets;
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
 *
 */
public class AppCompiler
{

    public static final VelocityEngine velocity_engine = new VelocityEngine();

    public static String
        compile(Path abs_path_from, Path abs_path_to)
    {
        Markdown4jProcessor md = new Markdown4jProcessor();
        velocity_engine.init();

        try {
            Path from = java.nio.file.FileSystems.getDefault()
                .getPath
                    (abs_path_from.toAbsolutePath().toString(), "static");

            Path to = java.nio.file.FileSystems.getDefault()
                .getPath
                    (abs_path_to.toAbsolutePath().toString(), "static");

            java.nio.file.Files.walkFileTree
                (from,
                    EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                    Integer.MAX_VALUE, new CopyDirectoryVisitor
                        (from, to)
                );

            Path dir = FileSystems.getDefault().getPath(abs_path_from.toAbsolutePath().toString(), "content");

            DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(dir);
            for (Path from_file : stream) {

                if
                    (from_file.toFile().isDirectory()
                    || !Files.getFileExtension
                        (from_file.toFile().getName())
                        .equals("md")
                    )
                {
                    continue;
                }

                ByteSource bs = Files.asByteSource(from_file.toFile());
                String html = md.process(bs.openBufferedStream());
                String template = "default.vm";
                VelocityContext velocity_context = new VelocityContext();

                Map<String, String> file_names = new TreeMap<>();

                DirectoryStream<Path> directory_stream = java.nio.file.Files.newDirectoryStream(dir, "*.md");
                for (Path path : directory_stream)
                {
                    String file_name = Files.getNameWithoutExtension(path.toString());
                    if (!"index".equals(file_name)) {
                        file_names.put(file_name, Files.readFirstLine(path.toFile(), Charsets.UTF_8));
                    }
                }
                directory_stream.close();

                velocity_context.put("name", Files.readFirstLine(from_file.toFile(), Charsets.UTF_8));
                velocity_context.put("content", html);
                velocity_context.put("pages", file_names);

                String page = merge(template, velocity_context);

                File to_file = new File
                    (abs_path_to
                        + File.separator
                        + Files.getNameWithoutExtension(from_file.toFile().getName()) + ".html");

                Files.write(page, to_file, Charsets.UTF_8);
            }
            stream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return abs_path_to.toAbsolutePath().toString();
    }

    private static String merge(String template, VelocityContext velocity_context) {
        Template def = velocity_engine.getTemplate(FileSystems.getDefault().getPath("resources", "templates", template).toString());
        StringWriter sw = new StringWriter();
        def.merge(velocity_context, sw);
        return sw.toString();
    }
}
