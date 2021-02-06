package chipercu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class HtmlParser extends Config {
    private static String address;


    public HtmlParser(String address) {
        this.address = address;
    }

    public String getDocument(User user) throws IOException {
        String url;
        if (address.equals("")) {
            url = "https://www." + DEFAULT_URL_ADDRESS + "/";
        } else {
            url = "https://www." + address + "/";
        }

        Document document = null;
        try {
            document = Jsoup.parse(new URL(url), 10000);

            DBConnection.addLog(user, url, "successful");
        } catch (IOException e) {
            System.out.println("Невозможно откртыть указаную страничку!");
            DBConnection.addLog(user, url, "unsuccessful");
            startParser(user);
        }
        return document.text();
    }

    public static void startParser(User user) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Если не будет введен адрес то по умолчанию будет спарсена страничка \"" +
                Config.DEFAULT_URL_ADDRESS + "\", адрес по умолчанию указывается в файле " +
                Config.CONFIG_FILE_PATH);
        System.out.println("Введите адрес страницы : ");

        address = bufferedReader.readLine();

        HtmlParser.analizator(address, user);
        while (true) {
            System.out.println("Введите адрес страницы или введите \"q\" для завершения програмы :");
            String nextAction = bufferedReader.readLine();
            if (!nextAction.equalsIgnoreCase("q")) {
                HtmlParser.analizator(nextAction, user);
            } else {
                System.exit(0);
            }
        }
    }


    public static void analizator(String address, User user) throws IOException {
        String text = null;
        text = new HtmlParser(address).getDocument(user).replaceAll(symbol, "").toUpperCase();
        //System.out.println("Текст странички: " + text);
        List<String> wordList = new ArrayList<>();
        assert text != null;
        for (String word : text.split(" ")) {
            wordList.addAll(Arrays.asList(word.split("-")));
        }

        Map<String, Integer> frequency = dublicat(wordList);

        frequency.forEach((key, value) ->
                System.out.println(key + " - " + value));
    }

    private static Map<String, Integer> dublicat(List<String> inputList) {
        Map<String, Integer> map = new HashMap<>();
        for (String str : inputList) {
            map.merge(str, 1, Integer::sum);
        }
        return map;
    }

}
