package core.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by harry on 11/03/2017.
 */

public class Register {

    public enum attemptOutcome {
        SUCCESS(6), WRITE_FAIL(5), USERNAME_TAKEN(4), PHONENO_FAIL(3), PASSWORD_UNSATISFIED(2), PASSWORDS_DIFFERENT(1), EMPTY_FIELDS(0);
        private int value;

        private attemptOutcome(int value){
            this.value = value;
        }
    }


    //This function is just to make it simpler so that you don't have to call every function from main.
    public attemptOutcome registerAttempt(HashMap customerDetailsHMap){

        if(!isNotEmpty(customerDetailsHMap)) {
            return attemptOutcome.EMPTY_FIELDS;
        }
        else if(!passwordMatches(customerDetailsHMap)) {
            return attemptOutcome.PASSWORDS_DIFFERENT;
        }
        else if(!passwordCriteria(customerDetailsHMap)) {
            return attemptOutcome.PASSWORD_UNSATISFIED;
        }
        else if(!phoneNoIsAus(customerDetailsHMap)) {
            return attemptOutcome.PHONENO_FAIL;
        }
        else if(!userNameFree(customerDetailsHMap)) {
            return attemptOutcome.USERNAME_TAKEN;
        }
        else if(!writeNewCustomer(customerDetailsHMap)) {
            return attemptOutcome.WRITE_FAIL;
        }
        else return attemptOutcome.SUCCESS;
    }

    private boolean isNotEmpty(HashMap custDetailsHMap){

        //Check none of the fields are empty
        if(custDetailsHMap.get("name").equals("") ||
            custDetailsHMap.get("userName").equals("") ||
            custDetailsHMap.get("password1").equals("") ||
            custDetailsHMap.get("password2").equals("") ||
            custDetailsHMap.get("address").equals("") ||
            custDetailsHMap.get("phoneNo").equals(""))  {

                System.out.println("You have not filled out all of the fields");
                return false;
        }

        return true;
    }


    private boolean passwordMatches(HashMap custDetailsHMap){

        //Check if the passwords match each other
        if(custDetailsHMap.get("password1").equals(custDetailsHMap.get("password2"))){
            return true;
        } else return false;

    }

    private boolean passwordCriteria(HashMap custDetailsHMap){

        String password = (String) custDetailsHMap.get("password1");

        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());

        //Check if the password matches the criteria of 8 chars, at least 1 uppercase, 1 lowercase and 1 digit
        if((password.length() < 8) || !hasLowercase || !hasUppercase || !password.matches(".*\\d+.*")) {
            System.out.println("Your password must have more than 8 characters, at least one uppercase, one lowercase and a number or symbol");
            return false;
        }

        return true;
    }

    private boolean phoneNoIsAus(HashMap custDetailsHMap){
        String phoneNo = (String) custDetailsHMap.get("phoneNo");
        if(phoneNo.matches("^\\({0,1}((0|\\+61)(2|4|3|7|8)){0,1}\\){0,1}(\\ |-){0,1}[0-9]{2}(\\ |-){0,1}[0-9]{2}(\\ |-){0,1}[0-9]{1}(\\ |-){0,1}[0-9]{3}$")){
            return true;
        }
        return false;
    }


    private boolean userNameFree(HashMap custDetailsHMap){
        //Setup with datebase
        ResultSet rs;
        //Create SQL Query
        String sqlQuery = "SELECT userName FROM customerLogin WHERE userName =" + "'" + custDetailsHMap.get("userName") + "'";
        //Pass through SQL Query to database class which returns the result set
        rs = Database.queryDatabase(sqlQuery);
        try{
            //If there is something in the result set then there was a matching username, return false.
            if(rs.next()){
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return true;
    }



    private boolean writeNewCustomer(HashMap custDetailsHMap){
        //The SQLite statements for inserting a new customers details
        String custDetailsSQL = "INSERT INTO customerDetails (custID, name, userName, address, phoneNo) values(?," +
                                    "'" + custDetailsHMap.get("name") + "'" + "," +
                                    "'" + custDetailsHMap.get("userName")+ "'" + "," +
                                    "'" + custDetailsHMap.get("address") + "'" + "," +
                                    "'" + custDetailsHMap.get("phoneNo") + "'" + ")";
        String custLoginSQL = "INSERT INTO customerLogin (custID, userName, password) values(?," +
                                "'" + custDetailsHMap.get("userName") + "'" + "," +
                                "'" + custDetailsHMap.get("password1") + "'" + ")";

        //Calling the function which will insert the data into the appropriate tables
        Database.updateDatabase(custDetailsSQL);
        Database.updateDatabase(custLoginSQL);

        return true;
    }


}