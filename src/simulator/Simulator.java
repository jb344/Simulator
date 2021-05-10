package simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;
import simulator.PWMDevice.PWMChannel;

public class Simulator {

    private final static int SERVO_FREQUENCY = 50;
    private final static int SERVO_MIN = calculatePulseWidth(1, SERVO_FREQUENCY); //205
    private final static int SERVO_CENTERED = calculatePulseWidth(1.5, SERVO_FREQUENCY); //307
    private final static int SERVO_MAX = calculatePulseWidth(2, SERVO_FREQUENCY); //410
    private static String pythonScriptPath = "/home/pi/mcp3008_joystick.py";
    private static String[] command = {"python", pythonScriptPath};
    private static String stErr;

    public static void main(String[] args) throws InterruptedException, IOException {
        PWMDevice device = new PWMDevice();
        device.setPWMFreqency(SERVO_FREQUENCY);
        PWMChannel servo0 = device.getChannel(0);
        PWMChannel servo1 = device.getChannel(1);
        PWMChannel servo2 = device.getChannel(2);

        System.out.println("Setting start conditions...");
        servo0.setPWM(0, SERVO_CENTERED);
        servo1.setPWM(0, SERVO_CENTERED);
        
        Process p = Runtime.getRuntime().exec(command);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader inErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        
        System.out.println("Python script started");

        System.out.println("Running perpetual loop...");
        
        while (p.isAlive()) {
            String data = in.readLine();
            String[] parts = data.split(" ");
            
            String x = parts[2];
            String y = parts[6];
            String sel = parts[10];
            String combined = "X : " + x + " Y : " + y + " SEL: " + sel;
            
            int xInt = Integer.parseInt(x);
            int yInt = Integer.parseInt(y);
            int selInt = Integer.parseInt(sel);
            
            //TODO - SET PWM TO THE OUTPUT OF THE JOYSTICK
            servo0.setPWM(0, ThreadLocalRandom.current().nextInt(SERVO_MIN, SERVO_MAX + 1));
            servo1.setPWM(0, ThreadLocalRandom.current().nextInt(SERVO_MIN, SERVO_MAX + 1));
            servo2.setPWM(0, ThreadLocalRandom.current().nextInt(SERVO_MIN, SERVO_MAX + 1));
            
            Thread.sleep(400);
        }
        
        while ((stErr = inErr.readLine()) != null) {
            System.out.println("Error: " + stErr);
        }
    }

    private static int calculatePulseWidth(double millis, int frequency) {
        return (int) (Math.round(4096 * millis * frequency / 1000));
    }

}
