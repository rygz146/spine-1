package vardevs.vivalab.spine;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

public class Start {
  public static void main(String[] args) throws Exception {
    Options options = new Options();
    options.addOption("p", "port", true, "port to use for serving the application");
    options.addOption("r", "remote", true, "remote git repo to serve");

    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cli = parser.parse(options, args);

      if (cli.hasOption("p") && cli.hasOption("r")) {
        int port = Integer.parseInt(cli.getOptionValue("p"), 10);
        Spine app = new Spine(port, cli.getOptionValue("r"));

        Runnable shutdown = () -> app.down();
        Runtime.getRuntime().addShutdownHook(new Thread(shutdown));

        app.up();
      } else {
        System.out.println("[SPINE] port or remote git repo missing");
      }
    } catch (ParseException e) {
      System.out.println("[SPINE] Command line options failed to parse" + e.getMessage());
    }
  }
}
