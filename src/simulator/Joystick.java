/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author JB
 */
public class Joystick {
    public static String stErr;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        String pythonScriptPath = "/home/pi/mcp3008_joystick.py";
        String[] command = {"python", pythonScriptPath};

        Process p = Runtime.getRuntime().exec(command);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader inErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        
        System.out.println("Python script started");

        while (p.isAlive() == true) {
            String data = in.readLine();
            String[] parts = data.split(" ");
            
            String x = parts[2];
            String y = parts[6];
            String sel = parts[10];
            
            //System.out.println(data);
            System.out.println("X : " + x + " Y : " + y + " SEL: " + sel);
        }
        
        while ((stErr = inErr.readLine()) != null) {
            System.out.println("Error: " + stErr);
        }
    }
}
