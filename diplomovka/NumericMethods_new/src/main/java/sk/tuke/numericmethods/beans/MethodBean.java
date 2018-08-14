package sk.tuke.numericmethods.beans;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import org.mariuszgromada.math.mxparser.Function;

import sk.tuke.numericmethods.entities.BisectionDataTableItem;
import sk.tuke.numericmethods.entities.DataTableItem;
import sk.tuke.numericmethods.entities.FunctionEvaluationResult;
import sk.tuke.numericmethods.entities.IterationDataTableItem;
import sk.tuke.numericmethods.entities.NewtonDataTableItem;
import sk.tuke.numericmethods.enums.NumericMethodType;
import sk.tuke.numericmethods.methods.Bisection;
import sk.tuke.numericmethods.methods.Iteration;
import sk.tuke.numericmethods.methods.Newton;
import sk.tuke.numericmethods.tools.FunctionEvaluator;
import sk.tuke.numericmethods.tools.MethodConstraints;

/**
 * Bean for creating graph and choosing a computation method.
 * 
 * @author Maros Mamrak
 *
 */
@ManagedBean(name = "method")
@SessionScoped
public class MethodBean {

	private static final String INIT_FUNCTION = "x^3-x-1";
	private static final String ITERATION_INIT_FUNCTION = "(x+1)^(1/3)";
	private static final double INIT_START = 1;
	private static final double INIT_END = 2;
	private static final double INIT_TOLERANCE = 1e-6;

	@ManagedProperty("#{msg}")
	private ResourceBundle msg;

	private NumericMethodType methodType;
	private String functionString;
	private Function function;
	private double intervalStart;
	private double intervalEnd;
	private double tolerance;
	private LineChartModel chart;

	private List<BisectionDataTableItem> bisectionItems;
	private BisectionDataTableItem selectedBisectionItem;

	private List<IterationDataTableItem> iterationItems;
	private IterationDataTableItem selectedIterationItem;

	private List<NewtonDataTableItem> newtonItems;
	private NewtonDataTableItem selectedNewtonItem;

	private boolean showData;

	@PostConstruct
	public void init() {
		functionString = INIT_FUNCTION;
		intervalStart = INIT_START;
		intervalEnd = INIT_END;
		tolerance = INIT_TOLERANCE;
		methodType = NumericMethodType.BISECTION;
		showData = true;

		compute();
	}

