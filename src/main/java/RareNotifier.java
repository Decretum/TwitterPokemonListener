import java.util.HashSet;
import java.util.Set;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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
        boolean sendTexts = true;
        String ACCOUNT_SID = "AC14ba3ae443a97034d8dfe5cccf3138f0";
        String AUTH_TOKEN = "de68549b4b6cc5b3cab1fdf99bad70e3";
        String RECEIVER_NUMBER = "+15105574226";
        String TWILIO_NUMBER = "+15106069380";
        HashSet<String> alreadyFound = new HashSet<>();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        if (sendTexts) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        }
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
                        if (info != null) {
                            if (info.contains("Unown")) {
                                SwingTest.showNotification(info, 1);
                            } else {
                                SwingTest.showNotification(info, 0);
                            }
                            if (sendTexts) {
                                Message message = Message.creator(new PhoneNumber(RECEIVER_NUMBER), new PhoneNumber(TWILIO_NUMBER), info).create();
                                System.out.println("DEBUG: Sent a text message for " + info);
                            }
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