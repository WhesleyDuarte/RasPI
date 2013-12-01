package moraes.hendrigo.controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * @since november, 30th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class MovementDetectorController extends GPIOBased {
	
	private GpioPinDigitalInput movimentDetect;
	private LEDController ledController;
	
	public MovementDetectorController(GpioController gpio, LEDController ledController){
		this.gpio = gpio;
		this.ledController = ledController;
	}

	@Override
	protected void setupPIN() {
		movimentDetect = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01);
		movimentDetect.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {

				try {
					boolean detected = event.getState().isHigh();
					ledController.turnLED(detected);
					if ( detected ){
						System.out.println("Movement DETECTED ! ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
	

}
