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


public class Registration {
    
    public static Member memeberRegistrationForm(Connection con) {
        Member member = new Member();
        
        String fName = BookStoreHelper.scanFromCommandLine("Enter firstname: ");
        while (fName == null || fName.equalsIgnoreCase("")) {
            System.out.println("Please input a valid First Name");
            fName = BookStoreHelper.scanFromCommandLine("Enter firstname: ");
        }
        member.setFname(fName.trim());
        
        String lName = BookStoreHelper.scanFromCommandLine("Enter last name: ");
        while (lName == null || lName.equalsIgnoreCase("")) {
            System.out.println("Please input a valid last Name");
            lName = BookStoreHelper.scanFromCommandLine("Enter last name: ");
        }
        member.setLname(lName.trim());
        
        boolean validAddress = false;
        String address = "";
        while (!validAddress) {
            address = BookStoreHelper.scanFromCommandLine("Enter street address: ");
            if (address == null || address.equalsIgnoreCase("")) {
                validAddress = false;
                System.out.println("Please input a valid Street Address:");
            } else {
                member.setAddress(address.trim());
                validAddress = true;
            }
        }
        
        boolean validCity = false;
        String city = "";
        while (!validCity) {
            city = BookStoreHelper.scanFromCommandLine("Enter City: ");
            if (city == null || city.equalsIgnoreCase("")) {
                validCity = false;
                System.out.println("Please input a valid  city:");
            } else {
                member.setCity(city.trim());
                validCity = true;
            }
        }
        
        boolean validState = false;
        String state = "";
        while (!validState) {
            state = BookStoreHelper.scanFromCommandLine("Enter State: ");
            if (state == null || state.equalsIgnoreCase("")) {
                validState = false;
                System.out.println("Please input a valid  State:");
            } else {
                member.setState(state.trim());
                validState = true;
            }
        }
        
        boolean validZip = false;
        String zip = "";
        while (!validZip) {
            zip = BookStoreHelper.scanFromCommandLine("Enter Zip: ");
            if (zip == null || zip.equalsIgnoreCase("")||zip.trim().length()!=5) {
                validZip = false;
                System.out.println("Please input a valid  Zip:");
            } else {
                try {
                    int zipInt = Integer.parseInt(zip);
                    member.setZip(zipInt);
                    validZip = true;
                } catch (NumberFormatException nfe) {
                    System.out.println("Please input a valid  Zip:Should be digit characters");
                }
            }
        }
        
        member.setPhone(BookStoreHelper.scanFromCommandLine("Enter phone: "));
        member.setEmail(BookStoreHelper.scanFromCommandLine("Enter email address: "));
        
        boolean validUserID = false;
        String userID = "";
        while (!validUserID) {
            userID = BookStoreHelper.scanFromCommandLine("Enter UserID: ");
            if (userID == null || userID.equalsIgnoreCase("")) {
                validUserID = false;
                System.out.println("Please input a valid  UserID:");
            } else {
                validUserID = BookStoreDao.checkForUniqueness(userID, con);
                if (validUserID) {
                    member.setUserid(userID);
                } else {
                    System.out.println("UserID is not available: Please input a unique user id");
                }
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
                member.setPassword(password);
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
                    if(creditProceed.equalsIgnoreCase("n")){
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
                            member.setCreditcardtype(creditCardType);
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
                                member.setCreditcardnumber(creditCardNUmber);
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
        
        return member;
    }
}

