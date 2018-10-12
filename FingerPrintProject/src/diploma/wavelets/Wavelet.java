package diploma.wavelets;

import diploma.CommonUtils;
import diploma.preprocessing.Convolution;

import java.util.*;

public enum Wavelet {

	Haar(new double[]{0.707106781186548, 0.707106781186548}, new double[]{-0.707106781186548, 0.707106781186548}),
	db2(new double[]{-0.129409522550921, 0.224143868041857, 0.836516303737469, 0.482962913144690},
			new double[]{-0.482962913144690, 0.836516303737469, -0.224143868041857, -0.129409522550921}),
	db3(new double[]{0.0352, -0.0854, -0.1350, 0.4599, 0.8069, 0.3327},
			new double[]{-0.3327, 0.8069, -0.4599, -0.1350, 0.0854, 0.0352});

	public static enum Subband {
		LL, HH, HL, LH
	}

	private double[] hiFilter, loFilter;

	Wavelet(double[] loFilter, double[] hiFilter) {
		this.loFilter = loFilter;
		this.hiFilter = hiFilter;
	}

	public double[][] transform(double[] signal) {

		if (signal.length % 2 != 0) {
			throw new IllegalArgumentException("Signal length must be even");
		}

		double[][] decomposition = new double[2][signal.length/2];
		decomposition[0] = downSample(Convolution.convolve(signal, loFilter));
		decomposition[1] = downSample(Convolution.convolve(signal, hiFilter));
		return decomposition;
	}

	/**
	 * Returns results of wavelet transform for given 2D signal
	 * @param signal    given signal
	 * @return          List of transformation result:
	 *                      1) LL block
	 *                      2) LH block
	 *                      3) HL block
	 *                      4) HH block
	 */
	public Map<Subband,double[][]> transform(double[][] signal) {

		if (signal.length % 2 != 0 || signal[0].length % 2 != 0) {
			throw new IllegalArgumentException("Signal length must be even");
		}

		List<double[][]> transformedRows = rowsTransform(signal);
		Map<Subband,double[][]> twoDimTransformRes = new HashMap<>(4);
		List<double[][]> LLAndLH = columnsTransform(transformedRows.get(0));
		List<double[][]> HLAndHH = columnsTransform(transformedRows.get(1));
		twoDimTransformRes.put(Subband.LL, LLAndLH.get(0));
		twoDimTransformRes.put(Subband.LH, LLAndLH.get(1));
		twoDimTransformRes.put(Subband.HL, HLAndHH.get(0));
		twoDimTransformRes.put(Subband.HH, HLAndHH.get(1));

		return twoDimTransformRes;
	}

	private List<double[][]> rowsTransform(double[][] signal) {

		double[][] lo = new double[signal.length][];
		double[][] hi = new double[signal.length][];

		for (int i = 0; i < signal.length; i++) {
			double[][] y = transform(signal[i]);
			lo[i] = y[0];
			hi[i] = y[1];
		}
		return Arrays.asList(lo, hi);
	}

	private List<double[][]> columnsTransform(double[][] x) {

		double[][] lo = new double[x.length/2][x[0].length];
		double[][] hi = new double[x.length/2][x[0].length];

		for (int i = 0; i < x[0].length; i++) {
			double[][] y = transform(CommonUtils.columnToArray(x, i));
			CommonUtils.arrayToColumn(lo, y[0], i);
			CommonUtils.arrayToColumn(hi, y[1], i);
		}
		return Arrays.asList(lo, hi);
	}

	private double[] downSample(double[] x) {

		double[] y = new double[x.length/2];
		for (int i = 0; i < x.length; i+=2) {
			y[i/2] = x[i];
		}
		return y;
	}
}
