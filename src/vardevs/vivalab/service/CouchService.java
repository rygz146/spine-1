package vardevs.vivalab.service;

import org.eclipse.jetty.client.api.ContentResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import vardevs.fw.api.ApiAgent;
import vardevs.vivalab.meta.Movie;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CouchService {

    List<Movie> movies = new ArrayList<>();

    public List<Movie> allMovies()
    {
        if (movies.isEmpty()) {
            movies = fromService();
        }

        return movies;
    }

    private List<Movie> fromService()
    {
        List<Movie> result = new ArrayList<>();
        String apiKey = "620b04f28d3f4bed9db2f05ca083d730";
        String apiCall = "media.list";

        try {
            URL url = new URL
                ("http://cerebralcortex:5050/api/"+apiKey+"/"+apiCall);

            ContentResponse content = ApiAgent.fetch(url.toURI());

            JSONObject jsonObject = new JSONObject
                (content.getContentAsString());

            JSONArray jsonArray = jsonObject.getJSONArray("movies");

            Integer i;
            for (i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Movie movie = new Movie
                    (title(obj)
                        , year(obj)
                        , synopsis(obj)
                        , tagline(obj)
                    );

                result.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String synopsis(JSONObject obj) {
        return obj
            .getJSONObject("library")
            .getString("plot");
    }

    private int year(JSONObject obj) {
        return obj
            .getJSONObject("library")
            .getInt("year");
    }

    private String tagline(JSONObject obj) {
        return obj
            .getJSONObject("library")
            .getString("tagline");
    }

    private String title(JSONObject obj) {
        return obj
            .getJSONObject("library")
            .getJSONObject("info")
            .getString("original_title");
    }
}
