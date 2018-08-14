package sk.tuke.numericmethods.methods;

import java.util.ArrayList;
import java.util.List;

import org.mariuszgromada.math.mxparser.Function;

import sk.tuke.numericmethods.entities.BisectionDataTableItem;
import sk.tuke.numericmethods.entities.FunctionEvaluationResult;

import static sk.tuke.numericmethods.tools.MethodConstraints.*;

/**
 * Class for computing zero value of function using bisection method.
 * 
 * @author Maros Mamrak
 *
 */
public class Bisection {
	
	/**
	 * Compute zero point of function with defined tolerance using bisection method
	 * 
	 * @param evalResult evaluation result
	 * @param tolerance defined tolerance
	 * @return computation items
	 */
	public static List<BisectionDataTableItem> compute(FunctionEvaluationResult evalResult, double tolerance) {
		Function function = evalResult.getFunction();
		double start = evalResult.getStart();
		double end = evalResult.getEnd();
		
		List<BisectionDataTableItem> items = new ArrayList<>();
		double fHalf, length; 
		int step = 0;
		
		do {
			length = (end - start) / 2;
			double half = start + length;
			step++;
			double fStart = function.calculate(start);
			double fEnd = function.calculate(end);
			fHalf = function.calculate(half);
			items.add(new BisectionDataTableItem(step, start, end, half, fStart, fEnd, fHalf));
			
			if (fStart*fHalf < 0) {
				end = half;
			} else {
				start = half;
			}
			
		} while (fHalf != 0 && length > tolerance && step < MAXIMUM_STEPS);
		
		
		return items;
	}
	
}
