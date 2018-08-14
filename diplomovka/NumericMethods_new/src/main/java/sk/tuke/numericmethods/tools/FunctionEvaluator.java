package sk.tuke.numericmethods.tools;

import org.mariuszgromada.math.mxparser.Function;
import org.primefaces.model.chart.LineChartSeries;

import sk.tuke.numericmethods.entities.Extremes;
import sk.tuke.numericmethods.entities.FunctionEvaluationResult;
import sk.tuke.numericmethods.enums.NumericMethodType;

import static sk.tuke.numericmethods.tools.MethodConstraints.*;

/**
 * Class for function evaluation for method.
 * 
 * @author Maros Mamrak
 *
 */
public class FunctionEvaluator {

	/**
	 * Evaluate all conditions for chosen numerical method.
	 * 
	 * @param functionString
	 *            function
	 * @param methodType
	 *            numeric method type
	 * @param start
	 *            interval start point
	 * @param end
	 *            interval end point
	 * @return evaluation valuation result
	 */
	public static FunctionEvaluationResult evaluate(String functionString, NumericMethodType methodType, double start,
			double end) {
		FunctionEvaluationResult result = new FunctionEvaluationResult(functionString, methodType, start, end);

		Function function = new Function(FUNCTION_NAME, functionString, VARIABLE);
		result.setFunction(function);

		if (!ComputationHelper.checkSyntax(function)) {
			result.addErrorMessage(BAD_SYNTAX_ERROR);
		}

		if (start >= end) {
			result.addErrorMessage(INVALID_INTERVAL_ERROR);
		}
		
		if (!result.getErrorMessages().isEmpty()) {
			// contains fatal error
			return result;
		}
		
		switch (methodType) {
		case BISECTION:
			evaluateFunctionValues(result);
			evaluateStartEndFunctionValues(result);
			break;
		case ITERATION:
			evaluateFunctionValues(result);
			evaluateFirstDerivative(result);
			break;
		case NEWTON:
			evaluateFunctionValues(result);
			evaluateStartEndFunctionValues(result);
			evaluateFirstDerivative(result);
			evaluateSecondDerivative(result);
			break;
		default:
			throw new IllegalArgumentException("Method type " + methodType + " is unknown!");
		}
		
		return result;
	}

	/**
	 * Evaluate if f(start) * f(end) < 0
	 * 
	 * @param result evaluation result
	 */
	private static void evaluateStartEndFunctionValues(FunctionEvaluationResult result) {
		double startValue = result.getFunction().calculate(result.getStart());
		double endValue = result.getFunction().calculate(result.getEnd());
		if (!Double.isNaN(startValue) && !Double.isNaN(endValue) && startValue * endValue >= 0) {
			result.addErrorMessage(START_TIMES_END_ERROR);
		}
	}

	/**
	 * Evaluate conditions for first derivative (f'(x) have same signum for
	 * Newton method and |phi'(x)| <= q < 1 for Iteration method)
	 * 
	 * @param result
	 *            evaluation result
	 */
	private static void evaluateFirstDerivative(FunctionEvaluationResult result) {
		Function firstDerivative = ComputationHelper.firstDerivative(result.getFunctionString());
		result.setFirstDerivative(firstDerivative);
		Extremes firstDerivativeExtremes = ComputationHelper.findExtremes(firstDerivative, result.getStart(),
				result.getEnd());
		double derMin = firstDerivativeExtremes.getMin();
		double derMax = firstDerivativeExtremes.getMax();
		if (firstDerivativeExtremes.isError()) {
			result.addErrorMessage(FIRST_DERIVATIVE_UNCONTINUITY_ERROR);
		} else {
			result.setFirstDerivateMin(derMin);
			result.setFirstDerivateMax(derMax);

			if (result.getMethodType() == NumericMethodType.ITERATION
					&& Math.max(Math.abs(derMin), Math.abs(derMax)) >= 1) {
				// condition |phi'(x)| <= q < 1
				result.addErrorMessage(FIRST_DERIVATE_SUPREMUM_ERROR);
			}

			if (result.getMethodType() == NumericMethodType.NEWTON
					&& firstDerivativeExtremes.getMin() * firstDerivativeExtremes.getMax() < 0) {
				// different signum in min and max
				result.addErrorMessage(FIRST_DERIVATIVE_SIGNUM_ERROR);
			}
		}

	}

	/**
	 * Evaluate condition for second derivative (f''(x) have same signum)
	 * 
	 * @param result
	 *            evaluation result
	 */
	private static void evaluateSecondDerivative(FunctionEvaluationResult result) {
		Function secondDerivative = ComputationHelper.secondDerivative(result.getFunctionString());
		result.setSecondDerivative(secondDerivative);
		Extremes secondDerivativeExtremes = ComputationHelper.findExtremes(secondDerivative, result.getStart(),
				result.getEnd());
		if (secondDerivativeExtremes.isError()) {
			result.addErrorMessage(SECOND_DERIVATIVE_UNCONTINUITY_ERROR);
		} else if (secondDerivativeExtremes.getMin() * secondDerivativeExtremes.getMax() < 0) {
			// different signum in min and max
			result.addErrorMessage(SECOND_DERIVATIVE_SIGNUM_ERROR);
		}
	}

	/**
	 * Evaluate function values, continuity, f(start)*f(end) < 0 and fill values
	 * 
	 * @param result
	 */
	private static void evaluateFunctionValues(FunctionEvaluationResult result) {
		Function function = result.getFunction();

		LineChartSeries series = new LineChartSeries();
		Extremes extremes = fillFunctionChartSeries(series, function, result.getStart(), result.getEnd());

		if (extremes.isError()) {
			result.addErrorMessage(UNCONTINUITY_ERROR);
		} else {
			result.setMin(extremes.getMin());
			result.setMax(extremes.getMax());
		}
		
		if (result.getMethodType() == NumericMethodType.ITERATION) {
			if (extremes.getMin() < result.getStart() || extremes.getMax() > result.getEnd())
			result.addErrorMessage(FUNCTIONAL_VALUES_ERROR);
		}

		result.setValues(series);
	}

	/**
	 * Create chart series for defined function in defined interval
	 * 
	 * @param function
	 *            function
	 * @param start
	 *            interval start
	 * @param end
	 *            interval end
	 * @return chart series
	 */
	public static LineChartSeries createFunctionChartSeries(Function function, double start, double end) {
		LineChartSeries series = new LineChartSeries();
		fillFunctionChartSeries(series, function, start, end);
		return series;
	}

	/**
	 * Fill chart series for defined function in defined interval
	 * 
	 * @param filling
	 *            series
	 * @param function
	 *            function
	 * @param start
	 *            interval start
	 * @param end
	 *            interval end
	 * @return function extremes
	 */
	private static Extremes fillFunctionChartSeries(LineChartSeries series, Function function, double start,
			double end) {
		boolean error = false;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		double stepX = (end - start) / PRECISION;
		double actualX = start;

		while (actualX <= end) {
			double value = function.calculate(actualX);
			if (Double.isNaN(value)) {
				error = true;
			} else {
				series.set(actualX, value);
				min = value < min ? value : min;
				max = value > max ? value : max;
			}

			actualX += stepX;
		}
		
		
		if (series.getData().isEmpty()) {
			series.set(start, 0);
		}

		return new Extremes(error, min, max);
	}
}
