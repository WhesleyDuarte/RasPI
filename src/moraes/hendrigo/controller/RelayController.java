package moraes.hendrigo.controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * @since december, 16th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class RelayController extends GPIOBased {
	
	private GpioPinDigitalOutput relayYellow;
	private GpioPinDigitalOutput relayBlue;

	public RelayController(GpioController gpio){
		this.gpio = gpio;
	}

	@Override
	protected void setupPIN() {
		relayYellow = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
		relayYellow.setShutdownOptions(true, PinState.HIGH);
		relayYellow.high();
		
		relayBlue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
		relayBlue.setShutdownOptions(true, PinState.HIGH);
		relayBlue.high();
	}
	
	protected void turnLightSeconds(GpioPinDigitalOutput gpioPinDigital) throws Exception{
		gpioPinDigital.low();
		Thread.sleep(3000);
		gpioPinDigital.high();
	}
	
	protected void turnYellowLightSeconds() throws Exception{
		System.out.println("Turning on Yellow light...");
		turnLightSeconds(relayYellow);
	}
	
	protected void turnBlueLightSeconds() throws Exception{
		System.out.println("Turning on Blue light...");
		turnLightSeconds(relayBlue);
	}

}
