package moraes.hendrigo.controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * @since december, 09th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class ButtonDistanceController extends GPIOBased {
	
	private GpioPinDigitalInput button;
	private DistanceController distanceController;
	private RelayController relayController;
	
	public ButtonDistanceController(GpioController gpio, DistanceController distanceController, RelayController relayController){
		this.gpio = gpio;
		this.distanceController = distanceController;
		this.relayController = relayController;
	}

	@Override
	protected void setupPIN() {
		button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07);
		button.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {

				try {
					if ( event.getState().isHigh()){
						int distanciaMedia = distanceController.readDistancia();
						if ( distanciaMedia > 100 ){ //if distance > 1 meter
							relayController.turnBlueLightSeconds();
						}else{
							relayController.turnYellowLightSeconds();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
	

}
