package sk.tuke.numericmethods.methods;

import static sk.tuke.numericmethods.tools.MethodConstraints.MAXIMUM_STEPS;

import java.util.ArrayList;
import java.util.List;

import org.mariuszgromada.math.mxparser.Function;

import sk.tuke.numericmethods.entities.FunctionEvaluationResult;
import sk.tuke.numericmethods.entities.NewtonDataTableItem;

/**
 * Class for computing zero point of function using Newton's method.
 * 
 * @author Maros Mamrak
 *
 */
public class Newton {

	/**
	 * Compute zero point of function with defined tolerance using Newton's
	 * method
	 * 
	 * @param evalResult
	 *            evaluation result
	 * @param tolerance
	 *            defined tolerance
	 * @return computation items
	 */
	public static List<NewtonDataTableItem> compute(FunctionEvaluationResult evalResult, double tolerance) {
		Function function = evalResult.getFunction();
		Function derivative = evalResult.getFirstDerivative();
		Function secondDerivative = evalResult.getFirstDerivative();
		double start = evalResult.getStart();

		start = function.calculate(start) * secondDerivative.calculate(start) > 0 ? start : evalResult.getEnd();
		double m = Math.min(Math.abs(evalResult.getFirstDerivateMin()), Math.abs(evalResult.getFirstDerivateMax()));

		List<NewtonDataTableItem> items = new ArrayList<>();
		int step = 0;
		double fStart;

		do {
			step++;
			fStart = function.calculate(start);
			double derivateStart = derivative.calculate(start);
			double next = start - (fStart / derivateStart);
			
			items.add(new NewtonDataTableItem(step, start, fStart, derivateStart, next));
			start = next;

		} while (tolerance < Math.abs(fStart) / m && step < MAXIMUM_STEPS);

		return items;
	}

}
