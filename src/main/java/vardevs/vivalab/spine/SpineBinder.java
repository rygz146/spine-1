package vardevs.vivalab.spine;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.markdown4j.Markdown4jProcessor;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class SpineBinder
{
    public static final VelocityEngine velocity_engine = new VelocityEngine();
    private static final Markdown4jProcessor md = new Markdown4jProcessor();

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
        System.out.println("[SPINE] Template path: " +  abs_path_from);
        velocity_engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, abs_path_from.resolve("templates").toString());
        velocity_engine.init();

        System.out.println("[SPINE] From path: " +  abs_path_from);
        System.out.println("[SPINE] To path: " + abs_path_to);
        
        try {
            Path dir = abs_path_from.resolve("content");

            Map<String, Vertabrae> file_names =
                extract_files(dir);

            generate_markup(abs_path_from, abs_path_to, file_names);
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
            System.out.println("[SPINE] Vertabrae file name: "+ vertabrae.file_name());
        }
        directory_stream.close();
        return file_names;
    }

    private static void generate_markup
        (Path abs_path_from, Path abs_path_to, Map<String, Vertabrae> file_names)
        throws IOException
    {
        Function<Vertabrae, String> title =
            Vertabrae::title;

        Map<String, String> toc = Maps.transformValues(file_names, title);

        Set<String> files = file_names.keySet();

        Set<String> filtered_files = files
                .stream()
                .filter(file -> !file.contains("_"))
                .collect(Collectors.toSet());

        for
            (String key : filtered_files)
        {
            Vertabrae vertabrae = file_names.get(key);

            ByteSource bs = Files.asByteSource(vertabrae.path().toFile());
            String html = md.process(bs.openBufferedStream());

            Set<String> relevant_files = files.stream().filter(file -> file.contains(key+"_")).collect(Collectors.toSet());

            String template = pick_template(abs_path_from, vertabrae.file_name());

            VelocityContext velocity_context = new VelocityContext();
            velocity_context.put("name", vertabrae.title());
            velocity_context.put("content", html);
            velocity_context.put("pages", toc);

            Map<String, String> content_files = content_for(relevant_files, file_names);

            for (String index : content_files.keySet()) {
                velocity_context.put("content_"+index, content_files.get(index));
            }

            System.out.println("[SPINE] Template: " + template);
            
            String page = merge(template, velocity_context);

            File to_file = new File
                (abs_path_to
                    + File.separator
                    + vertabrae.file_name() + ".html");

            Files.write(page, to_file, Charsets.UTF_8);
        }
    }

    private static Map<String, String> content_for
            (Set<String> relevant_files, Map<String, Vertabrae> file_names)

    {

        Map<String, String> result = new HashMap<>();
        for (String file: relevant_files) {
            String index = file.substring(file.length()-1);

            Vertabrae vertabrae = file_names.get(file);
            ByteSource bs = Files.asByteSource(vertabrae.path().toFile());
            String html;
            try {
                html = md.process(bs.openBufferedStream());
                result.put(index, html);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String pick_template(Path abs_path_from, String name) {
        String template = name+".vm";
        String val = "default.vm";

        if
            (java.nio.file.Files.exists(abs_path_from.resolve("templates").resolve(template)))
        {
            val = template;
        }

        return val;
    }

    private static String merge
        (String template, VelocityContext velocity_context)
    {
        Template def = velocity_engine.getTemplate(template);

        StringWriter sw = new StringWriter();
        def.merge(velocity_context, sw);
        return sw.toString();
    }

    private static void static_files_copy
        (Path abs_path_from, Path abs_path_to)
        throws IOException
    {
        Path from = abs_path_from.resolve("static");
        Path to = abs_path_to.resolve("static");

        java.nio.file.Files.walkFileTree
            (from,
                EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE, new CopyDirectoryVisitor
                    (from, to)
            );
    }
}
