package vardevs.fw.test;

import vardevs.fw.util.Log;

/**
 * This is what I want, this is what I got
 */
public class Assert {
    public static boolean equals(String message, Object expected, Object actual) {
        boolean equivalent = expected.equals(actual);

        if (!equivalent) {
            Log.print("Objects failed Object.equals() comparison with the message:\n" + message);
        }

        return equivalent;
    }

    public static boolean notEquals(String message, Object expected, Object actual) {
        boolean equivalent = expected.equals(actual);

        if (equivalent) {
            Log.print("Objects failed Object.equals() comparison with the message:\n" + message);
        }

        return equivalent;
    }
}
