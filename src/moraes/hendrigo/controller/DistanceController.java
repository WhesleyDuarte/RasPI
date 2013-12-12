package moraes.hendrigo.controller;

import java.math.BigDecimal;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * @since december, 04th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class DistanceController extends GPIOBased {
	
	private GpioPinDigitalOutput trigDistance; //GPIO5
	private GpioPinDigitalInput echoDistance; //GPIO4

	public DistanceController(GpioController gpio){
		this.gpio = gpio;
	}

	@Override
	protected void setupPIN() {
		trigDistance = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
		echoDistance = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04);
	}
	
	protected int readDistancia() throws Exception{
		long nanoIn=0;
		long nanoOut=0;
		int somaDistancias = 0;
		
		for(int i = 0; i < 5; i++){
			trigDistance.low();
			Thread.sleep(100);  //wait 100 milliseconds before start
			trigDistance.high();
			Thread.sleep(0, 10000);  //10000 nanosegundos = 10 microsegundos
			trigDistance.low();
	
			while ( echoDistance.isLow() ){
				//just wait until the echo get some value
			}
			nanoIn = System.nanoTime();
			while ( echoDistance.isHigh() ){
				//just wait until the echo change its value
			}
			nanoOut = System.nanoTime();
			
			long dur = (nanoOut - nanoIn);
			
			// Formula from Manual:   pulse width (uS-microsec) / 58 = distance(cm)   
			BigDecimal distancia = new BigDecimal( dur / 1000 / 58);  //divide by 1000 because   1 micro second = 1000 nanoseconds
			
			
			somaDistancias += distancia.intValue();
		}

		BigDecimal distanciaMedia = new BigDecimal( somaDistancias / 4);
		System.out.println("Distance: " + distanciaMedia + "cm" );
		
		return distanciaMedia.intValue();
	}

}
