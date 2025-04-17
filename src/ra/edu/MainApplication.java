package ra.edu;

import ra.edu.presentation.auth.AuthUI;

import java.util.Scanner;

public class MainApplication {
    public static Scanner sc = new Scanner(System.in);
    public static int choice;
    public static void main(String[] args) {
        AuthUI.login();
    }
}
