/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author pridhvi
 */
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStoreHelper {

    public static boolean exitMenu(boolean quit, String inpStr, int inpInt) {
        if (inpInt == -99) {
            try {
                if (inpStr.trim().equalsIgnoreCase("q")) {
                    quit = true;
                } else {
                    System.out.println("Please input a valid option from the menu");
                }
            } catch (Exception e) {
                System.out.println("Unexpected Exception");
            }
        }
        return quit;
    }

    public static int returnIntegerFromInput(String inpStr) {
        int option = -100;
        try {
            option = Integer.parseInt(inpStr);
            if (option == 1 || option == 2) {
                return option;

            } else {
                throw new Exception("Invalid Integer  inputted!!!");
            }

        } catch (NumberFormatException ne) {
            option = -99;
        } catch (Exception e) {
            System.out.println("Please input a valid option from the menu");

        }
        return option;
    }

    public static String scanFromCommandLine(String inputMessage) {
        System.out.print(inputMessage);
        Scanner scanner = new Scanner(System.in);
        String inputStr = scanner.nextLine();
        return inputStr;
    }

    public static void intialBanner() {
        System.out.println("\n********************************************************************** ");
        System.out.println("\n***                                                                ***");
        System.out.println("\n***             Welcome to the Online Book Store                   ***");
        System.out.println("\n***                                                                ***");
        System.out.println("\n********************************************************************** ");
        System.out.println("\n           1. Member Login \n           2. New Member Registration \n           q. Quit");
    }

    public static void memeberRegistrationBanner() {
        System.out.println("\n********************************************************************** ");
        System.out.println("\n***                                                                ***");
        System.out.println("\n***             Welcome to the Online Book Store                   ***");
        System.out.println("\n***               New Member Registration                          ***");
        System.out.println("\n***                                                                ***");
        System.out.println("\n********************************************************************** ");
    }

    public static void printMemberDetails(Member member) {
        System.out.println("Name: " + member.getFname() + " "
                + member.getLname());
        System.out.println("Address:  " + member.getAddress());
        System.out.println("City:  " + member.getCity());
        System.out.println("Phone:  " + member.getPhone());
        System.out.println("Email: " + member.getEmail());
        System.out.println("User ID: " + member.getUserid());
        System.out.println("Password: " + member.getPassword());
        System.out.println("CreditCard Type: " + member.getCreditcardtype());
        System.out.println("CreditCard Number: " + member.getCreditcardnumber());

    }

    public static int memeberMenuBanner() {
        System.out.println("\n********************************************************************** ");
        System.out.println("\n***                                                                ***");
        System.out.println("\n***             Welcome to the Online Book Store                   ***");
        System.out.println("\n***                     Member Menu                                ***");
        System.out.println("\n***                                                                ***");
        System.out.println("\n********************************************************************** ");
        System.out.println("\n 1. Browse by Subject \n 2. Search by Author/Title/Subject \n  3. View/Edit Shopping Cart \n "
                + "4. Check Order Status \n 5. Check Out \n  6. One Click Check Out \n  7. View/Edit Personal Information \n 8. Logout");

        boolean inputvalid = false;
        int input = 0;
        while (!inputvalid) {
            String inpStr = BookStoreHelper.scanFromCommandLine("Type in your option: ");

            try {
                input = Integer.parseInt(inpStr);
            } catch (NumberFormatException n) {
                System.out.println("Please Enter a proper number among the options");
            }
            if (input >= 1 && input <= 8) {
                inputvalid = true;
            } else {
                System.out.println("Please enter a valid input from the above options:");
            }

        }

        return input;

    }

    public static int searchByAuthor_Title_Menu() {

        System.out.println("\n           1. Author Search \n           2. Title Search \n           3.  Go Back to Member Menu");

        boolean inputvalid = false;
        int input = 0;
        while (!inputvalid) {
            String inpStr = BookStoreHelper.scanFromCommandLine("Type in your option: ");

            try {
                input = Integer.parseInt(inpStr);
            } catch (NumberFormatException n) {
                System.out.println("Please Enter a proper number among the options");
            }
            if (input >= 1 && input <= 3) {
                inputvalid = true;
            } else {
                System.out.println("Please enter a valid input from the above options:");
            }

        }

        return input;


    }

    public static boolean StringTest(String input) {
        boolean isString = false;
        String str = input;
        Pattern p = Pattern.compile("[a-zA-Z]*");
        Matcher m = p.matcher(input);
        if (m.matches()) {
            isString = true;
        } else {
            isString = false;
        }
        return isString;
    }
}
