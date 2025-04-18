package Domein;

import java.sql.SQLException;
import java.util.Scanner;

public interface IZoek {
//    void zoek(Scanner scanner) throws SQLException;
    default void zoek(Scanner scanner) throws SQLException {
        System.out.println("you're not allowed to do this");
    }

}
