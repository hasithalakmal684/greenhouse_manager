/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hasitha Lakmal
 */
public class Validator {
    
    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static boolean validatePassword(){
        return true;
    }
    
    public static boolean validateTelephone(String telephone){
        Pattern pattern = Pattern.compile("0[0-9]{9}");
        Matcher matcher = pattern.matcher(telephone);
        return matcher.matches();
    }
    
    public static boolean validateName(String name){
        Pattern pattern = Pattern.compile("^[\\p{L} \\.'\\-]+$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    
    public static boolean validateAddress(){
        return true;
    }
}
