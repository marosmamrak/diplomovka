package sk.tuke.numericmethods.tools;

import java.math.RoundingMode;

/**
 * Some contraints for numeric methods.
 * 
 * @author Maros Mamrak
 *
 */
public class MethodConstraints {

	public static final int PRECISION = 10_000;
	public static final int EXTREME_PRECISION = 5_000;
	public static final int ROUNDING_PRECISION = 10;
	public static final int MAXIMUM_STEPS = 1000;
	public static final int FIRST_DIFFERENTIATION = 1;
	public static final int SECOND_DIFFERENTIATION = 2;
	
	public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

	public static final String VARIABLE = "x";
	public static final String FUNCTION_NAME = "f";
	public static final String DECIMAL_FORMAT = "#.##########";
	public static final String NO_ERROR = "no errors.\n";
	
	public static final String BAD_SYNTAX_ERROR = "error.badSyntax";
	public static final String INVALID_INTERVAL_ERROR = "error.invalidInterval";
	public static final String UNCONTINUITY_ERROR = "error.uncontinuity";
	public static final String START_TIMES_END_ERROR = "error.functionStartEnd";
	public static final String FUNCTIONAL_VALUES_ERROR = "error.functionValues";
	public static final String FIRST_DERIVATIVE_UNCONTINUITY_ERROR = "error.firstDerivateUncontinuity";
	public static final String FIRST_DERIVATE_SUPREMUM_ERROR = "error.firstDerivateSupremum";
	public static final String FIRST_DERIVATIVE_SIGNUM_ERROR = "error.badFirstDerivateSignum";
	public static final String SECOND_DERIVATIVE_UNCONTINUITY_ERROR = "error.secondDerivateUncontinuity";
	public static final String SECOND_DERIVATIVE_SIGNUM_ERROR = "error.badSecondDerivateSignum";
	public static final String TOO_MANY_STEPS_ERROR = "error.tooManySteps";
	
}
