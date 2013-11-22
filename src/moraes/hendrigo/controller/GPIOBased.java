package moraes.hendrigo.controller;

import com.pi4j.io.gpio.GpioController;

/**
 * @since november, 12th, 2013
 * @author Hendrigo Moraes (henmoraes@hotmail.com)
 *
 */
public abstract class GPIOBased {
	protected GpioController gpio = null;
	
	protected abstract void setupPIN();
	
	public final void init(){
		System.out.println("Starting " + this.getClass().getSimpleName() + "...");
		setupPIN();
		System.out.println(this.getClass().getSimpleName() + " started !");
	}
}
