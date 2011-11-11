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
public class BookTest {

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

            String subject_Display = "select * from (select x.*,rownum  as r"
                    + " from (select *  from books where subject = \'" + subject1 + 
                    "\' order by subject) x) where r between +" + 
                    count1 + " and " + count2;

            ResultSet rs1 = stmt.executeQuery(subject_Display);

            while (rs1.next()) {
                System.out.println(rs1.getString("AUTHOR") + " " + rs1.getString("SUBJECT") + " " + rs1.getInt("r"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
