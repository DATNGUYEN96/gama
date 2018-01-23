/*********************************************************************************************
 *
 * 'ChartDataSource.java, in plugin msi.gama.core, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gama.outputs.layers.charts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaList;
import msi.gama.util.IList;
import msi.gama.util.matrix.GamaMatrix;
import msi.gama.util.matrix.IMatrix;
import msi.gaml.expressions.IExpression;
import msi.gaml.expressions.ListExpression;
import msi.gaml.operators.Cast;
import msi.gaml.types.IType;
import msi.gaml.types.Types;

@SuppressWarnings ({ "rawtypes" })
public class ChartDataSource {

	public static final int DATA_TYPE_NULL = 0;
	public static final int DATA_TYPE_DOUBLE = 1;
	public static final int DATA_TYPE_LIST_DOUBLE_12 = 2;
	public static final int DATA_TYPE_LIST_DOUBLE_3 = 3;
	public static final int DATA_TYPE_LIST_DOUBLE_N = 4;
	public static final int DATA_TYPE_LIST_LIST_DOUBLE_12 = 5;
	public static final int DATA_TYPE_LIST_LIST_DOUBLE_3 = 6;
	public static final int DATA_TYPE_LIST_LIST_DOUBLE_N = 7;
	public static final int DATA_TYPE_LIST_LIST_LIST_DOUBLE = 8;
	public static final int DATA_TYPE_POINT = 9;
	public static final int DATA_TYPE_LIST_POINT = 10;
	public static final int DATA_TYPE_LIST_LIST_POINT = 11;
	public static final int DATA_TYPE_MATRIX_DOUBLE = 12;
	public static final int DATA_TYPE_MATRIX_POINT = 13;
	public static final int DATA_TYPE_MATRIX_LIST_DOUBLE = 14;

	IExpression value;

	IExpression valueyerr;
	IExpression valuexerr;
	IExpression valueyminmax;
	IExpression colorexp;
	IExpression sizeexp;
	IExpression markershapeexp;

	String uniqueMarkerName;
	String style = IKeyword.DEFAULT;

	// Object lastvalue;
	LinkedHashMap<String, ChartDataSeries> mySeries = new LinkedHashMap<String, ChartDataSeries>();
	ChartDataSet myDataset;
	boolean isCumulative = false;
	boolean isCumulativeY = false;
	boolean forceCumulative = false;
	boolean forceCumulativeY = false;
	boolean useMarker = true;
	boolean fillMarker = true;
	boolean showLine = true;
	boolean useSecondYAxis = false;

	boolean useSize = false;

	boolean useYErrValues = false;
	boolean useXErrValues = false;
	boolean useYMinMaxValues = false;
	boolean useColorExp = false;
	boolean useMarkerShapeExp = false;

	double lineThickness = 1.0;

	public boolean cloneMe(final IScope scope, final int chartCycle, final ChartDataSource source) {

		value = source.value;

		valueyerr = source.valueyerr;
		valuexerr = source.valuexerr;
		valueyminmax = source.valueyminmax;
		colorexp = source.colorexp;
		sizeexp = source.sizeexp;
		markershapeexp = source.markershapeexp;

		uniqueMarkerName = source.uniqueMarkerName;
		style = source.style;

		myDataset = source.myDataset;
		isCumulative = source.isCumulative;
		isCumulativeY = source.isCumulativeY;
		forceCumulative = source.forceCumulative;
		forceCumulativeY = source.forceCumulativeY;
		useMarker = source.useMarker;
		fillMarker = source.fillMarker;
		showLine = source.showLine;

		useSize = source.useSize;

		useYErrValues = source.useYErrValues;
		useXErrValues = source.useXErrValues;
		useYMinMaxValues = source.useYMinMaxValues;
		useColorExp = source.useColorExp;
		useMarkerShapeExp = source.useMarkerShapeExp;
		lineThickness = source.lineThickness;

		return true;
	}

	public ChartDataSource getClone(final IScope scope, final int chartCycle) {
		final ChartDataSource res = new ChartDataSource();
		res.cloneMe(scope, chartCycle, this);
		return res;
	}

	public IExpression getValueyerr() {
		return valueyerr;
	}

	public IExpression getValuexerr() {
		return valuexerr;
	}

	public IExpression getValueyminmax() {
		return valueyminmax;
	}

	public String getUniqueMarkerName() {
		return uniqueMarkerName;
	}

	public boolean isUseSize() {
		return useSize;
	}

	public void setUseSize(final boolean useSize) {
		this.useSize = useSize;
	}

	public void setLineThickness(final double thickness) {
		lineThickness = thickness;
	}

	public double getLineThickness() {
		return lineThickness;
	}

	public IExpression getColorexp() {
		return colorexp;
	}

	public boolean isUseYErrValues() {
		return useYErrValues;
	}

	public void setUseYErrValues(final boolean useYErrValues) {
		this.useYErrValues = useYErrValues;
	}

	public boolean isUseXErrValues() {
		return useXErrValues;
	}

	public void setUseXErrValues(final boolean useXErrValues) {
		this.useXErrValues = useXErrValues;
	}

	public boolean isUseYMinMaxValues() {
		return useYMinMaxValues;
	}

	public void setUseYMinMaxValues(final boolean useYMinMaxValues) {
		this.useYMinMaxValues = useYMinMaxValues;
	}

	public boolean isByCategory() {
		return this.getDataset().isByCategory();
	}

	public boolean isCommonXSeries() {
		return this.getDataset().isCommonXSeries();
	}

	public boolean isCommonYSeries() {
		return this.getDataset().isCommonYSeries();
	}

	public boolean isCumulative() {
		return isCumulative;
	}

	public void setCumulative(final IScope scope, final boolean isCumulative) {
		if (!forceCumulative)
			this.isCumulative = isCumulative;
	}

	public boolean isCumulativeY() {
		return isCumulativeY;
	}

	public void setCumulativeY(final IScope scope, final boolean isCumulative) {
		if (!forceCumulativeY)
			this.isCumulativeY = isCumulative;
		if (this.isCumulativeY)
			this.getDataset().setForceNoYAccumulate(false);
	}

	public void setForceCumulativeY(final IScope scope, final boolean b) {
		this.forceCumulativeY = b;

	}

	public void setForceCumulative(final IScope scope, final boolean b) {
		this.forceCumulative = b;

	}

	public ChartDataSet getDataset() {
		return myDataset;
	}

	public void setDataset(final IScope scope, final ChartDataSet myDataset) {
		this.myDataset = myDataset;
		if (myDataset.getStyle(scope) != null)
			this.setStyle(scope, myDataset.getStyle(scope));
	}

	public void setStyle(final IScope scope, final String stval) {
		style = stval;
	}

	public String getStyle(final IScope scope) {
		if (style == IKeyword.DEFAULT)
			return this.getDataset().getStyle(scope);
		return style;
	}

	public void setValueExp(final IScope scope, final IExpression expval) {
		value = expval;
	}

	public IExpression getValue() {
		return value;
	}

	public Object getValue(final IScope scope) throws GamaRuntimeException {
		Object o;
		if (value != null) {
			o = value.value(scope);
		} else {
			o = null;
		}
		if (o instanceof GamaList) { return Cast.asList(scope, o); }
		if (o instanceof GamaPoint) { return Cast.asPoint(scope, o); }
		return Cast.asFloat(scope, o);
	}

	// public int computeTypeOfData(final IScope scope, final IExpression expr) {
	// final IType type = expr.getType();
	// if (type == Types.NO_TYPE)
	// return DATA_TYPE_NULL;
	// if (Types.POINT.isAssignableFrom(type))
	// return DATA_TYPE_POINT;
	// if (Types.MATRIX.isAssignableFrom(type)) {
	// final IType ct = type.getContentType();
	// if (ct == Types.INT || ct == Types.FLOAT)
	// return DATA_TYPE_MATRIX_DOUBLE;
	// if (Types.POINT.isAssignableFrom(ct))
	// return DATA_TYPE_MATRIX_POINT;
	// if (Types.LIST.isAssignableFrom(ct))
	// return DATA_TYPE_MATRIX_LIST_DOUBLE;
	// return DATA_TYPE_MATRIX_DOUBLE;
	// }
	// if (Types.LIST.isAssignableFrom(type)) {
	// final IType contents = type.getContentType();
	// if (contents == Types.NO_TYPE || contents == Types.INT || contents == Types.FLOAT) {
	// if (expr instanceof ListExpression) {
	// final IExpression[] exprs = ((ListExpression) expr).getElements();
	// switch (exprs.length) {
	// case 0:
	// return this.DATA_TYPE_LIST_DOUBLE_N;
	// case 1:
	// return this.DATA_TYPE_LIST_DOUBLE_12;
	// case 2:
	// return this.DATA_TYPE_LIST_DOUBLE_12;
	// case 3:
	// return this.DATA_TYPE_LIST_DOUBLE_3;
	// default:
	// return this.DATA_TYPE_LIST_DOUBLE_N;
	// }
	// }
	// } else if (contents == Types.POINT) {
	// return this.DATA_TYPE_LIST_POINT;
	// } else if (Types.LIST.isAssignableFrom(contents)) {
	// final IType subContents = contents.getContentType();
	// if (Types.LIST.isAssignableFrom(subContents)) {
	// final IType subSubContents = subContents.getContentType();
	// if (Types.POINT == subSubContents)
	// return DATA_TYPE_LIST_LIST_POINT;
	// else if (Types.LIST.isAssignableFrom(subSubContents))
	// return DATA_TYPE_LIST_LIST_LIST_DOUBLE;
	// else if (expr instanceof ListExpression) {
	// final IExpression[] exprs = ((ListExpression) expr).getElements();
	// if (exprs.length > 0) {
	// final IExpression contentExpr = exprs[0];
	// if (contentExpr instanceof ListExpression) {
	// final IExpression[] contentExprs = ((ListExpression) contentExpr).getElements();
	// if (contentExprs.length > 0) {
	// final IExpression subcontentExpr = contentExprs[0];
	// if (subcontentExpr instanceof ListExpression) {
	// final IExpression[] subcontentExprs =
	// ((ListExpression) subcontentExpr).getElements();
	// switch (subcontentExprs.length) {
	// case 0:
	// return this.DATA_TYPE_LIST_LIST_DOUBLE_N;
	// case 1:
	// case 2:
	// return DATA_TYPE_LIST_LIST_DOUBLE_12;
	// case 3:
	// return DATA_TYPE_LIST_LIST_DOUBLE_3;
	// default:
	// return DATA_TYPE_LIST_LIST_DOUBLE_N;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// }
	// }
	// return this.DATA_TYPE_DOUBLE;
	// }

	public int computeTypeOfData(final IScope scope, final IExpression expr) {
		final IType type = expr.getType();
		if (type == Types.NO_TYPE)
			return DATA_TYPE_NULL;
		if (Types.POINT.isAssignableFrom(type))
			return DATA_TYPE_POINT;
		if (Types.MATRIX.isAssignableFrom(type)) {
			final IType ct = type.getContentType();
			if (ct == Types.INT || ct == Types.FLOAT)
				return DATA_TYPE_MATRIX_DOUBLE;
			if (Types.POINT.isAssignableFrom(ct))
				return DATA_TYPE_MATRIX_POINT;
			if (Types.LIST.isAssignableFrom(ct))
				return DATA_TYPE_MATRIX_LIST_DOUBLE;
			return DATA_TYPE_MATRIX_DOUBLE;
		}
		if (Types.LIST.isAssignableFrom(type)) {
			final IType contents = type.getContentType();
			if (contents == Types.NO_TYPE || contents == Types.INT || contents == Types.FLOAT) {
				if (expr instanceof ListExpression) {
					final IExpression[] exprs = ((ListExpression) expr).getElements();
					switch (exprs.length) {
						case 0:
							return this.DATA_TYPE_LIST_DOUBLE_N;
						case 1:
							return this.DATA_TYPE_LIST_DOUBLE_12;
						case 2:
							return this.DATA_TYPE_LIST_DOUBLE_12;
						case 3:
							return this.DATA_TYPE_LIST_DOUBLE_3;
						default:
							return this.DATA_TYPE_LIST_DOUBLE_N;
					}
				}
			} else if (contents == Types.POINT) {
				return this.DATA_TYPE_LIST_POINT;
			} else if (Types.LIST.isAssignableFrom(contents)) {
				final IType subContents = contents.getContentType();
				if (Types.LIST.isAssignableFrom(subContents)) {
					final IType subSubContents = subContents.getContentType();
					if (Types.POINT == subSubContents)
						return DATA_TYPE_LIST_LIST_POINT;
					else if (Types.LIST.isAssignableFrom(subSubContents))
						return DATA_TYPE_LIST_LIST_LIST_DOUBLE;
					else if (expr instanceof ListExpression) {
						final IExpression[] exprs = ((ListExpression) expr).getElements();
						if (exprs.length > 0) {
							final IExpression contentExpr = exprs[0];
							if (contentExpr instanceof ListExpression) {
								final IExpression[] contentExprs = ((ListExpression) contentExpr).getElements();
								if (contentExprs.length > 0) {
									final IExpression subcontentExpr = contentExprs[0];
									if (subcontentExpr instanceof ListExpression) {
										final IExpression[] subcontentExprs =
												((ListExpression) subcontentExpr).getElements();
										switch (subcontentExprs.length) {
											case 0:
												return this.DATA_TYPE_LIST_LIST_DOUBLE_N;
											case 1:
											case 2:
												return DATA_TYPE_LIST_LIST_DOUBLE_12;
											case 3:
												return DATA_TYPE_LIST_LIST_DOUBLE_3;
											default:
												return DATA_TYPE_LIST_LIST_DOUBLE_N;
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return this.DATA_TYPE_DOUBLE;
	}

	public int get_data_type(final IScope scope, final Object o) {
		// final int type = this.DATA_TYPE_NULL;
		if (o == null)
			return this.DATA_TYPE_NULL;
		if (o instanceof GamaPoint)
			return this.DATA_TYPE_POINT;
		if (o instanceof GamaMatrix) {
			final IMatrix l1value = Cast.asMatrix(scope, o);
			if (l1value.length(scope) == 0)
				return this.DATA_TYPE_MATRIX_DOUBLE;
			final Object o2 = l1value.get(scope, 0, 0);
			if (o2 instanceof GamaPoint)
				return this.DATA_TYPE_MATRIX_POINT;
			if (o2 instanceof GamaList)
				return this.DATA_TYPE_MATRIX_LIST_DOUBLE;
			return this.DATA_TYPE_MATRIX_DOUBLE;
		}
		if (o instanceof GamaList) {

			final IList l1value = Cast.asList(scope, o);
			if (l1value.length(scope) == 0)
				return this.DATA_TYPE_LIST_DOUBLE_N;
			final Object o2 = l1value.get(0);
			if (o2 instanceof GamaPoint)
				return this.DATA_TYPE_LIST_POINT;
			if (o2 instanceof GamaList) {
				final IList l2value = Cast.asList(scope, o2);
				if (l2value.length(scope) == 0)
					return this.DATA_TYPE_LIST_LIST_DOUBLE_N;
				final Object o3 = l2value.get(0);
				if (o3 instanceof GamaList)
					return this.DATA_TYPE_LIST_LIST_LIST_DOUBLE;
				if (o3 instanceof GamaPoint)
					return this.DATA_TYPE_LIST_LIST_POINT;
				if (l2value.length(scope) == 1)
					return this.DATA_TYPE_LIST_LIST_DOUBLE_12;
				if (l2value.length(scope) == 2)
					return this.DATA_TYPE_LIST_LIST_DOUBLE_12;
				if (l2value.length(scope) == 3)
					return this.DATA_TYPE_LIST_LIST_DOUBLE_3;
				if (l2value.length(scope) > 3)
					return this.DATA_TYPE_LIST_LIST_DOUBLE_N;
			}

			if (l1value.length(scope) == 1)
				return this.DATA_TYPE_LIST_DOUBLE_12;
			if (l1value.length(scope) == 2)
				return this.DATA_TYPE_LIST_DOUBLE_12;
			if (l1value.length(scope) == 3)
				return this.DATA_TYPE_LIST_DOUBLE_3;
			if (l1value.length(scope) > 3)
				return this.DATA_TYPE_LIST_DOUBLE_N;
		}
		return this.DATA_TYPE_DOUBLE;
	}

	// void updateseriewithvalue(final IScope scope, final ChartDataSeries myserie, final IExpression expr,
	// final int chartCycle, final HashMap barvalues, final int listvalue) {
	// final int type_val = this.computeTypeOfData(scope, expr);
	// final Object o = expr.value(scope);
	void updateseriewithvalue(final IScope scope, final ChartDataSeries myserie, final Object o, final int chartCycle,
			final HashMap barvalues, final int listvalue) {
		final int type_val = this.get_data_type(scope, o);
		// could move into outputs object... would be (a little) less complex.
		// But less factorisation...

		if (!this.isCumulative() && !this.isCumulativeY()) {
			myserie.clearValues(scope);
			myserie.startupdate(scope);

		}
		if (!this.isCommonYSeries()) {

			// series charts (series/bw/...)
			if (this.isCommonXSeries() & !this.isByCategory()) {
				if (this.isCumulative()) {
					// new cumulative Y value

					switch (type_val) {
						case ChartDataSource.DATA_TYPE_POINT: {
							final ILocation pvalue = Cast.asPoint(scope, o);
							myserie.addxysvalue(scope,
									getDataset().getXSeriesValues().get(getDataset().getCommonXIndex()), pvalue.getX(),
									pvalue.getY(), chartCycle, barvalues, listvalue);
							break;
						}
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
							final IList lvalue = Cast.asList(scope, o);
							if (lvalue.length(scope) == 0) {

							}
							if (lvalue.length(scope) == 1) {
								myserie.addxyvalue(scope,
										getDataset().getXSeriesValues().get(getDataset().getCommonXIndex()),
										Cast.asFloat(scope, lvalue.get(0)), chartCycle, barvalues, listvalue);
							}
							if (lvalue.length(scope) > 1) {
								myserie.addxysvalue(scope,
										getDataset().getXSeriesValues().get(getDataset().getCommonXIndex()),
										Cast.asFloat(scope, lvalue.get(0)), Cast.asFloat(scope, lvalue.get(1)),
										chartCycle, barvalues, listvalue);
							}
							break;

						}
						case ChartDataSource.DATA_TYPE_NULL: {
							// last value?
							break;
						}
						case ChartDataSource.DATA_TYPE_DOUBLE:
						default: {
							final Double dvalue = Cast.asFloat(scope, o);
							myserie.addxyvalue(scope,
									getDataset().getXSeriesValues().get(getDataset().getCommonXIndex()), dvalue,
									chartCycle, barvalues, listvalue);

							break;
						}

					}

				}
				if (!this.isCumulative()) {
					// new non cumulative y value
					// serie in the order of the dataset
					switch (type_val) {
						case ChartDataSource.DATA_TYPE_POINT: {
							final ILocation pvalue = Cast.asPoint(scope, o);
							myserie.addxysvalue(scope, getDataset().getXSeriesValues().get(0), pvalue.getX(),
									pvalue.getY(), chartCycle, barvalues, listvalue);

							break;
						}
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
							final IList l1value = Cast.asList(scope, o);
							for (int n1 = 0; n1 < l1value.size(); n1++) {
								final Object o2 = l1value.get(n1);
								while (n1 >= getDataset().getXSeriesValues().size())
									getDataset().updateXValues(scope, chartCycle, l1value.size());
								myserie.addxyvalue(scope, getDataset().getXSeriesValues().get(n1),
										Cast.asFloat(scope, o2), chartCycle, barvalues, listvalue);
							}
							break;

						}
						case ChartDataSource.DATA_TYPE_LIST_LIST_POINT:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_N: {
							final IList l1value = Cast.asList(scope, o);
							for (int n1 = 0; n1 < l1value.size(); n1++) {
								final Object o2 = l1value.get(n1);
								final IList lvalue = Cast.asList(scope, o2);
								if (lvalue.length(scope) == 1) {
									myserie.addxyvalue(scope, getDataset().getXSeriesValues().get(n1),
											Cast.asFloat(scope, lvalue.get(0)), chartCycle, barvalues, listvalue);

								}
								if (lvalue.length(scope) > 1) {
									myserie.addxysvalue(scope, getDataset().getXSeriesValues().get(n1),
											Cast.asFloat(scope, lvalue.get(0)), Cast.asFloat(scope, lvalue.get(1)),
											chartCycle, barvalues, listvalue);
								}

							}
							break;

						}
						case ChartDataSource.DATA_TYPE_NULL: {
							// last value?
							break;
						}
						case ChartDataSource.DATA_TYPE_DOUBLE:
						default: {
							final Double dvalue = Cast.asFloat(scope, o);
							myserie.addxyvalue(scope, getDataset().getXSeriesValues().get(0), dvalue, chartCycle,
									barvalues, listvalue);
							break;

						}

					}

				}

			}

			// xy charts
			if (!this.isByCategory() && !this.isCommonXSeries()) {

				if (this.isCumulative()) {
					// new cumulative XY value

					switch (type_val) {
						case ChartDataSource.DATA_TYPE_POINT: {
							final ILocation pvalue = Cast.asPoint(scope, o);
							myserie.addxysvalue(scope, pvalue.getX(), pvalue.getY(), pvalue.getZ(), chartCycle,
									barvalues, listvalue);

							break;
						}
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
							final IList lvalue = Cast.asList(scope, o);
							if (lvalue.length(scope) < 2) {

							}
							if (lvalue.length(scope) == 2) {
								myserie.addxyvalue(scope, Cast.asFloat(scope, lvalue.get(0)),
										Cast.asFloat(scope, lvalue.get(1)), chartCycle, barvalues, listvalue);
							}
							if (lvalue.length(scope) > 2) {
								myserie.addxysvalue(scope, Cast.asFloat(scope, lvalue.get(0)),
										Cast.asFloat(scope, lvalue.get(1)), Cast.asFloat(scope, lvalue.get(2)),
										chartCycle, barvalues, listvalue);
							}
							break;

						}
						case ChartDataSource.DATA_TYPE_NULL: {
							// last value?
							break;
						}
						case ChartDataSource.DATA_TYPE_DOUBLE:
						default: {
							final Double dvalue = Cast.asFloat(scope, o);
							myserie.addxyvalue(scope,
									getDataset().getXSeriesValues().get(getDataset().getCommonXIndex()), dvalue,
									chartCycle, barvalues, listvalue);

							break;
						}

					}

				}

				if (!this.isCumulative()) {
					// new XY values
					switch (type_val) {
						case ChartDataSource.DATA_TYPE_POINT: {
							final ILocation pvalue = Cast.asPoint(scope, o);
							myserie.addxysvalue(scope, pvalue.getX(), pvalue.getY(), pvalue.getZ(), chartCycle,
									barvalues, listvalue);

							break;
						}
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
							final IList lvalue = Cast.asList(scope, o);
							if (lvalue.length(scope) < 2) {

							}
							if (lvalue.length(scope) == 2) {
								myserie.addxyvalue(scope, Cast.asFloat(scope, lvalue.get(0)),
										Cast.asFloat(scope, lvalue.get(1)), chartCycle, barvalues, listvalue);
							}
							if (lvalue.length(scope) > 2) {
								myserie.addxysvalue(scope, Cast.asFloat(scope, lvalue.get(0)),
										Cast.asFloat(scope, lvalue.get(1)), Cast.asFloat(scope, lvalue.get(2)),
										chartCycle, barvalues, listvalue);
							}
							break;

						}
						case ChartDataSource.DATA_TYPE_LIST_POINT:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_N: {
							final IList l1value = Cast.asList(scope, o);
							for (int n1 = 0; n1 < l1value.size(); n1++) {
								final Object o2 = l1value.get(n1);
								final IList lvalue = Cast.asList(scope, o2);
								if (lvalue.length(scope) < 2) {

								}
								if (lvalue.length(scope) == 2) {
									myserie.addxyvalue(scope, Cast.asFloat(scope, lvalue.get(0)),
											Cast.asFloat(scope, lvalue.get(1)), chartCycle, barvalues, listvalue);
								}
								if (lvalue.length(scope) > 2) {
									myserie.addxysvalue(scope, Cast.asFloat(scope, lvalue.get(0)),
											Cast.asFloat(scope, lvalue.get(1)), Cast.asFloat(scope, lvalue.get(2)),
											chartCycle, barvalues, listvalue);
								}

							}
							break;

						}
						case ChartDataSource.DATA_TYPE_NULL: {
							// last value?
							break;
						}
						case ChartDataSource.DATA_TYPE_DOUBLE:
						default: {
							final Double dvalue = Cast.asFloat(scope, o);
							myserie.addxyvalue(scope,
									getDataset().getXSeriesValues().get(getDataset().getCommonXIndex()), dvalue,
									chartCycle, barvalues, listvalue);
							break;

						}

					}

				}

			}

			// category charts
			if (this.isByCategory()) {

				if (this.isCumulative()) {
					// new cumulative category value
					// category is the last of the dataset

					switch (type_val) {
						case ChartDataSource.DATA_TYPE_POINT: {
							final ILocation pvalue = Cast.asPoint(scope, o);
							myserie.addcysvalue(scope, getDataset().getLastCategories(scope), pvalue.getX(),
									pvalue.getY(), chartCycle, barvalues, listvalue);
							break;
						}
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
							final IList lvalue = Cast.asList(scope, o);
							if (lvalue.length(scope) == 0) {

							}
							if (lvalue.length(scope) == 1) {
								myserie.addcyvalue(scope, getDataset().getLastCategories(scope),
										Cast.asFloat(scope, lvalue.get(0)), chartCycle, barvalues, listvalue);
							}
							if (lvalue.length(scope) > 1) {
								myserie.addcysvalue(scope, getDataset().getLastCategories(scope),
										Cast.asFloat(scope, lvalue.get(0)), Cast.asFloat(scope, lvalue.get(1)),
										chartCycle, barvalues, listvalue);
							}
							break;

						}
						case ChartDataSource.DATA_TYPE_NULL: {
							// last value?
							break;
						}
						case ChartDataSource.DATA_TYPE_DOUBLE:
						default: {
							final Double dvalue = Cast.asFloat(scope, o);
							myserie.addcyvalue(scope, getDataset().getLastCategories(scope), dvalue, chartCycle,
									barvalues, listvalue);

							break;
						}

					}

				}

				if (!this.isCumulative()) {
					// new non cumulative category value
					// category in the order of the dataset
					switch (type_val) {
						case ChartDataSource.DATA_TYPE_POINT: {
							final ILocation pvalue = Cast.asPoint(scope, o);
							myserie.addcysvalue(scope, getDataset().getCategories(scope, 0), pvalue.getX(),
									pvalue.getY(), chartCycle, barvalues, listvalue);

							break;
						}
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
							final IList l1value = Cast.asList(scope, o);
							for (int n1 = 0; n1 < l1value.size(); n1++) {
								final Object o2 = l1value.get(n1);
								myserie.addcyvalue(scope, getDataset().getCategories(scope, n1),
										Cast.asFloat(scope, o2), chartCycle, barvalues, listvalue);
							}
							break;

						}
						case ChartDataSource.DATA_TYPE_LIST_LIST_POINT:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_12:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_3:
						case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_N: {
							final IList l1value = Cast.asList(scope, o);
							for (int n1 = 0; n1 < l1value.size(); n1++) {
								final Object o2 = l1value.get(n1);
								final IList lvalue = Cast.asList(scope, o2);
								if (lvalue.length(scope) == 1) {
									myserie.addcyvalue(scope, getDataset().getCategories(scope, n1),
											Cast.asFloat(scope, lvalue.get(0)), chartCycle, barvalues, listvalue);

								}
								if (lvalue.length(scope) > 1) {
									myserie.addcysvalue(scope, getDataset().getCategories(scope, n1),
											Cast.asFloat(scope, lvalue.get(0)), Cast.asFloat(scope, lvalue.get(1)),
											chartCycle, barvalues, listvalue);
								}

							}
							break;

						}
						case ChartDataSource.DATA_TYPE_NULL: {
							// last value?
							break;
						}
						case ChartDataSource.DATA_TYPE_DOUBLE:
						default: {
							final Double dvalue = Cast.asFloat(scope, o);
							myserie.addcyvalue(scope, getDataset().getCategories(scope, 0), dvalue, chartCycle,
									barvalues, listvalue);
							break;

						}

					}

				}

			}

		}
		if (this.isCommonYSeries()) {
			// heatmaps

			if (!this.isCumulative()) {
				// new non cumulative z value
				// serie in the order of the dataset

				switch (type_val) {
					case ChartDataSource.DATA_TYPE_POINT: {
						final ILocation pvalue = Cast.asPoint(scope, o);
						myserie.addxysvalue(scope, getDataset().getXSeriesValues().get(0),
								getDataset().getYSeriesValues().get(0), pvalue.getX(), chartCycle, barvalues,
								listvalue);

						break;
					}
					case ChartDataSource.DATA_TYPE_LIST_DOUBLE_12:
					case ChartDataSource.DATA_TYPE_LIST_DOUBLE_3:
					case ChartDataSource.DATA_TYPE_LIST_DOUBLE_N: {
						final IList l1value = Cast.asList(scope, o);
						for (int n1 = 0; n1 < l1value.size(); n1++) {
							final Object o2 = l1value.get(n1);
							while (n1 >= getDataset().getXSeriesValues().size())
								getDataset().updateXValues(scope, chartCycle, l1value.size());
							myserie.addxysvalue(scope, getDataset().getXSeriesValues().get(n1),
									getDataset().getCurrentCommonYValue(), Cast.asFloat(scope, o2), chartCycle,
									barvalues, listvalue);
						}
						break;

					}
					case ChartDataSource.DATA_TYPE_LIST_LIST_POINT:
					case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_12:
					case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_3:
					case ChartDataSource.DATA_TYPE_LIST_LIST_DOUBLE_N: {
						final IList l1value = Cast.asList(scope, o);
						for (int n1 = 0; n1 < l1value.size(); n1++) {
							final Object o2 = l1value.get(n1);
							final IList lvalue = Cast.asList(scope, o2);
							while (n1 >= getDataset().getXSeriesValues().size())
								getDataset().updateXValues(scope, chartCycle, l1value.size());
							for (int n2 = 0; n2 < lvalue.size(); n2++) {
								while (n2 >= getDataset().getYSeriesValues().size())
									getDataset().updateYValues(scope, chartCycle, lvalue.size());
								myserie.addxysvalue(scope, getDataset().getXSeriesValues().get(n1),
										getDataset().getYSeriesValues().get(n2), Cast.asFloat(scope, lvalue.get(n2)),
										chartCycle, barvalues, listvalue);

							}

						}
						break;

					}
					case ChartDataSource.DATA_TYPE_NULL: {
						// last value?
						break;
					}
					case ChartDataSource.DATA_TYPE_DOUBLE:
					default: {
						final Double dvalue = Cast.asFloat(scope, o);
						myserie.addxysvalue(scope, getDataset().getXSeriesValues().get(0),
								getDataset().getYSeriesValues().get(0), dvalue, chartCycle, barvalues, listvalue);
						break;

					}

				}

			}

		}
		if (!this.isCumulative()) {
			myserie.endupdate(scope);

		}

	}

	public LinkedHashMap<String, ChartDataSeries> getSeries() {
		return mySeries;
	}

	public void setValue(final IExpression value) {
		this.value = value;
	}

	public void setYErrValueExp(final IScope scope, final IExpression expval) {
		this.setUseYErrValues(true);
		this.valueyerr = expval;

	}

	public void setXErrValueExp(final IScope scope, final IExpression expval) {
		this.setUseXErrValues(true);
		this.valuexerr = expval;

	}

	public void setYMinMaxValueExp(final IScope scope, final IExpression expval) {
		this.setUseYMinMaxValues(true);
		this.valueyminmax = expval;

	}

	public void setMarkerShape(final IScope scope, final String stval) {
		// markerName is useless, for now creates/modifies the output
		uniqueMarkerName = stval;
		if (uniqueMarkerName == ChartDataStatement.MARKER_EMPTY) {
			this.setMarkerBool(scope, false);
		}
	}

	public void setMarkerSize(final IScope scope, final IExpression expval) {
		this.setUseSize(scope, true);
		this.sizeexp = expval;

	}

	public IExpression getSizeexp() {
		return sizeexp;
	}

	public void setColorExp(final IScope scope, final IExpression expval) {
		this.setUseColorExp(scope, true);
		this.colorexp = expval;

	}

	public boolean isUseSizeExp() {
		if (this.sizeexp == null)
			return false;
		return true;
	}

	public void setUseColorExp(final IScope scope, final boolean b) {
		this.useColorExp = b;

	}

	public boolean isUseColorExp() {
		return useColorExp;
	}

	public void setMarkerBool(final IScope scope, final boolean boolval) {
		useMarker = boolval;
	}

	public void setFillMarker(final IScope scope, final boolean boolval) {
		fillMarker = boolval;
	}

	public void setUseSecondYAxis(final IScope scope, final boolean boolval) {
		useSecondYAxis=boolval;
	}

	public boolean getUseSecondYAxis(final IScope scope) {
		return useSecondYAxis;
	}

	public void setShowLine(final IScope scope, final boolean boolval) {
		showLine = boolval;
	}

	public void updatevalues(final IScope scope, final int lastUpdateCycle) {

	}

	public void setUseSize(final IScope scope, final boolean b) {
		this.setUseSize(b);
	}

	public void createInitialSeries(final IScope scope) {

	}

	public void savehistory(final IScope scope, final ChartHistory history) {
		for (final Map.Entry<String, ChartDataSeries> seriepair : this.mySeries.entrySet()) {
			seriepair.getValue().savehistory(scope, history);
		}
	}

}
