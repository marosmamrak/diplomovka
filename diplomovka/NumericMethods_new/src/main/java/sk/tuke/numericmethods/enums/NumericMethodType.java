package sk.tuke.numericmethods.enums;

public enum NumericMethodType {

	BISECTION, ITERATION, NEWTON;
	
	private static final String LABEL_PREFIX = "index.method.";
	
	public String getLabel() {
		return LABEL_PREFIX + this.toString().toLowerCase();
	}
}
