package moraes.hendrigo.controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * @since november, 30th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class LEDController extends GPIOBased {
	
	private GpioPinDigitalOutput led;
	
	public LEDController(GpioController gpio){
		this.gpio = gpio;
	}

	@Override
	protected void setupPIN() {
		led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
	}
	
	protected void turnLED(boolean on){
		led.setState(on);
	}
	

}
