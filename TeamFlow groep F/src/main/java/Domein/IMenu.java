package Domein;

import java.sql.SQLException;
import java.util.Scanner;

public interface IMenu {
    default void menu() {
        System.out.println("you're not allowed to do this");
    }
    default void GebruikerAanmaken() {
        System.out.println("you're not allowed to do this");
    }
    default void GebruikerInloggen() {
        System.out.println("you're not allowed to do this");
    }
    default void BerichtAanmaken(Scanner scanner) throws SQLException {
        System.out.println("you're not allowed to do this");
    }
    default void GaNaar() {
        System.out.println("you're not allowed to do this");
    }
    default void GaTerug() {
        System.out.println("you're not allowed to do this");
    }
    default void TeamAanmaken(Scanner scanner)throws SQLException {
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
