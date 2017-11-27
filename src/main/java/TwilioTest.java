import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.testng.annotations.Test;

// Run this to check if Twilio is working for you I guess

public class TwilioTest {

    @Test
    public void textMessagesWork() {
        String ACCOUNT_SID = "AC14ba3ae443a97034d8dfe5cccf3138f0";
        String AUTH_TOKEN = "de68549b4b6cc5b3cab1fdf99bad70e3";
        String RECEIVER_NUMBER = "+15105574226";
        String TWILIO_NUMBER = "+15106069380";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String info = "a pokemon appear lmao";

        Message.creator(new PhoneNumber(RECEIVER_NUMBER), new PhoneNumber(TWILIO_NUMBER), info).create();
        System.out.println("DEBUG: Sent a text message for " + info);
    }

}
