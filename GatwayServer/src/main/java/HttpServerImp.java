import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class HttpServerImp<T, R> {
    private final HttpServer httpServer;
    private final Function<T, R> function;

    public HttpServerImp(String ip, int port, Function<T, R> function) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(ip, port), 0);
        this.function = function;
    }

    public HttpContext createContext(String context, HttpHandler handler) {
        return httpServer.createContext(context, handler);
    }

    public void setExecutor(Executor var1) {
        httpServer.setExecutor(var1);
    }

    public R applyCons(T httpExchange) {
        return function.apply(httpExchange);
    }

    public void start() {
        httpServer.start();
    }
}
