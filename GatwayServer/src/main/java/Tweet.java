import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.io.*;

public class Tweet implements Requests {
    private static final int ERROR_RES = 401;

    @Override
    public void send(JsonObject jsonData, HttpExchange httpExchange) throws IOException {
        validateRequest(jsonData, httpExchange);

        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(jsonData.get("Consumer Key").getAsString())
                    .setOAuthConsumerSecret(jsonData.get("Consumer Secret").getAsString())
                    .setOAuthAccessToken(jsonData.get("Access Token").getAsString())
                    .setOAuthAccessTokenSecret(jsonData.get("Token Secret").getAsString());
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            Status status = twitter.updateStatus(jsonData.get("status").getAsString());
            sendResponse(httpExchange, "tweet success!", 200);
        }
        catch (TwitterException e) {
            sendResponse(httpExchange, "tweet failed!", e.getStatusCode());
        }
    }

    private void validateRequest(JsonObject jsonData, HttpExchange httpExchange) throws IOException {
        if (null == jsonData.get("Consumer Key") || null == jsonData.get("Consumer Secret") ||
                null == jsonData.get("Access Token") || null == jsonData.get("Token Secret") ||
                null == jsonData.get("status")) {
            sendResponse(httpExchange, "tweet failed!, missing token", ERROR_RES);
        }
    }
}