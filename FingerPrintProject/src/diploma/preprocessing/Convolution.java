package diploma.preprocessing;

import com.google.common.collect.Range;
import diploma.CommonUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class Convolution {

	public static double[] convolve(double[] x, double[] f) {

		double[] y = new double[x.length];
		for (int n = 0; n < x.length; n++) {
			for (int k = n - f.length + 1; k <= n; k++) {

				if (k < 0) {
					continue;
				}
				y[n] += x[k]*f[n - k];
			}
		}
		return y;
	}

	public static double[][] convolve(double[][] x, double[][] kernel) {

		if (kernel.length != kernel[0].length) {
			throw new IllegalArgumentException("Kernel should be square");
		}
		if (kernel.length % 2 != 1) {
			throw new IllegalArgumentException("Kernel size should by odd");
		}

		double[][] y = new double[x.length][x[0].length];
		int w = (kernel.length - 1)/2;

		double[][] flippedKernel = new double[kernel.length][];
		for (int i = 0; i < kernel.length; i++) {
			flippedKernel[i] = Arrays.copyOf(kernel[i], kernel.length);
			ArrayUtils.reverse(flippedKernel[i]);
		}
		for (int i = 0; i < kernel.length; i++) {

			double[] column = CommonUtils.columnToArray(flippedKernel, i);
			ArrayUtils.reverse(column);
			CommonUtils.arrayToColumn(flippedKernel, column, i);
		}

		Range<Integer> rowRange = Range.closedOpen(0, x.length);
		Range<Integer> columnRange = Range.closedOpen(0, x[0].length);

		for (int m = 0; m < x.length; m++) {
			for (int n = 0; n < x[0].length; n++) {

				for (int i = -w; i <= w; i++) {
					for (int j = -w; j <= w; j++) {

						if (rowRange.contains(m + i) && columnRange.contains(n + j)) {
							y[m][n] += x[m + i][n + j]*flippedKernel[i + w][j + w];
						}
					}
				}
			}
		}
		return y;
	}

	public static void main(String[] args) {

		double[][] a = new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
		double[][] k = new double[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
		System.out.println(Arrays.deepToString(convolve(a, k)));
	}
}
