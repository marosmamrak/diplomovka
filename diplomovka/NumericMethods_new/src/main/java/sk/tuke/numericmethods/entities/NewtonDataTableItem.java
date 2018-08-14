package sk.tuke.numericmethods.entities;

import java.text.DecimalFormat;

import sk.tuke.numericmethods.tools.ComputationHelper;

/**
 * Immutable class for Newton dataTable item contains point and functional value in point,
 * derivative at point and next point
 * 
 * @author Maros Mamrak
 *
 */
public class NewtonDataTableItem extends IterationDataTableItem {

	private final String derivatePoint;
	private final String nextPoint;
	
	public NewtonDataTableItem(int step, double point, double fPoint, double derivatePoint, double nextPoint) {
		super(step, point, fPoint);
		DecimalFormat df = ComputationHelper.getDecimalFormat();
		this.derivatePoint = df.format(derivatePoint);
		this.nextPoint = df.format(nextPoint);
	}

	public String getDerivatePoint() {
		return derivatePoint;
	}

	public String getNextPoint() {
		return nextPoint;
	}
}
