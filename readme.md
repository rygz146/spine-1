Spine
=====

Jump: [installation](#installation), [usage](#usage)

We get so much for "free" when building nowadays that my day consists of
consuming rather than producing.

When you sell your craft by time then every unit has to be spent wisely.

This is my escape and has only one intent:

    Be as framework-less as possible.

These are the spoils of war:

* javax.servlet-api-3.1.0.jar
* jetty-all-9.1.1.v20140108.jar
* velocity-1.7.jar
* markdown4j-2.2.jar
* guava-16.0.1.jar
* commons-collections-3.2.1.jar
* commons-lang-2.4.jar

By end of 2014 all of them will be replaced with hand-rolled counterparts. _\[ed: hah!\]_

Hah, now that I've pulled in guava for "something neat" I've pretty much
screwed the pooch already.

Installation
============

I don't like context-clipping so here's the full pom.xml for a site that wants to use spine.

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>

        <groupId>group_name</groupId>
        <artifactId>project_name</artifactId>
        <version>version_string</version>
        <packaging>jar</packaging>

        <dependencies>
            <dependency>
                <groupId>vardevs.vivalab</groupId>
                <artifactId>spine</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.1</version>
                    <configuration>
                        <source>1.8</source>
                        <target>1.8</target>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-shade-plugin</artifactId>
                    <version>2.2</version>
                    <executions>
                        <execution>
                            <phase>package</phase>
                            <goals>
                                <goal>shade</goal>
                            </goals>
                            <configuration>
                                <transformers>
                                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                        <mainClass>vardevs.veng.Start</mainClass>
                                    </transformer>
                                </transformers>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>

    </project>

You need to build spine with maven before it's available in your repository. I haven't put it on central yet.

Usage
=====

Well, now we are talking.

The layout I use is,

- \<project_name\>
    - public/
        - \<generated web root\>
    - resources/
        - content/
            - \<content_x\>.md
        - static/
            - \<static_files\>
        - templates/
            - \<default.vm\>
            - <content_x>.vm
    - src/
        - main/
            - java/
                - vardevs.veng.Start
    - target/
        - \<project_name\>-\<version\>.jar


Start.java looks like this:

    package vardevs.<project_name>;

    import vardevs.vivalab.spine.Spine;
    import vardevs.vivalab.spine.SpineBinder;

    import java.nio.file.FileSystems;
    import java.nio.file.Path;

    public class Start {
        public static void main
            (String[] args)
            throws Exception
        {
            Path from = FileSystems.getDefault().getPath("resources");
            Path to = FileSystems.getDefault().getPath("public");

            String compiled_app = SpineBinder.compile(from, to);
            Spine app = new Spine(8080, compiled_app);
            app.up();
        }
    }

This holds true then as now:

    Stop work time and begin playtime. Make something cool.

/v.
