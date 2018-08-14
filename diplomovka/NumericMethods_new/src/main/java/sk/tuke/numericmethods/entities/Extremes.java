package sk.tuke.numericmethods.entities;

/**
 * Immutable max and min value entity.
 * 
 * @author Maros Mamrak
 *
 */
public class Extremes {

	private final boolean error;
	private final double min;
	private final double max;
	
	public Extremes(boolean error, double min, double max) {
		this.error = error;
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public boolean isError() {
		return error;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (error ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(max);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(min);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Extremes other = (Extremes) obj;
		if (error != other.error)
			return false;
		if (Double.doubleToLongBits(max) != Double.doubleToLongBits(other.max))
			return false;
		if (Double.doubleToLongBits(min) != Double.doubleToLongBits(other.min))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Extremes [error=" + error + ", min=" + min + ", max=" + max + "]";
	}
	
}
