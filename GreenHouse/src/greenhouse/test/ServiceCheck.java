/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Hasitha Lakmal
 */
public class ServiceCheck {
    public static void main(String[] args) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            LinkedList<String> list = new LinkedList<>();
            while ((line = input.readLine()) != null) {
                list.add(line);
            }
            input.close();
            Set<String> services = new HashSet<>();
            for (int i = 3; i < list.size(); i++) {
                services.add(list.get(i).substring(0, 29).trim());
            }
            for (String service : services) {
                System.out.println(service);
            }
            if(services.contains("KMPlayer.exe")){
                System.out.println("Exist");
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
