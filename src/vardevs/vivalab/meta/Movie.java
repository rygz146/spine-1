package vardevs.vivalab.meta;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 *
 */
@Immutable
public class Movie {
    String title;
    Integer year;
    String synopsis;
    String tagline;

    public Movie(String title, Integer year, String synopsis, String tagline) {
        this.title = title;
        this.year = year;
        this.synopsis = synopsis;
        this.tagline = tagline;
    }

    public String title() {
        return title;
    }

    public Integer year() {
        return year;
    }

    public String synopsis() {
        return synopsis;
    }

    public String tagline() {
        return tagline;
    }

    public String toString() {
        return
            this.title()
                + " ("+this.year()+") "
                +" - "+ this.tagline();
    }
}
