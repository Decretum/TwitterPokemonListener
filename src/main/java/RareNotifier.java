import java.util.HashSet;
import java.util.Set;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class RareNotifier {

    private static String[] wantedCities = {"Fremont", "Milpitas", "Hayward", "Newark", "Union C"};
    private static String feedUrl = "https://twitter.com/AlamedaRareMons";
    private static boolean showPopup = true;
    private static boolean sendTexts = false;
    private static String RECEIVER_NUMBER = "+15105574226";
    private static String TWILIO_NUMBER = "+15106069380";
    private static String ACCOUNT_SID = "";
    private static String AUTH_TOKEN = "";
    private static HashSet<String> alreadyFound = new HashSet<>();

    private static WebDriver driver;

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        driver = new ChromeDriver();

        String citiesString = "";
        for (String city : wantedCities) {
            citiesString += city + "\n";
        }
        Config.showConfig(feedUrl, citiesString, RECEIVER_NUMBER, showPopup, sendTexts);

        while (true) {
            driver.get(feedUrl);
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
                            if (showPopup) {
                                if (info.contains("Unown")) {
                                    Notification.showNotification(info, 1);
                                } else {
                                    Notification.showNotification(info, 0);
                                }
                            }
                            if (sendTexts) {
                                Message.creator(new PhoneNumber(RECEIVER_NUMBER), new PhoneNumber(TWILIO_NUMBER), info).create();
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

    private static String parseInfoFromTweet(String originalTweet, String[] cities) {
        // If the tweet has a city listed, return some information about it.
        // Otherwise return null.
        for (String city : cities) {
            if ((!city.equals("")) && originalTweet.contains(city)) {
                String result = city;
                String tweet = new String(originalTweet);
                if (tweet.contains(" sign\" />")) {
                    tweet = tweet.split("<a href=")[0];
                    tweet = tweet.split(" sign\" />")[1];
                } else {
                    // Without this check, genderless Pokemon like Unown crash the program lmao
                    tweet = tweet.split("<a href=")[0];
                    tweet = tweet.split(city + " ")[1];
                }
                System.out.println("DEBUG: " + tweet);
                result = result + " " + tweet;
                return result + " " + parseGoogleMapsUrlFromTweet(originalTweet);
            }
        }
        return null;
    }

    private static String parseGoogleMapsUrlFromTweet(String tweet) {
        tweet = tweet.split("target=\"_blank\" title=\"")[1];
        tweet = tweet.split("\"")[0];
        return tweet;
    }

    public static void updateSettings(String feed, String cities, String receiver, boolean showNotifications, boolean sendMessages) {
        feedUrl = feed;
        RECEIVER_NUMBER = receiver;
        showPopup = showNotifications;
        sendTexts = sendMessages;
        wantedCities = cities.split("\n");
    }

}