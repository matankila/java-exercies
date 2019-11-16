import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Matan keler, ilrd.
 */
public class GatewayServer {
    private static final int ERROR_RES = 400;
    private final HttpServerImp<HttpExchange, JsonObject> server;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /************************************************* CTOR *************************************************/
    public GatewayServer(String ip, int port) throws IOException {
        Objects.requireNonNull(ip, "ip cant be null");
        server = new HttpServerImp<>(ip, port , this::validate);
        server.setExecutor(pool);
    }

    /********************************************** API Method **********************************************/
    public void startServer() {
        server.createContext("/matan", (httpExchange)-> {
            Objects.requireNonNull(httpExchange, "cant be null");
            try {
                JsonObject jsonObject = server.applyCons(httpExchange);
                SingletonCommandFactory.getInstance().execute(jsonObject, httpExchange);
            }
            catch (IOException e) {
                e.getCause();
            }
        });

        server.start();
    }

    /****************************************** NON API Method **********************************************/
    private void sendResponse(HttpExchange httpExchange, String msg, int statusCode) throws IOException {
        OutputStream response  = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(statusCode, msg.length());
        response.write(msg.getBytes());
        response.close();
    }

    private JsonObject validate(HttpExchange httpExchange) {
        if ((httpExchange).getRequestMethod().equals("GET")) { return null; }

        JsonObject jsonObject = (JsonObject) new JsonParser().parse(new InputStreamReader(httpExchange.getRequestBody()));
        if (null == jsonObject.get("command type") || null == jsonObject.get("company") ||
                     null == jsonObject.get("product") || null == jsonObject.get("sn")) {
            try {
                sendResponse(httpExchange, "JSON doesnt contain any mandatory fields", ERROR_RES);
            } catch (IOException ex) {
                ex.getCause();
            }
        }

        return jsonObject;
    }

    public static void main(String[] args) throws IOException{
        GatewayServer g = new GatewayServer("127.0.0.1", 12345);
        g.startServer();
    }
}
