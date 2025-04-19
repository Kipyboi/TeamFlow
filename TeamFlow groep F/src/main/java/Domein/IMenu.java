package Domein;

import java.sql.SQLException;
import java.util.Scanner;

public interface IMenu {
    default void menu(Scanner scanner) throws SQLException {
        System.out.println("you're not allowed to do this");
    }
    default void BerichtAanmaken(Scanner scanner) throws SQLException {
        System.out.println("you're not allowed to do this");
    }
    default void EpicAanmaken(Scanner scanner)throws SQLException {
        System.out.println("you're not allowed to do this");
    }
    default void UserstoryAanmaken(Scanner scanner) throws SQLException {
        System.out.println("you're not allowed to do this");
    }
    default void TaakAanmaken(Scanner scanner) throws SQLException {
        System.out.println("you're not allowed to do this");
    }
}
