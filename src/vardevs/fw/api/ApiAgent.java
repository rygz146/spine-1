package vardevs.fw.api;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import vardevs.fw.util.Log;

import java.net.URI;

/**
 * Static API Agent to handle external requests.
 */
public class ApiAgent {

    static HttpClient client = new HttpClient();

    public static void initialize()
    {
        client.setFollowRedirects(false);
        try {
            client.start();
        } catch (Exception e) {
            Log.print("The HTTP Client failed to start.");
            e.printStackTrace();
        }
    }

    public static ContentResponse fetch(URI uri)
        throws Exception
    {
        if (!client.isStarted()) {
            initialize();
        }

        return client.GET(uri.toString());
    }

}
