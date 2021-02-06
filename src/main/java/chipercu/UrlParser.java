package chipercu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlParser {

    private static final String URL_ADDRESS = "simbirsoft.com";
    private  String address;

    public UrlParser(String address) {
        this.address = address;
    }

    public URLConnection getConnection() throws IOException {

        String url;
        if (address.equals("")) {
            url = "https://www." + URL_ADDRESS + "/";
        } else {
            url = "https://www." + address + "/";
        }
        return new URL(url).openConnection();
    }
}
