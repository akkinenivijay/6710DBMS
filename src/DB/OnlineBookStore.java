/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author pridhvi
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class OnlineBookStore {

    public static void main(String args[]) throws SQLException, IOException {

        Connection con = null;

        String url = "jdbc:oracle:thin:@tinman.cs.gsu.edu:1522:tinman";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            System.out.println("Failed to load Oracle driver.");
            return;
        }

        try {
            con = DriverManager.getConnection(url, "bookstore", "bookstore123");
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean quit = false;
        while (!quit) {

            BookStoreHelper.intialBanner();

            String inpStr = BookStoreHelper.scanFromCommandLine("Type in your option: ");

            int inpInt = BookStoreHelper.returnIntegerFromInput(inpStr);
            quit = BookStoreHelper.exitMenu(quit, inpStr, inpInt);

            if (!quit) {

                if (inpInt == 2) {
                    memberRegistration(con);
                }
                if (inpInt == 1) {
                    boolean validAuthentication = promptAndCheckUserCredentials(con);
                    if (validAuthentication) {
                        memberMenu(con);

                    } else {
                        System.out.println("Entered User Credentials are Invalid !!!!!");
                    }
                }

            } else {
                continue;
            }

        }

        if (con != null) {
            con.close();
        }

    }

    private static void memberMenu(Connection con) {
        int menuOption = 0;

        if (menuOption == 0) {
            menuOption = BookStoreHelper.memeberMenuBanner();
        }

        while (menuOption >= 0 && menuOption <= 8) {
            if (menuOption == 0) {
                menuOption = BookStoreHelper.memeberMenuBanner();
            }

            switch (menuOption) {
                // the choices go here - print the details
                case 1:
                    browseBySubjectFUN(con);
                    break;
                case 2:
                    SearchByAuthor_Title_FUN(con);
                    break;
                case 3:
                    ViewEditShoppingCartFUN(con);
                    break;
                case 4:
                    checkOrderStatusFUN(con);
                    break;

                case 5:
                    checkOutFUN(con);
                    break;

                case 6:
                    OneClickCheckOutFUN(con);
                    break;
                case 7:
                    viewOrEditPersonalInfoFUN(con);
                    break;
                case 8:
                    break;
                default:
                    System.out.println("\nPlease choose a number between 1-7 only \n");
            }

            if (menuOption == 8) {
                return;
            }
            menuOption = 0;

        }

    }

    private static void browseBySubjectFUN(Connection con) {
        BookStoreDao.browseBySubjectDAO(con);

    }

    private static void SearchByAuthor_Title_FUN(Connection con) {

        int menuOption = 0;

        if (menuOption == 0) {
            menuOption = BookStoreHelper.searchByAuthor_Title_Menu();
        }

        while (menuOption >= 0 && menuOption <= 3) {
            if (menuOption == 0) {
                menuOption = BookStoreHelper.searchByAuthor_Title_Menu();
            }

            switch (menuOption) {
                // the choices go here - print the details
                case 1:
                    SearchByAuthorFUN(con);
                    break;
                case 2:
                    SearchByTitleFUN(con);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("\nPlease choose a optioin between 1-3 only \n");
            }

            if (menuOption == 3) {
                return;
            }
            menuOption = 0;

        }


    }

    private static void SearchByAuthorFUN(Connection con) {

        boolean validUserID = false;
        String authorString = "";
        while (!validUserID) {
            authorString = BookStoreHelper.scanFromCommandLine("Enter author name or part of author name: ");
            if (authorString == null || authorString.equalsIgnoreCase("")) {
                validUserID = false;
                System.out.println("Please input a proper string:");
            } else {
                if (BookStoreHelper.StringTest(authorString)) {
                    validUserID = true;
                    BookStoreDao.searchByAuthorDAO(con, authorString);
                } else {
                    System.out.println("Please input a proper string with alphabet");
                }

            }
        }

    }

    private static void SearchByTitleFUN(Connection con) {
        boolean validUserID = false;

        String titleString = "";

        while (!validUserID) {

            titleString = BookStoreHelper.scanFromCommandLine("Enter the title or part of the tile: ");
            if (titleString == null || titleString.equalsIgnoreCase("")) {
                validUserID = false;
                System.out.println("Please input a proper string:");
            } else {
                if (BookStoreHelper.StringTest(titleString)) {
                    validUserID = true;
                    BookStoreDao.SearchByTitleDao(con, titleString);
                } else {
                    System.out.println("Please input a proper string with alphabet");
                }

            }
        }
    }

    private static void ViewEditShoppingCartFUN(Connection con) {
        BookStoreDao.viewEditShopingCartDAO(con);
    }

    private static void checkOrderStatusFUN(Connection con) {
        BookStoreDao.checkOrderStatusDAO(con, CurrentUser.getInstance().getUserID());
        Prompt_Order(con);
    }

    private static void checkOutFUN(Connection con) {
    }

    private static void OneClickCheckOutFUN(Connection con) {
        BookStoreDao.OneClickCheckOutDAO(con);
    }

    private static void viewOrEditPersonalInfoFUN(Connection con) {
    }

    private static void memberRegistration(Connection con) {
        BookStoreHelper.memeberRegistrationBanner();
        Member member = Registration.memeberRegistrationForm(con);

        boolean registrationSuccess = false;

        // TODO not sure if we have to do this. check and remove the loop
        while (!registrationSuccess) {
            try {
                BookStoreDao.persistMemberToDB(con, member);
                System.out.println("You have registered successfully.");
                BookStoreHelper.printMemberDetails(member);
                registrationSuccess = true;
            } catch (SQLException e) {
                System.out.println("Error Registering a member: "
                        + e.getMessage());
                System.out.println("Please try again:");
                registrationSuccess = false;
            }
        }

        // if(registrationSuccess){
        //
        // BookStoreDao.FetchMemberDetails(con,userID);
        // }

    }

    private static boolean promptAndCheckUserCredentials(Connection con) {

        boolean validUserID = false;
        String userID = "";
        while (!validUserID) {
            userID = BookStoreHelper.scanFromCommandLine("Enter UserID: ");
            if (userID == null || userID.equalsIgnoreCase("")) {
                validUserID = false;
                System.out.println("Please input a valid  UserID:");
            } else {
                validUserID = true;
            }
        }

        boolean validPassword = false;
        String password = "";
        while (!validPassword) {
            password = BookStoreHelper.scanFromCommandLine("Enter Password: ");
            if (password == null || password.equalsIgnoreCase("")) {
                validPassword = false;
                System.out.println("Please input a valid  Password:");
            } else {
                validPassword = true;
            }
        }

        boolean validCredentials = BookStoreDao.checkForValidCredentials(
                userID, password, con);

        if (validCredentials) {

            CurrentUser authorizedUser = CurrentUser.getInstance();
            authorizedUser.setUserID(userID);
            authorizedUser.setPaassword(password);
        }

        return validCredentials;
    }

    private static void Prompt_Order(Connection con) {


        boolean quit = false;
        while (!quit) {

            String inpStr = BookStoreHelper.scanFromCommandLine("Enter the Order No to display its details or (q) to quit: ");

            int inpInt = BookStoreHelper.returnIntegerFromInput(inpStr);
            quit = BookStoreHelper.exitMenu(quit, inpStr, inpInt);

            if (!quit) {

                boolean validOrderNumber = BookStoreDao.checkForValidOrderNumber(con, inpInt);;
                if (validOrderNumber) {
                    BookStoreDao.FetchOrderDetails(con, inpInt);

                } else {
                    System.out.println("Entered Order Number does not Exist !!!!!");
                }
            } else {
                continue;
            }
        }
    }
}
