package vardevs.vivalab;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 */
public class Vertabrae {
    private final String file_name;
    private final String title;
    private final Path path;

    public Vertabrae
        (Path from_file)
        throws IOException
    {
        this.file_name =
            Files.getNameWithoutExtension(from_file.toFile().getAbsolutePath());

        this.title = Files.readFirstLine
            (from_file.toFile(), Charsets.UTF_8);

        this.path = from_file;
    }

    String file_name() {
        return this.file_name;
    }

    String title() {
        return this.title;
    }

    Path path() {
        return this.path;
    }
}
