import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import il.co.ilrd.crud.SQL.CRUDSQL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * @author Matan Keler, Ilrd.
 */
@WebServlet("/Iots")
public class Iots extends HttpServlet {
    private static String PATHCRUD;
    private static CRUDSQL cs;

    /********************************** API Methods **********************************/
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cs = new CRUDSQL(config.getInitParameter("conn"), config.getInitParameter("user"), config.getInitParameter("pass"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = convertRequestToJson(req);
        JsonObject dataJson = jsonRequest.getAsJsonObject("data");
        cs.create(dataJson.get("entity").getAsString());
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = convertRequestToJson(req);
        JsonObject dataJson = jsonRequest.getAsJsonObject("data");
        cs.delete(dataJson.get("key").getAsString(), dataJson.get("table").getAsString());
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = convertRequestToJson(req);
        JsonObject dataJson = jsonRequest.getAsJsonObject("data");
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("ret_val", cs.read(dataJson.get("key").getAsString(), dataJson.get("table").getAsString()));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = convertRequestToJson(req);
        JsonObject dataJson = jsonRequest.getAsJsonObject("data");
        cs.update(dataJson.get("key").getAsString(), dataJson.get("entity").getAsString(), dataJson.get("entity").getAsString());
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /********************************** Non API Methods **********************************/
    private JsonObject convertRequestToJson(HttpServletRequest request) throws IOException {
        String jsonString =  new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        JsonObject jsonRequest = new GsonBuilder().create().fromJson(jsonString, JsonObject.class);

        return jsonRequest;
    }
}
