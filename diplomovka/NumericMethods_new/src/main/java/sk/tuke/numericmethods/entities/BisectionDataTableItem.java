package sk.tuke.numericmethods.entities;

import java.text.DecimalFormat;

import sk.tuke.numericmethods.tools.ComputationHelper;

/**
 * Immutable class for bisection dataTable item contains start, end and half
 * point of inteval and functional values.
 * 
 * @author Maros Mamrak
 *
 */
public class BisectionDataTableItem extends DataTableItem {

	private final String start;
	private final String end;
	private final String half;

	private final String fStart;
	private final String fEnd;
	private final String fHalf;

	public BisectionDataTableItem(int step, double start, double end, double half, double fStart, double fEnd,
			double fHalf) {
		super(step);
		DecimalFormat df = ComputationHelper.getDecimalFormat();
		this.start = df.format(start);
		this.end = df.format(end);
		this.half = df.format(half);
		this.fStart = df.format(fStart);
		this.fEnd = df.format(fEnd);
		this.fHalf = df.format(fHalf);
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public String getHalf() {
		return half;
	}

	public String getfStart() {
		return fStart;
	}

	public String getfEnd() {
		return fEnd;
	}

	public String getfHalf() {
		return fHalf;
	}
}
