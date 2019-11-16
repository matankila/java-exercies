import il.co.ilrd.crud.CRUD;

import java.sql.*;

public class Ex2 {
    private String table;
    private Connection connection;

/*    public Ex2(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public Integer create(String entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(entity)) {
            return preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.getErrorCode();
        }

        return null;
    }

    @Override
    public String read(Integer specialKey) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Statement preparedStatement = connection.createStatement()) {
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM " + table + " WHERE code = " + specialKey);
            while (resultSet.next()) {
                for(int i = 1; i < resultSet.getMetaData().getColumnCount(); ++i) {
                    stringBuilder.append(resultSet.getString(i) + " ");
                }
            }

            return stringBuilder.toString();
        }
        catch (SQLException e) {
            e.getErrorCode();
        }

        return null;
    }

    @Override
    public void update(Integer specialKey, String entity) {

    }

    @Override
    public void delete(Integer specialKey) {

    }

    public static void main(String[] args) throws SQLException{
        Ex2 ex = new Ex2("jdbc:mysql://localhost/Computer_Firm" ,"matan", "1q2w3e4r");
        //ex.create("insert into PC value (14, 1233, 123, 12, 5, '12x', 999);");
        ex.setTable("PC");
        System.out.println(ex.read(1));
    }*/
}
