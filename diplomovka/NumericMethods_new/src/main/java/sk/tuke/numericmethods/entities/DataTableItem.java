package sk.tuke.numericmethods.entities;

/**
 * Abstract class for dataTable item contains integer step
 * 
 * @author Maros Mamrak
 *
 */
public abstract class DataTableItem {

	private final int step;
	
	public DataTableItem(int step) {
		this.step = step;
	}

	public int getStep() {
		return step;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + step;
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
		DataTableItem other = (DataTableItem) obj;
		if (step != other.step)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataTableItem [step=" + step + "]";
	}
	
}
