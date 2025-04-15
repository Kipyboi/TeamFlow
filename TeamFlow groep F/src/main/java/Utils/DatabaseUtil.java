package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://jjjq5.h.filess.io:3307/TeamFlow_subjecttax";
    private static final String USER = "TeamFlow_subjecttax";
    private static final String PASS = "3580404a02bb7f876ea7ca2f84fa6aa87fa95cea";

    public static Connection getConnection () throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
