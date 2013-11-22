package moraes.hendrigo.app;

import java.util.Scanner;

import moraes.hendrigo.controller.TemperatureController;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

/**
 * @since november, 12th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class StartApp {
	public static void main(String[] args) {
		System.out.println("Starting app...");
		GpioController gpio = null;
		try{
			System.out.println("Initializing GPIOController...");
			gpio = GpioFactory.getInstance();
			System.out.println("GPIOController initialized !");
			
			
			new TemperatureController(gpio).init();
			
			
			System.out.println("App started !");

			//just wait a keypress to finish the app
			Scanner scanIn = new Scanner(System.in);
			scanIn.next();
			scanIn.close();            

			System.out.println("Finishing...");
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				gpio.shutdown();
			}catch (Exception e) {
				//do nothing
			}
		}
		System.exit(0);
	}
}
