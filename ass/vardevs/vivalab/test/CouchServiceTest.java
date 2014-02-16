package vardevs.vivalab.test;

import vardevs.fw.test.Assert;
import vardevs.vivalab.meta.Movie;
import vardevs.vivalab.service.CouchService;

import java.util.List;

/**
 *
 */
public class CouchServiceTest {
    public static void couchIntegrationTest() {
        CouchService couch = new CouchService();

        List<Movie> movies = couch.allMovies();
        assert !movies.isEmpty();
        assert Assert.notEquals("The movie array should not be null", movies, null);
    }

    public static void main(String[] args) {
        // Poor man's test runner
        couchIntegrationTest();
    }
}


