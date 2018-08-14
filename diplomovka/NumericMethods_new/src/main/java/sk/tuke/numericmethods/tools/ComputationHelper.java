package sk.tuke.numericmethods.tools;

import static sk.tuke.numericmethods.tools.MethodConstraints.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.mariuszgromada.math.mxparser.Function;

import sk.tuke.numericmethods.entities.Extremes;

/**
 * Class with static methods for computation help.
 * 
 * @author Maros Mamrak
 *
 */
public class ComputationHelper {
	
	/**
	 * Check syntax of function.
	 * 
	 * @param function function
	 * @return true if function syntax is valid otherwise false
	 */
	public static boolean checkSyntax(Function function) {
		function.checkSyntax();
		return function.getErrorMessage().endsWith(NO_ERROR);
	}
	
	/**
	 * Find extremes of functions
	 * 
	 * @param function function
	 * @param start start point of interval
	 * @param end end point of interval
	 * @return extremes
	 */
	public static Extremes findExtremes(Function function, double start, double end) {
		double stepX = (end - start) / EXTREME_PRECISION;
		double actualX = start;
		
		boolean error = false;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		while (actualX <= end) {
			double value = function.calculate(actualX);
			if (Double.isNaN(value)) {
				error = true;
			} else {
				min = value < min ? value : min;
				max = value > max ? value : max;
			}
			
			actualX += stepX;
		}
		
		return new Extremes(error, round(min), round(max));
	}
	
	/**
	 * Round value
	 * 
	 * @param value double value
	 * @return rounded value
	 */
	public static double round(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(ROUNDING_PRECISION, ROUNDING_MODE);
	    return bd.doubleValue();
	}
		
	/**
	 * Get first derivative function
	 * 
	 * @param functionString string representation of function
	 * @return first derivative function - f'(x)
	 */
	public static Function firstDerivative(String functionString) {
		return new Function(FUNCTION_NAME, firstDerivativeString(functionString), VARIABLE);
	}
	
	/**
	 * Get second derivative function in point
	 * 
	 * @param functionString string representation of function
	 * @param value point
	 * @return second derivative function - f''(value)
	 */
	public static Function secondDerivative(String functionString) {
		return new Function(FUNCTION_NAME, secondDerivativeString(functionString), VARIABLE);
	}
	
	/**
	 * Return decimal format for formating double for output dataTable.
	 * 
	 * @return decimal format
	 */
	public static DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT);
		df.setRoundingMode(ROUNDING_MODE);
		return df;
	}
	
	/**
	 * Return derivative form of function string
	 * 
	 * @param functionString function as string
	 * @return string representation of derivative
	 */
	private static String firstDerivativeString(String functionString) {
		return "der("+functionString+", " + VARIABLE + ")";
	}
	
	/**
	 * Return second derivative form of function string
	 * 
	 * @param functionString function as string
	 * @return string representation of second derivative
	 */
	private static String secondDerivativeString(String functionString) {
		return firstDerivativeString(firstDerivativeString(functionString));
	}

}
