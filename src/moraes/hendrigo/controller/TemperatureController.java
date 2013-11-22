package moraes.hendrigo.controller;

import java.text.DecimalFormat;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * @since november, 12th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public class TemperatureController extends GPIOBased {
	
	private DecimalFormat df = new DecimalFormat();
	
	private GpioPinDigitalOutput clockpin;
	private GpioPinDigitalOutput mosipin; 
	private GpioPinDigitalInput misopin;
	private GpioPinDigitalOutput cspin;
	
	public TemperatureController(GpioController gpio){
		this.gpio = gpio;
		df.setMaximumFractionDigits(1);	
		df.setMinimumFractionDigits(1);
	}

	@Override
	protected void setupPIN() {
		cspin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10);
		mosipin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
		misopin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13);
		clockpin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14);
		
		new Thread( new RunReadTemp(this) ).start();
	}
	
	class RunReadTemp implements Runnable{
		
		TemperatureController temperatureController;
		
		public RunReadTemp(TemperatureController temperatureController){
			this.temperatureController = temperatureController;
		}
		
		@Override
		public void run() {
			while(true){
				try{
					Thread.sleep(5000);  //every 5 seconds, read the temperature
					temperatureController.readTemperature();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void readTemperature() throws Exception{
		System.out.println("Reading Temperature...");
		int times = 6;
		double sumtemp_C = 0;
		double sumtemp_F = 0;
		double millivolts = 0;
		for(int i=0; i < times; i++){
			int read_adc0 = readADC(0);

			// convert analog reading to millivolts = ADC * ( 3300 / 1024 )
			millivolts = read_adc0 * ( 3300.0 / 1024.0);

			// Temp in °C = [(Vout in mV) - 500] / 10 
			double temp_C = (millivolts - 500.0) / 10.0;
			sumtemp_C += temp_C;
			
			// convert celsius to fahrenheit 
			double temp_F = ( temp_C * 1.8 ) + 32;
			sumtemp_F += temp_F;

		}
		
		System.out.println("mv: " + millivolts );
		System.out.println("temp C: " + df.format(sumtemp_C / times) );
		System.out.println("temp F: " + df.format(sumtemp_F / times));
	}

	private int readADC(int adcCH) throws Exception{
		if ( adcCH > 7 || adcCH < 0 ){
			return -1;
		}

		cspin.high();

		clockpin.low(); 
		cspin.low(); 

		int commandout = adcCH;
		commandout |= 0x18;  
		commandout <<= 3;    
		for (int i=0; i < 5; i++){
			if ( (commandout & 0x80) == 128 ){
				mosipin.high();
			}else{   
				mosipin.low();
			}
			commandout <<= 1;
			clockpin.high();
			clockpin.low();
		}

		int adcout = 0;
		for( int i=0; i < 12; i++){
			clockpin.high();
			clockpin.low();
			adcout <<= 1;
			if (misopin.isHigh() ){
				adcout |= 0x1;
			}
		}
		cspin.high();

		adcout /= 2;       

		return adcout;
	}

}
