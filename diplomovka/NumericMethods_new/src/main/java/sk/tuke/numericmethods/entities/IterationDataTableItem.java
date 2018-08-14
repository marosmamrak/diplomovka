package sk.tuke.numericmethods.entities;

import java.text.DecimalFormat;

import sk.tuke.numericmethods.tools.ComputationHelper;

/**
 * Immutable class for iteration dataTable item contains point and functional value in point
 * 
 * @author Maros Mamrak
 *
 */
public class IterationDataTableItem extends DataTableItem {

	private final String point;
	private final String fPoint;

	public IterationDataTableItem(int step, double point, double fPoint) {
		super(step);
		DecimalFormat df = ComputationHelper.getDecimalFormat();
		this.point = df.format(point);
		this.fPoint = df.format(fPoint);
	}

	public String getPoint() {
		return point;
	}

	public String getfPoint() {
		return fPoint;
	}
}
