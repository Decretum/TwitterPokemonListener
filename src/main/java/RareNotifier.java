import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RareNotifier {

    private static String[] wantedCities = {"Fremont:", "Milpitas:", "Hayward:", "Newark:", "Union C:"};
    private static String[] feeds = {"https://twitter.com/AlamedaRareMons"};

    private static WebDriver driver;
//    private static String[] feeds = {"https://twitter.com/AlamedaUltraMon"};
//    private static String[] feeds = {"https://twitter.com/AlamedaMons"}; // 100 IV mons
//    private static String[] feeds = {"https://twitter.com/AlamedaUnowns"};

    public static void main(String[] args) throws Exception {
        HashSet<String> alreadyFound = new HashSet<>();

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        driver = new ChromeDriver();

        while (true) {
            driver.get(feeds[0]);
            String source = driver.getPageSource();
            boolean notable = false;
            for (String city : wantedCities) {
                if (source.contains(city)) {
                    notable = true;
                    break;
                }
            }
            if (notable) {
                // Something popped up in a city that's somewhat close
                Set<String> results = parseInfo(source, wantedCities);
                if (!alreadyFound.containsAll(results)) {
                    results.removeAll(alreadyFound);
                    alreadyFound.addAll(results);
                    results.forEach(info -> {
                        if (info != null && info.contains("Unown")) {
                            SwingTest.showNotification(info, 1);
                        } else {
                            SwingTest.showNotification(info, 0);
                        }
                    });
                }
            }
            Thread.sleep(15000);
        }
    }

    public static void closeBrowser() {
        driver.close();
    }

    private static Set<String> parseInfo(String source, String[] cities) {
        Set<String> results = new HashSet<String>();
        String[] tweets = source.split("<div class=\"js-tweet-text-container\">");
        for (int x = 0; x < tweets.length; x++) {
            // Only get the content inside the div
            tweets[x] = tweets[x].split("</div>")[0].trim();
            results.add(parseInfoFromTweet(tweets[x], cities));
        }
        return results;
    }

    private static String parseInfoFromTweet(String tweet, String[] cities) {
        // If the tweet has a city listed, return some information about it.
        // Otherwise return null.
        for (String city : cities) {
            if (tweet.contains(city)) {
                String result = city;
                tweet = tweet.split("<a href=")[0];
                tweet = tweet.split(" sign\" />")[1];
                System.out.println("DEBUG: " + tweet);
                result = result + tweet;
                return result;
            }
        }
        return null;
    }

}