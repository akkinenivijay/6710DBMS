/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pridhvi
 */
public class InsertTest {

    private static String subject1 = "GENERAL";
    private static int count1 = 2;
    private static int count2 = 3;

    public static void main(String[] args) {
        try {
            Connection con = null;

            String url = "jdbc:oracle:thin:@tinman.cs.gsu.edu:1522:tinman";

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (Exception e) {
                System.out.println("Failed to load Oracle driver.");
                return;
            }

            try {
                con = DriverManager.getConnection(url, "bookstore",
                        "bookstore123");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Statement stmt = con.createStatement();
            
            String ISBN = "0737800033";

          //  String CartQuery = "INSERT INTO CART VALUES (" + "raj" + "," + ISBN + "," + 1 + ")";
            
             String CartQuery1 = "insert into cart values(\'raj\',\'0737800033\',23)";
             
             System.out.println(CartQuery1);

           stmt.executeUpdate(CartQuery1);
           
            System.out.println(CartQuery1);
           
           stmt.close();
           con.close();

        } catch (SQLException ex) {
            Logger.getLogger(BookTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