	/**
	 * Evaluate function and conditions for chosen method, create graph and
	 * compute data with chosen method
	 */
	public void compute() {
		FunctionEvaluationResult evalResult = FunctionEvaluator.evaluate(functionString, methodType, intervalStart,
				intervalEnd);
		if (evalResult.getValues() == null) {
			showData = false;
		} else {
			showData = true;
			createGraph(evalResult.getValues(), intervalStart, intervalEnd);
		}

		if (!evalResult.getErrorMessages().isEmpty()) {
			for (String error : evalResult.getErrorMessages()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString(error), null);
				FacesContext.getCurrentInstance().addMessage(null, message);
			}

		} else {
			function = evalResult.getFunction();

			switch (methodType) {
			case BISECTION:
				bisectionItems = Bisection.compute(evalResult, tolerance);
				addTooManyStepsWarning(bisectionItems);
				break;
			case ITERATION:
				iterationItems = Iteration.compute(evalResult, tolerance);
				addTooManyStepsWarning(iterationItems);
				addSeriesForIteration(evalResult);
				break;
			case NEWTON:
				newtonItems = Newton.compute(evalResult, tolerance);
				addTooManyStepsWarning(bisectionItems);
				break;
			default:
				throw new IllegalArgumentException("Method type " + methodType + "is unknown!");
			}
		}
	}

	/**
	 * Add warning if computation finished in maximum steps
	 * 
	 * @param dataItems
	 *            data table items
	 */
	private <T extends DataTableItem> void addTooManyStepsWarning(List<T> dataItems) {
		if (dataItems != null && dataItems.size() >= MethodConstraints.MAXIMUM_STEPS) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					msg.getString(MethodConstraints.TOO_MANY_STEPS_ERROR), null));
		}
	}

	/**
	 * Create chart line model with series of point from functional values.
	 * 
	 * @param series
	 *            series of points
	 */
	private void createGraph(LineChartSeries series, double start, double end) {
		series.setLabel(msg.getString(
				methodType != NumericMethodType.ITERATION ? "index.graph.function.f" : "index.graph.function.phi"));
		series.setShowMarker(false);
		series.setSmoothLine(true);

		chart = new LineChartModel();

		if (methodType != NumericMethodType.ITERATION) {
			LineChartSeries axeX = new LineChartSeries();
			axeX.setLabel(msg.getString("index.graph.function.x"));
			axeX.setShowMarker(false);
			axeX.set(start, 0);
			axeX.set(end, 0);
			chart.addSeries(axeX);
		}

		chart.addSeries(series);
		chart.setTitle(msg.getString(getChartTitleMessage()));
		chart.setLegendPosition("se");
	}

	/**
	 * Create y=x series for iteration method
	 * 
	 * @param evalResult
	 *            function evaluation result
	 */
	private void addSeriesForIteration(FunctionEvaluationResult evalResult) {
		LineChartSeries series = new LineChartSeries();
		series.setLabel(msg.getString("index.graph.function.yx"));
		series.set(evalResult.getMin(), evalResult.getMin());
		series.set(evalResult.getMax(), evalResult.getMax());
		series.setShowMarker(false);
		chart.addSeries(series);
	}

	private String getChartTitleMessage() {
		return "index.graph." + methodType.toString().toLowerCase();
	}

	/**
	 * Redraw graph for chosen interval from bisection method
	 */
	public void onBisectionRowSelect() {
		double start = Double.parseDouble(selectedBisectionItem.getStart());
		double end = Double.parseDouble(selectedBisectionItem.getEnd());
		LineChartSeries series = FunctionEvaluator.createFunctionChartSeries(function, start, end);
		createGraph(series, start, end);
	}

	/**
	 * Change function for chosen method if function is initial.
	 */
	public void onMethodTypeChange() {
		switch (methodType) {
		case BISECTION:
		case NEWTON:
			functionString = functionString.equals(ITERATION_INIT_FUNCTION) ? INIT_FUNCTION : functionString;
			break;
		case ITERATION:
			functionString = functionString.equals(INIT_FUNCTION) ? ITERATION_INIT_FUNCTION : functionString;
			break;
		default:
			throw new IllegalArgumentException("Method type " + methodType + "is unknown!");
		}
	}

	/**
	 * Add point [x, f(x)] and [f(x), f(f(x))]
	 */
	public void onIterationRowSelect() {
		List<ChartSeries> series = chart.getSeries();
		if (series.size() == 4) {
			series.remove(3);
			series.remove(2);
		}

		double point = Double.parseDouble(selectedIterationItem.getPoint());
		double fPoint = Double.parseDouble(selectedIterationItem.getfPoint());

		LineChartSeries xSerie = new LineChartSeries();
		xSerie.setLabel(msg.getString("index.graph.function.point"));
		xSerie.set(point, fPoint);
		chart.addSeries(xSerie);

		LineChartSeries fxSerie = new LineChartSeries();
		fxSerie.setLabel(msg.getString("index.graph.function.nextPoint"));
		fxSerie.set(fPoint, function.calculate(fPoint));
		chart.addSeries(fxSerie);
	}

	/**
	 * Redraw graph for chosen step from Newton's method
	 */
	public void onNewtonRowSelect() {
		List<ChartSeries> series = chart.getSeries();
		if (series.size() == 3) {
			series.remove(2);
		}
		
		double point = Double.parseDouble(selectedNewtonItem.getPoint());
		double fPoint = Double.parseDouble(selectedNewtonItem.getfPoint());
		double next = Double.parseDouble(selectedNewtonItem.getNextPoint());
		
		LineChartSeries tangentSerie = new LineChartSeries();
		tangentSerie.setLabel(msg.getString("index.graph.function.tangent"));
		tangentSerie.set(point, fPoint);
		tangentSerie.set(next, 0);
		chart.addSeries(tangentSerie);
	}

	public NumericMethodType[] getNumericMethodTypes() {
		return NumericMethodType.values();
	}

	public NumericMethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(NumericMethodType methodType) {
		this.methodType = methodType;
	}

	public String getFunctionString() {
		return functionString;
	}

	public void setFunctionString(String functionString) {
		this.functionString = functionString;
	}

	public double getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(double intervalStart) {
		this.intervalStart = intervalStart;
	}

	public double getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(double intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public LineChartModel getChart() {
		return chart;
	}

	public BisectionDataTableItem getSelectedBisectionItem() {
		return selectedBisectionItem;
	}

	public void setSelectedBisectionItem(BisectionDataTableItem selectedBisectionItem) {
		this.selectedBisectionItem = selectedBisectionItem;
	}

	public List<BisectionDataTableItem> getBisectionItems() {
		return bisectionItems;
	}

	public List<IterationDataTableItem> getIterationItems() {
		return iterationItems;
	}

	public IterationDataTableItem getSelectedIterationItem() {
		return selectedIterationItem;
	}

	public void setSelectedIterationItem(IterationDataTableItem selectedIterationItem) {
		this.selectedIterationItem = selectedIterationItem;
	}

	public List<NewtonDataTableItem> getNewtonItems() {
		return newtonItems;
	}

	public NewtonDataTableItem getSelectedNewtonItem() {
		return selectedNewtonItem;
	}

	public void setSelectedNewtonItem(NewtonDataTableItem selectedNewtonItem) {
		this.selectedNewtonItem = selectedNewtonItem;
	}

	public void setMsg(ResourceBundle msg) {
		this.msg = msg;
	}

	public boolean isShowData() {
		return showData;
	}

}