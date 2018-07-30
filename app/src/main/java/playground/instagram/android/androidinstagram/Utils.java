package playground.instagram.android.androidinstagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static String streamToString(InputStream is) throws IOException {
        if (is == null) return "";

        StringBuilder sb = new StringBuilder();
        String line;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException ex) {
            ex.printStackTrace();;
        } finally {
            is.close();
        }

        return sb.toString();
    }
}
