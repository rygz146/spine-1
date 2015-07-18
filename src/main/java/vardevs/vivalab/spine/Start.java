package vardevs.vivalab.spine;

import vardevs.vivalab.spine.Spine;

public class Start {
    public static void main
            (String[] args)
            throws Exception
    {
        // use args to get port and remote repo?
        Spine app = new Spine(8003, "https://github.com/varl/vlv-spine.git");
        app.up();
    }
}
