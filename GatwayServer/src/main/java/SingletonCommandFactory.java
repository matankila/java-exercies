import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.HashMap;

public class SingletonCommandFactory {
    private static final SingletonCommandFactory CMD = new SingletonCommandFactory();
    private final HashMap<String, Requests> httpRequestsMap = new HashMap<>();

    /****************************************** CTOR ********************************************/
    private SingletonCommandFactory() {
        httpRequestsMap.put("tweet", new Tweet());
    }

    /****************************************** API Methods ********************************************/
    public void execute(JsonObject jsonObject, HttpExchange httpExchange) throws IOException{
        String key = httpExchange.getRequestMethod();
        if (key.equals("GET") && httpRequestsMap.containsKey(key)) {
            httpRequestsMap.get(key).send(jsonObject, httpExchange);
        }
        else {
            String commandType = jsonObject.get("command type").getAsString();
            if (httpRequestsMap.containsKey(commandType)) {
                httpRequestsMap.get(commandType).send(jsonObject.getAsJsonObject("data"), httpExchange);
            }
        }
    }

    public static SingletonCommandFactory getInstance() {
        return CMD;
    }
}