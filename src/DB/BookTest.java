/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pridhvi
 */
public class BookTest {

    private static String subject1 = "GENERAL";
    private static int count1 = 2;
    private static int count2 = 3;

    public static void main(String[] args) {
        String str = "xyzas";
        Pattern p = Pattern.compile("[a-zA-Z]*");
        Matcher m = p.matcher(str);
        System.out.println(m.matches());
    }
}
