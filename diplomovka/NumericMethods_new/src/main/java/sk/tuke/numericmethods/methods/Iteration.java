package sk.tuke.numericmethods.methods;

import static sk.tuke.numericmethods.tools.MethodConstraints.MAXIMUM_STEPS;

import java.util.ArrayList;
import java.util.List;

import org.mariuszgromada.math.mxparser.Function;

import sk.tuke.numericmethods.entities.FunctionEvaluationResult;
import sk.tuke.numericmethods.entities.IterationDataTableItem;

/**
 * Class for computing fixed point of function using iteration method.
 * 
 * @author Maros Mamrak
 *
 */
public class Iteration {

	/**
	 * Compute fixed point of function with defined tolerance using iteration method
	 * 
	 * @param evalResult evaluation result
	 * @param tolerance defined tolerance
	 * @return computation items
	 */
	public static List<IterationDataTableItem> compute(FunctionEvaluationResult evalResult, double tolerance) {
		Function function = evalResult.getFunction();
		
		double point = (evalResult.getStart() + evalResult.getEnd()) / 2;
		double next = function.calculate(point);
		
		double q = Math.max(Math.abs(evalResult.getFirstDerivateMin()), Math.abs(evalResult.getFirstDerivateMax()));
		double mistakeFactor = q / (1-q);
		
		List<IterationDataTableItem> items = new ArrayList<>();
		int step = 0;
		
		do {
			step++;
			items.add(new IterationDataTableItem(step, point, next));
			point = next;
			next = function.calculate(point);
			
		} while (tolerance < mistakeFactor * Math.abs(point - next) && step < MAXIMUM_STEPS);
		
		
		return items;
	}
	
}