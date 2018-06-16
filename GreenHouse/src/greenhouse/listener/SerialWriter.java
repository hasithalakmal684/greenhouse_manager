/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.listener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Hasitha Lakmal
 */
public class SerialWriter implements Runnable {

    OutputStream out;
    InputStream in = System.in;

    public SerialWriter(OutputStream out) {
        this.out = out;
    }

    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        System.out.println("Writing Started...");
        try {
            int c = 0;
            while ((c = in.read()) > -1) {
                this.out.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Writing ended...");
    }
}
