package sk.tuke.numericmethods.entities;

import java.util.HashSet;
import java.util.Set;

import org.mariuszgromada.math.mxparser.Function;
import org.primefaces.model.chart.LineChartSeries;

import sk.tuke.numericmethods.enums.NumericMethodType;

/**
 * Entity which represents function evaluation result with some computation
 * (e.g. function maximum, derivative max etc.)
 * 
 * @author Maros Mamrak
 *
 */
public class FunctionEvaluationResult {

	private final String functionString;
	private final NumericMethodType methodType;

	private Function function;
	private Function firstDerivative;
	private Function secondDerivative;

	private final double start;
	private final double end;

	private double min;
	private double max;

	private Double firstDerivateMin;
	private Double firstDerivateMax;

	private LineChartSeries values;

	private final Set<String> errorMessages = new HashSet<>();

	public FunctionEvaluationResult(String functionString, NumericMethodType methodType, double start, double end) {
		this.functionString = functionString;
		this.methodType = methodType;
		this.start = start;
		this.end = end;
	}

	public String getFunctionString() {
		return functionString;
	}

	public NumericMethodType getMethodType() {
		return methodType;
	}

	public Function getFunction() {
		return function;
	}

	public Function getFirstDerivative() {
		return firstDerivative;
	}

	public Function getSecondDerivative() {
		return secondDerivative;
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public Double getFirstDerivateMin() {
		return firstDerivateMin;
	}

	public Double getFirstDerivateMax() {
		return firstDerivateMax;
	}

	public LineChartSeries getValues() {
		return values;
	}

	public Set<String> getErrorMessages() {
		return errorMessages;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public void setFirstDerivative(Function firstDerivative) {
		this.firstDerivative = firstDerivative;
	}

	public void setSecondDerivative(Function secondDerivative) {
		this.secondDerivative = secondDerivative;
	}

	public void setFirstDerivateMin(Double firstDerivateMin) {
		this.firstDerivateMin = firstDerivateMin;
	}

	public void setFirstDerivateMax(Double firstDerivateMax) {
		this.firstDerivateMax = firstDerivateMax;
	}

	public void setValues(LineChartSeries values) {
		this.values = values;
	}

	public void addErrorMessage(String error) {
		errorMessages.add(error);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(end);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((functionString == null) ? 0 : functionString.hashCode());
		result = prime * result + ((methodType == null) ? 0 : methodType.hashCode());
		temp = Double.doubleToLongBits(start);
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
		FunctionEvaluationResult other = (FunctionEvaluationResult) obj;
		if (Double.doubleToLongBits(end) != Double.doubleToLongBits(other.end))
			return false;
		if (functionString == null) {
			if (other.functionString != null)
				return false;
		} else if (!functionString.equals(other.functionString))
			return false;
		if (methodType != other.methodType)
			return false;
		if (Double.doubleToLongBits(start) != Double.doubleToLongBits(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FunctionEvaluation [functionString=" + functionString + ", methodType=" + methodType + ", start="
				+ start + ", end=" + end + ", min=" + min + ", max=" + max + ", firstDerivateMin=" + firstDerivateMin
				+ ", firstDerivateMax=" + firstDerivateMax + ", errorMessages=" + errorMessages + "]";
	}
}
