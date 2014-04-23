Spine
=====

We get so much for "free" when building nowadays that my day consists of
consuming rather than producing.

When you sell your craft by time then every unit has to be spent wisely.

This is my escape and has only one intent:

    Be as framework-less as possible.

A short escape if there ever was one. Shy of 2 months later there are 7 dependencies
and this is without the aid of any "modern" dependency management tool. It is a
slippery slope, glazed with the blood of good intention.

And a couple of weeks later we have Maven .. Slippery slope, indeed.

These are the spoils of war:

* javax.servlet-api-3.1.0.jar
* jetty-all-9.1.1.v20140108.jar
* velocity-1.7.jar
* markdown4j-2.2.jar
* guava-16.0.1.jar
* commons-collections-3.2.1.jar
* commons-lang-2.4.jar

By end of 2014 all of them will be replaced with hand-rolled counterparts.

Hah, now that I've pulled in guava for "something neat" I've pretty much
screwed the pooch already.

This holds true then as now:

    Stop work time and begin playtime. Make something cool.

Usage
=====

Well, now we are talking.

The layout I use is,

* <project_name>
** public/
*** <generated web root>
** resources/
*** content/
**** <content_x>.md
*** static/
**** <static_files>
*** templates/
**** <default.vm>
**** <content_x>.vm
** src/
*** main/
**** java/
***** vardevs.veng.Start
** target/
*** <project_name>-<version>.jar


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

That's all there's to it.

/v.
