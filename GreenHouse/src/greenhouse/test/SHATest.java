/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.test;

import java.security.MessageDigest;

/**
 *
 * @author Hasitha Lakmal
 */
public class SHATest {

    public static void main(String arg[]) throws Exception {
        String s = new String(encrypt("String"));
        
        System.out.println(s);
    }

    public static byte[] encrypt(String x) throws Exception {
        MessageDigest d = null;
        d = MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }
}
