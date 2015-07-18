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

Hah, now that I've pulled in guava for "something neat" I've pretty much
screwed the pooch already.

Installation
============

You need to build spine before it's available in your repository.

    $ git clone git@github.com:varl/spine.git &&  cd spine && gradle build && gradle install

Usage
=====

`Start.java` in the app looks like this:

    package vardevs.veng;

    import vardevs.vivalab.spine.Spine;
    import vardevs.vivalab.spine.SpineBinder;

    import java.nio.file.FileSystems;
    import java.nio.file.Path;

    public class Start {
        public static void main
            (String[] args)
            throws Exception
        {
            Spine app = new Spine(8080);
            app.up();
        }
    }
    
    $ cd <project_name> && gradle build && gradle shadowJar

To run your web application is just a command away:

    $ java -jar build/libs/<project_name>-<version_string>-all.jar
    
Set up a reverse proxy and supervisor, and you are done.

/v.
