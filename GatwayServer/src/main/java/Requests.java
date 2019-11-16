import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.util.Objects;

public interface Requests {
    void send(JsonObject json, HttpExchange httpExchange) throws IOException;

    default void sendResponse(HttpExchange httpExchange, String msg, int statusCode) throws IOException {
        Objects.requireNonNull(httpExchange, "httpExchnage cant be null");
        Objects.requireNonNull(msg, "msg of response cant be null");
        OutputStream response  = httpExchange.getResponseBody();
        httpExchange.sendResponseHeaders(statusCode, msg.length());
        response.write(msg.getBytes());
        response.close();
    }
}