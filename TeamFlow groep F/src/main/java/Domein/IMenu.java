package Domein;

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
    default void BerichtAanmaken() {
        System.out.println("you're not allowed to do this");
    }
    default void GaNaar() {
        System.out.println("you're not allowed to do this");
    }
    default void GaTerug() {
        System.out.println("you're not allowed to do this");
    }
    default void TeamAanmaken() {
        System.out.println("you're not allowed to do this");
    }
    default void EpicAanmaken() {
        System.out.println("you're not allowed to do this");
    }
    default void UserstoryAanmaken() {
        System.out.println("you're not allowed to do this");
    }
    default void TaakAanmaken() {
        System.out.println("you're not allowed to do this");
    }
}
