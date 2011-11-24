/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author pridhvi
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookStoreDao {

    public static void persistMemberToDB(Connection con, Member member)
            throws SQLException {
        String query = " insert into members values(?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement insertMember = null;

        insertMember = con.prepareStatement(query);
        insertMember.setString(1, member.getFname());
        insertMember.setString(2, member.getLname());
        insertMember.setString(3, member.getAddress());
        insertMember.setString(4, member.getCity());
        insertMember.setString(5, member.getState());
        insertMember.setInt(6, member.getZip());
        insertMember.setString(7, member.getPhone());
        insertMember.setString(8, member.getEmail());
        insertMember.setString(9, member.getUserid());
        insertMember.setString(10, member.getPassword());
        insertMember.setString(11, member.getCreditcardtype());
        insertMember.setString(12, member.getCreditcardnumber());

        insertMember.executeUpdate();
        insertMember.close();

    }

    public static boolean checkForUniqueness(String userID, Connection con) {
        String uniqenessQuery = "select count(*) from members a where a.userid = \'"
                + userID + "\'";
        boolean valid = false;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(uniqenessQuery);

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    valid = true;
                } else {
                    valid = false;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }

    public static void browseBySubjectDAO(Connection con) {

        int count = 0;
        int input = 0;
        boolean inputvalid = false;

        String subjectsQuery = "select DISTINCT(SUBJECT) from BOOKS";
        Hashtable subjectIndex = new Hashtable();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(subjectsQuery);

            while (rs.next()) {
                String subject = rs.getString("SUBJECT");
                ++count;

                subjectIndex.put(count, subject);

                System.out.println(" \n " + count + "." + " " + subject
                        + " \n ");
            }

            String inpStr = BookStoreHelper.scanFromCommandLine("Type in your option: ");
            if (inpStr.equalsIgnoreCase("") || inpStr.equalsIgnoreCase("\n")) {
                return;
            }

            try {
                input = Integer.parseInt(inpStr);
            } catch (NumberFormatException n) {
                System.out.println("Please select a subject option from the available");

            }

            while (!inputvalid) {

                if (input >= 1 && input <= count) {

                    inputvalid = true;
                    int rowCount = 10;

                    String sub1 = (String) subjectIndex.get(input);
                    String CountQuery = "select count(*) from BOOKS a where a.subject = \'"
                            + sub1 + "\'";
                    ;

                    ResultSet rs_Count = stmt.executeQuery(CountQuery);

                    while (rs_Count.next()) {
                        rowCount = rs_Count.getInt(1);

                    }
                    if (rowCount > 0) {

                        int count1 = 0;
                        int count2 = 0;

                        if (rowCount == 1) {
                            count1 = 1;
                            count2 = 1;
                            System.out.println(" \n " + rowCount + " "
                                    + "Book available on this Subject"
                                    + " \n ");
                        } else {
                            count1 = 1;
                            count2 = 2;
                            System.out.println(" \n " + rowCount + " "
                                    + "Books available on this Subject"
                                    + " \n ");
                        }

                        while (count2 <= rowCount) {
                            String subject_Display = "select * from (select x.*,rownum  as r"
                                    + " from (select *  from books where subject = \'"
                                    + sub1
                                    + "\' order by subject) x) where r between +"
                                    + count1 + " and " + count2;

                            ResultSet rs1 = stmt.executeQuery(subject_Display);
                            while (rs1.next()) {
                                String Author = rs1.getString("AUTHOR");
                                String Title = rs1.getString("TITLE");
                                String ISBN = rs1.getString("ISBN");
                                String Subject = rs1.getString("SUBJECT");

                                System.out.println(" \n Author:" + Author
                                        + "  ");
                                System.out.println(" \n Title:" + Title + "  ");
                                System.out.println(" \n ISBN:" + ISBN + " \n ");
                                System.out.println(" \n Subject:" + Subject
                                        + " \n ");
                                System.out.println(" \n " + " \n ");

                            }
                            String inpStr1 = BookStoreHelper.scanFromCommandLine(" \n Enter ISBN to add to Cart or "
                                    + " \n n Enter to browse or"
                                    + " \n  ENTER to go back to menu: ");
                            if (inpStr1.equalsIgnoreCase("")) {
                                break;
                            } else if (inpStr1.equalsIgnoreCase("n")) {
                                count1 = count1 + 2;
                                count2 = count2 + 2;
                                continue;
                            } else {
                                String TestISBN = inpStr1;
                                boolean validate_ISBN = validateISBN(con,
                                        TestISBN);
                                if (validate_ISBN) {
                                    addToCart(con, inpStr1);
                                } else {
                                    System.out.println("Please enter the correct ISBN number ");
                                    continue;
                                }
                            }

                        }

                    } else {
                        System.out.println(" \n "
                                + "No  Books available on this Subject"
                                + " \n ");
                    }

                } else {
                    System.out.println("Please enter a valid input form the above options:");
                    break;
                }
            }

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }

    public static void searchByAuthorDAO(Connection con, String authorString) {

        int count = 0;

        int rowCount = 10;

        String CountQuery = "select count(*) from BOOKS a where a.author like \'%"
                + authorString + "%\'";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs_Count = stmt.executeQuery(CountQuery);

            while (rs_Count.next()) {
                rowCount = rs_Count.getInt(1);
                System.out.println("ROW COUNT" + rowCount);

            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        if (rowCount > 0) {

            int count1 = 0;
            int count2 = 0;

            if (rowCount == 1) {
                count1 = 1;
                count2 = 1;
                System.out.println(" \n " + rowCount + " "
                        + "Book available with a similar author name "
                        + " \n ");
            } else {
                count1 = 1;
                count2 = 2;
                System.out.println(" \n " + rowCount + " "
                        + "Books available with  similar author names"
                        + " \n ");
            }

            while (count2 <= rowCount) {
                String book_Display = "select * from (select x.*,rownum  as r"
                        + " from (select *  from books where author like \'%"
                        + authorString
                        + "%\' order by author) x) where r between +"
                        + count1 + " and " + count2;

                try {
                    Statement stmt = con.createStatement();
                    ResultSet rs1 = stmt.executeQuery(book_Display);

                    while (rs1.next()) {

                        String Author = rs1.getString("AUTHOR");
                        String Title = rs1.getString("TITLE");
                        String ISBN = rs1.getString("ISBN");
                        String Subject = rs1.getString("SUBJECT");

                        System.out.println(" \n Author:" + Author
                                + "  ");
                        System.out.println(" \n Title:" + Title + "  ");
                        System.out.println(" \n ISBN:" + ISBN + " \n ");
                        System.out.println(" \n Subject:" + Subject
                                + " \n ");
                        System.out.println(" \n " + " \n ");

                    }

                } catch (SQLException sqe) {
                    sqe.printStackTrace();
                }
                String inpStr1 = BookStoreHelper.scanFromCommandLine(" \n Enter ISBN to add to Cart or "
                        + " \n n Enter to browse or"
                        + " \n  ENTER to go back to menu: ");


                if (inpStr1.equalsIgnoreCase(
                        "")) {
                    break;
                } else if (inpStr1.equalsIgnoreCase(
                        "n")) {
                    count1 = count1 + 2;
                    count2 = count2 + 2;
                    continue;
                } else {
                    String TestISBN = inpStr1;
                    boolean validate_ISBN = validateISBN(con,
                            TestISBN);
                    if (validate_ISBN) {
                        addToCart(con, inpStr1);
                    } else {
                        System.out.println("Please enter the correct ISBN number ");
                        continue;
                    }
                }
            }
        } else {
            System.out.println(" \n "
                    + "No  Books available with similar Author name"
                    + " \n ");
        }

    }

    public static void SearchByTitleDao(Connection con, String titleString) {

        int count = 0;

        int rowCount = 10;

        String CountQuery = "select count(*) from BOOKS a where a.title like \'%"
                + titleString + "%\'";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs_Count = stmt.executeQuery(CountQuery);

            while (rs_Count.next()) {
                rowCount = rs_Count.getInt(1);

            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }

        if (rowCount > 0) {

            int count1 = 0;
            int count2 = 0;

            if (rowCount == 1) {
                count1 = 1;
                count2 = 1;
                System.out.println(" \n " + rowCount + " "
                        + "Book available with a similar title name "
                        + " \n ");
            } else {
                count1 = 1;
                count2 = 2;
                System.out.println(" \n " + rowCount + " "
                        + "Books available with  similar title names"
                        + " \n ");
            }

            while (count2 <= rowCount) {
                String book_Display = "select * from (select x.*,rownum  as r"
                        + " from (select *  from books where title like \'%"
                        + titleString
                        + "%\' order by title) x) where r between +"
                        + count1 + " and " + count2;

                try {
                    Statement stmt = con.createStatement();
                    ResultSet rs1 = stmt.executeQuery(book_Display);

                    while (rs1.next()) {

                        String Author = rs1.getString("AUTHOR");
                        String Title = rs1.getString("TITLE");
                        String ISBN = rs1.getString("ISBN");
                        String Subject = rs1.getString("SUBJECT");

                        System.out.println(" \n Author:" + Author
                                + "  ");
                        System.out.println(" \n Title:" + Title + "  ");
                        System.out.println(" \n ISBN:" + ISBN + " \n ");
                        System.out.println(" \n Subject:" + Subject
                                + " \n ");
                        System.out.println(" \n " + " \n ");

                    }

                } catch (SQLException sqe) {
                    sqe.printStackTrace();
                }
                String inpStr1 = BookStoreHelper.scanFromCommandLine(" \n Enter ISBN to add to Cart or "
                        + " \n n Enter to browse or"
                        + " \n  ENTER to go back to menu: ");


                if (inpStr1.equalsIgnoreCase(
                        "")) {
                    break;
                } else if (inpStr1.equalsIgnoreCase(
                        "n")) {
                    count1 = count1 + 2;
                    count2 = count2 + 2;
                    continue;
                } else {
                    String TestISBN = inpStr1;
                    boolean validate_ISBN = validateISBN(con,
                            TestISBN);
                    if (validate_ISBN) {
                        addToCart(con, inpStr1);
                    } else {
                        System.out.println("Please enter the correct ISBN number ");
                        continue;
                    }
                }
            }
        } else {
            System.out.println(" \n "
                    + "No  Books available with similar Title name"
                    + " \n ");
        }

    }

    public static boolean validateISBN(Connection con, String ISBN) {
        boolean validated = false;

        String validateISBN_Query = "select * from books where ISBN=\'" + ISBN
                + "\'";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(validateISBN_Query);

            while (rs.next()) {
                validated = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return validated;

    }

    public static void addToCart(Connection con, String ISBN1) {

        int QT = 0;
        String ISBN = (String) ISBN1;
        String inpStr2 = BookStoreHelper.scanFromCommandLine("Enter quantity: ");

        try {
            QT = Integer.parseInt(inpStr2);
        } catch (NumberFormatException n) {
            System.out.println("Please enter a proper number as quantity");

        }

        String CartQuery = "insert into cart values(\'"
                + CurrentUser.getInstance().getUserID() + "\',\'" + ISBN
                + "\'," + QT + ")";
        // String CartQuery = "INSERT INTO CART " + " VALUES (" +
        // CurrentUser.getInstance().getUserID() + "," + ISBN + "," + inpStr1 +
        // ")";

        try {
            Statement stmt = con.createStatement();
            int row = stmt.executeUpdate(CartQuery);
            // System.out.println("**************");
            // System.out.println(row);
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            // System.out.println("**************");
        }

    }

    public static void viewEditShopingCartDAO(Connection con) {

        BookStoreDao.FetchCartDetails(con);

        String inpStr1 = BookStoreHelper.scanFromCommandLine(" \n  Enter d to delete item or "
                + " \n  e to edit cart or  or"
                + " \n  q to go back to Menu: ");

        String ISBN = "";


        if (inpStr1.equalsIgnoreCase("")) {
        } else if (inpStr1.equalsIgnoreCase(
                "e")) {
            ISBN = BookStoreHelper.scanFromCommandLine("Enter isbn of item:");
            boolean validate_ISBN = validateISBN(con, ISBN);
            if (validate_ISBN) {
                EditCart(con, ISBN);
            } else {
                System.out.println("Please enter the correct ISBN number ");
            }

        } else if (inpStr1.equalsIgnoreCase(
                "d")) {
            ISBN = BookStoreHelper.scanFromCommandLine("Enter isbn of item:");
            boolean validate_ISBN = validateISBN(con, ISBN);
            if (validate_ISBN) {
                DeleteFromCart(con, ISBN);
            } else {
                System.out.println("Please enter the correct ISBN number ");
            }
        }


    }

    public static boolean DeleteFromCart(Connection con, String ISBN) {

        boolean Deleted = false;
        int count = 0;
        Statement stmt = null;

        String Delete_From_Cart_Query = "delete from cart where userid=\'"
                + CurrentUser.getInstance().getUserID() + "\'"
                + "and ISBN=\'" + ISBN + "\'";


        try {
            stmt = con.createStatement();
            count = stmt.executeUpdate(Delete_From_Cart_Query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }

        if (count > 0) {
            System.out.println("Delete Item Completed");
            Deleted = true;
        } else {
            System.out.println("Not exist specific row that you select for delete");
        }

        return Deleted;

    }

    public static void EditCart(Connection con, String ISBN) {

        BookStoreDao.addToCart(con, ISBN);

    }

    public static void FetchCartDetails(Connection con) {
        String viewEditCart_Query = " select ISBN,QTY from cart where userid=\'"
                + CurrentUser.getInstance().getUserID() + "\'";


        System.out.println("Current Cart Contents:");

        Statement stmt = null;
        ResultSet rs = null;
        double Total_Order_Price = 0.0;

        String ISBN = "";

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("ISBN        Title                                                $  Qty   Total");
        System.out.println("-------------------------------------------------------------------------------");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(viewEditCart_Query);

            while (rs.next()) {
                int Quantity = rs.getInt("QTY");
                ISBN = rs.getString("ISBN");
                double ISBN_Price = Fetch_Price(con, ISBN);
                String Title = Fetch_Title(con, ISBN);
                System.out.println(ISBN + "        " + Title + "                                                " + ISBN_Price + "  " + Quantity + "   " + (Quantity * ISBN_Price));
                Total_Order_Price = Total_Order_Price + (Quantity * ISBN_Price);

            }
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Total =                                                                  $" + Total_Order_Price);
            System.out.println("-------------------------------------------------------------------------------");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    public static boolean checkOrderStatusDAO(Connection con, String userID) {
        System.out.println("Executing order query");
        String Orders_Query = " select ONO,RECEIVED,SHIPPED from orders where userid=\'"
                + userID + "\'";

        String User_Name_Query = "select FNAME,LNAME from MEMBERS WHERE USERID=\'"
                + CurrentUser.getInstance().getUserID() + "\'";
        boolean valid = false;
        boolean No_orders = false;
        String FirstName = "";
        String LastName = "";

        ResultSet Names = null;
        Statement stmt1 = null;
        try {
            stmt1 = con.createStatement();
            Names = stmt1.executeQuery(User_Name_Query);

            while (Names.next()) {
                FirstName = Names.getString("FNAME");
                LastName = Names.getString("LNAME");
            }
            Names.close();
            stmt1.close();

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            try {
                Names.close();
            } catch (SQLException ex) {
                Logger.getLogger(BookStoreDao.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        ResultSet rs = null;
        Statement stmt2 = null;
        try {
            stmt2 = con.createStatement();
            rs = stmt2.executeQuery(Orders_Query);

            if (rs != null) {
                System.out.println("Orders placed by" + " " + FirstName + " "
                        + LastName);
                System.out.println();
                System.out.println("-----------------------------------------------------------");
                System.out.println("ORDER NO    RECEIVED DATE                 SHIPPED DATE");
                System.out.println("-----------------------------------------------------------");

                while (rs.next()) {
                    int ORDERNO = rs.getInt("ONO");
                    Date RECEIVEDDATE = rs.getDate("RECEIVED");
                    Date SHIPPEDDATE = rs.getDate("SHIPPED");

                    System.out.println(ORDERNO + "            " + RECEIVEDDATE
                            + "                 " + SHIPPEDDATE);
                }

                // boolean No_orders=true;
                rs.close();
                stmt2.close();
            } else {
                System.out.println("No Orders are placed by the user" + " "
                        + FirstName + " " + LastName);
                rs.close();
                stmt2.close();
            }
        } catch (Exception e) {
        } finally {
            try {
                rs.close();






            } catch (SQLException ex) {
                Logger.getLogger(BookStoreDao.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return No_orders;

    }

    public static void showCheckOrderDetails(Connection con) {
        String inputOption = BookStoreHelper.scanFromCommandLine("Enter the Order No to display its details or (q) to quit: ");
        boolean validity = false;
        boolean valid_ONO = false;
        if (inputOption.equalsIgnoreCase("q")) {
            return;
        }
        int OrderNum = 0;
        try {
            OrderNum = Integer.parseInt(inputOption);
        } catch (NumberFormatException n) {
            n.printStackTrace();
        }
        while (!validity) {

            if (inputOption == null || inputOption.equalsIgnoreCase("")) {
                validity = false;
                inputOption = BookStoreHelper.scanFromCommandLine("Please Enter a valid Order No to display its details or (q) to quit: ");

            } else {
                validity = true;
            }
        }
    }

    public static void checkOutDAO(Connection con) {
        String ClickCheckOutCart_Query = " select ISBN,QTY from cart where userid=\'"
                + CurrentUser.getInstance().getUserID() + "\'";

        String memeberDetailsQuery = "select * from members where userid=\'"
                + CurrentUser.getInstance().getUserID() + "\'";

        HashMap<String, Integer> cartData = new HashMap<String, Integer>();

        Statement stmt = null;
        String SHIP_FirstName = "";
        String SHIP_LastName = "";
        String SHIP_ADDRESS = "";
        String SHIP_CITY = "";
        int SHIP_zip = 0;
        String SHIP_state = "";

        String FirstName = "";
        String LastName = "";
        String ADDRESS = "";
        String CITY = "";
        int zip = 0;
        String state = "";
        int ORDERNO = 0;

        System.out.println("Current Cart Contents:");
        BookStoreDao.FetchCartDetails(con);

        try {

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(memeberDetailsQuery);

            while (rs.next()) {
                ADDRESS = rs.getString("ADDRESS");
                CITY = rs.getString("CITY");
                zip = rs.getInt("ZIP");
                state = rs.getString("STATE");
                FirstName = rs.getString("FNAME");
                LastName = rs.getString("LNAME");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String Proceed_Checkout = "";
        boolean Valid_Proceed_Checkout = false;
        boolean Vaild_shippingAddress = false;
        String Shipping_Address_NEW = "";

        while (!Valid_Proceed_Checkout) {

            Proceed_Checkout = BookStoreHelper.scanFromCommandLine("Proceed to check out(Y/N):");
            if (Proceed_Checkout.equalsIgnoreCase("y")) {

                while (!Vaild_shippingAddress) {

                    Shipping_Address_NEW = BookStoreHelper.scanFromCommandLine("Do you want to enter new shipping address(y/n):");

                    if (Shipping_Address_NEW.equalsIgnoreCase("y")) {

                        SHIP_FirstName = BookStoreHelper.scanFromCommandLine("Enter firstname: ");
                        while (SHIP_FirstName == null || SHIP_FirstName.equalsIgnoreCase("")) {
                            System.out.println("Please input a valid First Name");
                            SHIP_FirstName = BookStoreHelper.scanFromCommandLine("Enter firstname: ");
                        }

                        SHIP_LastName = BookStoreHelper.scanFromCommandLine("Enter last name: ");
                        while (SHIP_LastName == null || SHIP_LastName.equalsIgnoreCase("")) {
                            System.out.println("Please input a valid last Name");
                            SHIP_LastName = BookStoreHelper.scanFromCommandLine("Enter last name: ");
                        }

                        boolean validAddress = false;
                        while (!validAddress) {
                            SHIP_ADDRESS = BookStoreHelper.scanFromCommandLine("Enter street address: ");
                            if (SHIP_ADDRESS == null || SHIP_ADDRESS.equalsIgnoreCase("")) {
                                validAddress = false;
                                System.out.println("Please input a valid Street Address:");
                            } else {
                                validAddress = true;
                            }
                        }

                        boolean validCity = false;

                        while (!validCity) {
                            SHIP_CITY = BookStoreHelper.scanFromCommandLine("Enter City: ");
                            if (SHIP_CITY == null || SHIP_CITY.equalsIgnoreCase("")) {
                                validCity = false;
                                System.out.println("Please input a valid  city:");
                            } else {
                                validCity = true;
                            }
                        }

                        boolean validState = false;
                        while (!validState) {
                            state = BookStoreHelper.scanFromCommandLine("Enter State: ");
                            if (state == null || state.equalsIgnoreCase("")) {
                                validState = false;
                                System.out.println("Please input a valid  State:");
                            } else {
                                validState = true;
                            }
                        }

                        boolean validZip = false;
                        String ZIP = "";
                        while (!validZip) {
                            ZIP = BookStoreHelper.scanFromCommandLine("Enter Zip: ");
                            if (ZIP == null || ZIP.equalsIgnoreCase("") || ZIP.trim().length() != 5) {
                                validZip = false;
                                System.out.println("Please input a valid  Zip:");
                            } else {
                                try {
                                    SHIP_zip = Integer.parseInt(ZIP);
                                    validZip = true;
                                } catch (NumberFormatException nfe) {
                                    System.out.println("Please input a valid  Zip:Should be digit characters");
                                }
                            }
                        }
                        boolean exit = false;

                        while (!exit) {

                            String creditProceed = BookStoreHelper.scanFromCommandLine("Do you wish to store credit card information(y/n):");

                            boolean proceed = false;
                            if (creditProceed != null) {
                                if (creditProceed.equalsIgnoreCase("y")
                                        || creditProceed.equalsIgnoreCase("n")) {
                                    if (creditProceed.equalsIgnoreCase("y")) {
                                        proceed = true;
                                    }
                                    if (creditProceed.equalsIgnoreCase("n")) {
                                        proceed = false;
                                    }
                                    exit = true;
                                } else {
                                    proceed = false;
                                }
                            } else {
                                proceed = false;
                            }

                            if (proceed) {

                                boolean creditTypeValid = false;

                                while (!creditTypeValid) {
                                    String creditCardType = BookStoreHelper.scanFromCommandLine("Enter type of Credit Card(amex/visa):");

                                    if (creditCardType != null) {
                                        if (creditCardType.trim().equalsIgnoreCase("amex")
                                                || creditCardType.trim().equalsIgnoreCase(
                                                "visa")) {
                                            creditTypeValid = true;
                                        }
                                    }

                                    if (!creditTypeValid) {
                                        System.out.println("Please enter a valid credit card type: SHould be wither amex or visa");
                                    }
                                }

                                boolean creditCardNumberValid = false;

                                while (!creditCardNumberValid) {

                                    String creditCardNUmber = BookStoreHelper.scanFromCommandLine("Enter Credit Card Number:");

                                    if (creditCardNUmber != null) {
                                        if (creditCardNUmber.trim().length() == 16) {
                                            try {
                                                long cardNumber = Long.parseLong(creditCardNUmber);
                                                creditCardNumberValid = true;
                                            } catch (NumberFormatException e) {
                                                creditCardNumberValid = false;
                                            }
                                        } else {
                                            creditCardNumberValid = false;
                                        }
                                    }

                                    if (!creditCardNumberValid) {
                                        System.out.println("PLease input a valid credit card number: (Only Digits Allowed and should be of 16 characters");
                                    }

                                }

                            }
                        }
                        Vaild_shippingAddress = true;

                    } else if (Shipping_Address_NEW.equalsIgnoreCase("n")) {

                        SHIP_FirstName = FirstName;
                        SHIP_LastName = LastName;
                        SHIP_CITY = CITY;
                        SHIP_zip = zip;
                        SHIP_state = state;
                        Vaild_shippingAddress = true;

                    } else {
                        continue;
                    }
                }
                try {

                    stmt = con.createStatement();

                    ResultSet rs = stmt.executeQuery(ClickCheckOutCart_Query);

                    while (rs.next()) {
                        String ISBN = rs.getString("ISBN");
                        int Quantity = rs.getInt("QTY");
                        cartData.put(ISBN, Quantity);
                    }
                    rs.close();
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (String isbn : cartData.keySet()) {
                    try {
                        stmt = con.createStatement();

                        DatabaseMetaData metaData = con.getMetaData();
                        System.out.println("Generated Keys Feature: "
                                + metaData.supportsGetGeneratedKeys());

                        double PRICE = CalcuatePrice(con, isbn, cartData.get(isbn));

                        String INSERT_RECORD = "insert into ORDERS(USERID, RECEIVED,SHIPPED,SHIPADDRESS,SHIPCITY,SHIPSTATE,SHIPZIP) values(?,?,?,?,?,?,?)";
                        String[] cols = {"ONO"};
                        PreparedStatement ps = con.prepareStatement(INSERT_RECORD,
                                Statement.RETURN_GENERATED_KEYS);

                        // ps.setInt(1, ORDERNO);
                        ps.setString(1, CurrentUser.getInstance().getUserID());
                        ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                        ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                        ps.setString(4, SHIP_ADDRESS);
                        ps.setString(5, SHIP_CITY);
                        ps.setString(6, SHIP_state);
                        ps.setInt(7, SHIP_zip);

                        int bool = ps.executeUpdate();

                        ResultSet rset = ps.getGeneratedKeys();
                        String ROWNUM = "";
                        while (rset.next()) {
                            ROWNUM = rset.getString(1);
                            // System.out.println("Autogeneraed order number from the sequence is: "
                            //        + ROWNUM);
                        }

                        Statement sqlStatement = con.createStatement();
                        ResultSet rest = sqlStatement.executeQuery("select * from orders where rowid = "
                                + "\'" + ROWNUM + "\'" + " ");

                        rest.next();
                        ORDERNO = rest.getInt("ONO");

                        String ODetails_Query = "INSERT INTO ODETAILS VALUES(" + "\'"
                                + ORDERNO + "\'," + "\'" + isbn + "\',"
                                + cartData.get(isbn) + "," + PRICE + ")";

                        String Delete_Cart_Query = "delete from cart where userid=\'"
                                + CurrentUser.getInstance().getUserID() + "\'"
                                + "and ISBN=\'" + isbn + "\'";
                        stmt.addBatch(ODetails_Query);
                        stmt.addBatch(Delete_Cart_Query);
                        int[] updateActions = stmt.executeBatch();

                        stmt.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }

                System.out.println("Invoice for Order no." + ORDERNO);
                System.out.println("Shipping Address                    Billing address");
                System.out.println("Name:     " + SHIP_FirstName + " " + SHIP_LastName + "           Name:     " + FirstName + " " + LastName);
                System.out.println("Address:  " + SHIP_ADDRESS + "                      Address:  " + ADDRESS);
                System.out.println("          " + SHIP_CITY + "                             " + CITY);
                System.out.println("          " + SHIP_state + " " + SHIP_zip + "                            " + state + " " + zip);
                System.out.println();
                System.out.println();
                FetchOrderDetails(con, ORDERNO);

                Valid_Proceed_Checkout = true;
            } else if (Proceed_Checkout.equalsIgnoreCase("n")) {

                for (String isbn : cartData.keySet()) {
                    BookStoreDao.DeleteFromCart(con, isbn);
                }
                Valid_Proceed_Checkout = true;
                break;
            }

        }
    }

    public static void OneClickCheckOutDAO(Connection con) {

        String ClickCheckOutCart_Query = " select ISBN,QTY from cart where userid=\'"
                + CurrentUser.getInstance().getUserID() + "\'";

        String memeberDetailsQuery = "select * from members where userid=\'"
                + CurrentUser.getInstance().getUserID() + "\'";

        HashMap<String, Integer> cartData = new HashMap<String, Integer>();

        Statement stmt = null;
        String FirstName = "";
        String LastName = "";
        String ADDRESS = "";
        String CITY = "";
        int zip = 0;
        String state = "";
        int ORDERNO = 0;

        try {

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(memeberDetailsQuery);

            while (rs.next()) {
                ADDRESS = rs.getString("ADDRESS");
                CITY = rs.getString("CITY");
                zip = rs.getInt("ZIP");
                state = rs.getString("STATE");
                FirstName = rs.getString("FNAME");
                LastName = rs.getString("LNAME");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(ClickCheckOutCart_Query);

            while (rs.next()) {
                String ISBN = rs.getString("ISBN");
                int Quantity = rs.getInt("QTY");
                cartData.put(ISBN, Quantity);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String isbn : cartData.keySet()) {
            try {
                stmt = con.createStatement();

                DatabaseMetaData metaData = con.getMetaData();
                System.out.println("Generated Keys Feature: "
                        + metaData.supportsGetGeneratedKeys());

                double PRICE = CalcuatePrice(con, isbn, cartData.get(isbn));

                String INSERT_RECORD = "insert into ORDERS(USERID, RECEIVED,SHIPPED,SHIPADDRESS,SHIPCITY,SHIPSTATE,SHIPZIP) values(?,?,?,?,?,?,?)";
                String[] cols = {"ONO"};
                PreparedStatement ps = con.prepareStatement(INSERT_RECORD,
                        Statement.RETURN_GENERATED_KEYS);

                // ps.setInt(1, ORDERNO);
                ps.setString(1, CurrentUser.getInstance().getUserID());
                ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                ps.setString(4, ADDRESS);
                ps.setString(5, CITY);
                ps.setString(6, state);
                ps.setInt(7, zip);

                int bool = ps.executeUpdate();

                ResultSet rset = ps.getGeneratedKeys();
                String ROWNUM = "";
                while (rset.next()) {
                    ROWNUM = rset.getString(1);
                    // System.out.println("Autogeneraed order number from the sequence is: "
                    //        + ROWNUM);
                }

                Statement sqlStatement = con.createStatement();
                ResultSet rest = sqlStatement.executeQuery("select * from orders where rowid = "
                        + "\'" + ROWNUM + "\'" + " ");

                rest.next();
                ORDERNO = rest.getInt("ONO");

                String ODetails_Query = "INSERT INTO ODETAILS VALUES(" + "\'"
                        + ORDERNO + "\'," + "\'" + isbn + "\',"
                        + cartData.get(isbn) + "," + PRICE + ")";

                String Delete_Cart_Query = "delete from cart where userid=\'"
                        + CurrentUser.getInstance().getUserID() + "\'"
                        + "and ISBN=\'" + isbn + "\'";
                stmt.addBatch(ODetails_Query);
                stmt.addBatch(Delete_Cart_Query);
                int[] updateActions = stmt.executeBatch();

                stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            }
        }

        System.out.println("Invoice for Order no." + ORDERNO);
        System.out.println("Shipping Address                    Billing address");
        System.out.println("Name:     " + FirstName + " " + LastName + "           Name:     " + FirstName + " " + LastName);
        System.out.println("Address:  " + ADDRESS + "                      Address:  " + ADDRESS);
        System.out.println("          " + CITY + "                             " + CITY);
        System.out.println("          " + state + " " + zip + "                            " + state + " " + zip);
        System.out.println();
        System.out.println();
        FetchOrderDetails(con, ORDERNO);



    }

    public static double CalcuatePrice(Connection con, String ISBN, int Quantity) {
        String Price_Query = "SELECT PRICE  FROM BOOKS WHERE ISBN=\'" + ISBN
                + "\'";

        double price = 0.0;
        double price1 = 0.0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(Price_Query);

            while (rs.next()) {
                price1 = (double) rs.getFloat("PRICE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        price = price1 * Quantity;
        return price;
    }

    public static void PrintDetails(Connection con, String ISBN, int quantity) {
        String Title_Query = "SELECT TITLE,PRICE  FROM BOOKS WHERE ISBN=\'" + ISBN
                + "\'";

        double price = 0.0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(Title_Query);

            while (rs.next()) {
                String TITLE = rs.getString("TITLE");
                double PRICE = (double) rs.getFloat("PRICE");
                System.out.println(ISBN + "        " + TITLE + "                                                " + PRICE + "  " + quantity + "   " + (PRICE * quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void viewOrEditPersonalInfoDAO(Connection con) {
    }

    public static boolean checkForValidCredentials(String userID, String password, Connection con) {
        Statement stmt = null;
        ResultSet rs = null;

        String uniqenessQuery = "select * from members a where a.userid = \'"
                + userID + "\'";
        boolean valid = false;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(uniqenessQuery);

            if (rs.next()) {
                String useridFromDB = rs.getString("userid");
                String passwordFromDB = rs.getString("password");

                if (userID.trim().equalsIgnoreCase(useridFromDB)
                        && password.trim().equalsIgnoreCase(passwordFromDB)) {
                    valid = true;

                } else {
                    valid = false;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public static boolean checkForValidOrderNumber(Connection con, int OrderNumber) {
        Statement stmt = null;
        ResultSet rs = null;

        String uniqenessQuery = "select ono from orders a where a.userid = \'"
                + CurrentUser.getInstance().getUserID() + "\'" + " and ONO=" + OrderNumber;

        boolean valid = false;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(uniqenessQuery);

            if (rs.next()) {
                int ordernumFromDB = rs.getInt("ono");
                if (OrderNumber == ordernumFromDB) {
                    valid = true;

                } else {
                    valid = false;
                }
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public static void FetchOrderDetails(Connection con, int OrderNumber) {
        Statement stmt = null;
        ResultSet rs = null;
        double Total_Order_Price = 0.0;
        String Order_Details = "select * from odetails where ONO=" + OrderNumber;
        String ISBN = "";

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("ISBN        Title                                                $  Qty   Total");
        System.out.println("-------------------------------------------------------------------------------");

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(Order_Details);

            while (rs.next()) {
                int Quantity = rs.getInt("QTY");
                ISBN = rs.getString("ISBN");
                double ISBN_Price = Fetch_Price(con, ISBN);
                String Title = Fetch_Title(con, ISBN);
                System.out.println(ISBN + "        " + Title + "                                                " + ISBN_Price + "  " + Quantity + "   " + (Quantity * ISBN_Price));
                Total_Order_Price = Total_Order_Price + (Quantity * ISBN_Price);

            }
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("Total =                                                                  $" + Total_Order_Price);
            System.out.println("-------------------------------------------------------------------------------");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }


    }

    public static double Fetch_Price(Connection con, String ISBN) {
        Statement stmt = null;
        ResultSet rs = null;

        double price = 0.0;

        String Price_Query = "Select PRICE FROM BOOKS WHERE ISBN=\'" + ISBN + "\'";

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(Price_Query);

            while (rs.next()) {
                price = rs.getDouble("PRICE");

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return price;
    }

    public static String Fetch_Title(Connection con, String ISBN) {
        Statement stmt = null;
        ResultSet rs = null;

        String title = "";

        String Title_Query = "Select TITLE FROM BOOKS WHERE ISBN=\'" + ISBN + "\'";

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(Title_Query);

            while (rs.next()) {
                title = rs.getString("TITLE");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return title;
    }
}
