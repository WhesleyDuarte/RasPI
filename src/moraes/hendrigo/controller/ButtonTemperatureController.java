package moraes.hendrigo.controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * @since november, 26th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class ButtonTemperatureController extends GPIOBased {
	
	private GpioPinDigitalInput button;
	private TemperatureController temperatureController;
	
	public ButtonTemperatureController(GpioController gpio, TemperatureController temperatureController){
		this.gpio = gpio;
		this.temperatureController = temperatureController;
	}

	@Override
	protected void setupPIN() {
		button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00);
		button.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {

				try {
					if ( event.getState().isHigh()){
						temperatureController.readTemperature();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
	

}
