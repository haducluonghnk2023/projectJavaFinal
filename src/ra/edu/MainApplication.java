package ra.edu;

import ra.edu.business.model.account.Account;
import ra.edu.presentation.auth.AuthUI;

import java.util.Scanner;

public class MainApplication {
    public static Scanner sc = new Scanner(System.in);
    public static Account currentUser;
    public static int choice;
    public static void main(String[] args) {
        AuthUI.login();
    }
}
