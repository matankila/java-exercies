import com.google.gson.JsonObject;
import il.co.ilrd.logmonitor.LogMonitor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Matan Keler, Ilrd.
 */
public class TomCatCRUD {
    private final HashMap<String, String> hashMap = new HashMap<>();
    private final LogMonitor logMonitor;
    private final String serverURI;
    private final String fileToListenPath;
    private final HttpPost request;

    {
        hashMap.put("ENTRY_MODIFY", "POST");
        hashMap.put("ENTRY_CREATE", "PUT");
        hashMap.put("ENTRY_DELETE", "DELETE");
    }

    /**
     * TomCatCRUD CTOR.
     * @param fileToListenPath the path for the file that will be monitored.
     * @param serverURI the URI to send request to.
     * @throws MalformedURLException Bad URI.
     * @throws IOException Server is not available.
     */
    public TomCatCRUD(String fileToListenPath, String serverURI) throws MalformedURLException, IOException {
        validate(serverURI);
        logMonitor = new LogMonitor(fileToListenPath);
        this.serverURI = serverURI;
        request = new HttpPost(serverURI);
        this.fileToListenPath = fileToListenPath;
    }

    /************************************ Public API methods ****************************************/
    /**
     * start monitoring of the file, must use this fucntion to use TomcatCRUD.
     */
    public void startMonitoring() {
        logMonitor.addPropertyChangeListener((evt)-> {
            String evtNewValue = (String) evt.getNewValue();
            String event = evtNewValue.substring(1, evtNewValue.indexOf('>'));
            String eventContent = evtNewValue.substring(evtNewValue.indexOf('>') + 1);
            JsonObject jsonObject = setJson(hashMap.get(event), "ilrd", "poster", 123);
            JsonObject dataJson = setDataJson(eventContent);
            jsonObject.add("data", dataJson);
            handleHttpRequests(hashMap.get(event), jsonObject, serverURI);
        });

        try {
            logMonitor.watch();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /************************************ Private API methods ****************************************/
    /**
     * set JsonObject.
     * @param cmd commandType
     * @param company .
     * @param prod .
     * @param serial .
     * @return Json Object filled with all mendatory data.
     */
    private JsonObject setJson(String cmd, String company, String prod, int serial) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commandtype", "post");
        jsonObject.addProperty("company", "ilrd");
        jsonObject.addProperty("product", "logmonitor");
        jsonObject.addProperty("sn", 1234);

        return jsonObject;
    }

    /**
     * set JsonData parameters.
     * @param entity row to be passed.
     * @return JsonData filled with data.
     */
    private JsonObject setDataJson(String entity) {
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("entity", entity);

        return jsonData;
    }

    /**
     * handle any http request.
     * @param event request method to be handled.
     * @param jsonObject containing the data to pass.
     * @param serverURI .
     * @throws UnsupportedOperationException if http request method isnt POST.
     */
    private void handleHttpRequests(String event, JsonObject jsonObject, String serverURI) {
        if (!Objects.equals(event, HttpRequests.POST.getStr())) { throw new UnsupportedOperationException(); }

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
        }
        catch (ClientProtocolException ex) {
            System.out.println("cant find Client");
        }
        catch (IOException e) {
            e.getCause();
        }
    }

    /**
     * validate the URI is good & that the server is up.
     * @param serverURI .
     * @throws MalformedURLException if the URI is bad.
     * @throws IOException if the server is down.
     */
    private void validate(String serverURI) throws MalformedURLException, IOException {
        URL url = new URL(serverURI);
        URLConnection myURLConnection = url.openConnection();
        myURLConnection.connect();
    }

    private enum HttpRequests {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private final String str;

        private HttpRequests(String str) {
            this.str = str;
        }

        private String getStr() {
            return str;
        }
    }

    public static void main(String[] args) {
        try {
            TomCatCRUD tc = new TomCatCRUD("/var/log/syslog", "http://localhost:8080/iots");
            tc.startMonitoring();
        }
        catch (MalformedURLException e) {
            System.out.println("bad URI");
        }
        catch (IOException ee) {
            System.out.println("server not active");
        }
    }
}
