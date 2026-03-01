package mainApp;

import java.awt.Color;

/**
 * Class: ElectricBarrier
 * Purpose: implements the electric barrier object
 */
public class ElectricBarrier extends Barrier{

	public static final Color ELECTRIC_BARRIER_COLOR = Color.ORANGE;
	
	/**
	 * ensures: constructs the electric barrier
	 * @param gameComponent
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public ElectricBarrier(GameComponent gameComponent, double x1, double y1, double x2, double y2){
		super(gameComponent, x1, y1, x2, y2, ELECTRIC_BARRIER_COLOR);
	}
}
