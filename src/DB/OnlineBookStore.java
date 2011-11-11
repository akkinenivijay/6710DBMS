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

			String inpStr = BookStoreHelper
					.scanFromCommandLine("Type in your option: ");

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
						System.out
								.println("Entered User Credentials are Invalid !!!!!");
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
				searchByAuthorFUN(con);
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
				System.out
						.println("\nPlease choose a number between 1-7 only \n");
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

	private static void searchByAuthorFUN(Connection con) {
	}

	private static void ViewEditShoppingCartFUN(Connection con) {
	}

	private static void checkOrderStatusFUN(Connection con) {
		BookStoreDao.checkOrderStatusDAO(con, CurrentUser.getInstance()
				.getUserID());

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
}
