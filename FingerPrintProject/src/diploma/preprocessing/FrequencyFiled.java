package diploma.preprocessing;

import com.google.common.collect.Range;
import diploma.CommonUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static java.lang.Math.*;

public class FrequencyFiled {

	/**
	 * Calculates distance to line with given angle of slope from specified point
	 * @param i		x coordinate of point
	 * @param j		y coordinate of point
	 * @param theta	angle of line slope
	 * @return		distance
	 */
	private static double distance(double i,double j, double theta) {

		if (abs(PI/2 - theta) < 1e-4) {
			return i;
		}
		double norm = sqrt(1 + pow(tan(theta),2));
		return (i*tan(theta) - j)/norm;
	}

	/**
	 * Find pixels through which line with given angle should have passed
	 * Assumed that line passes through (0,0) point of coordinate space
	 * @param theta	angle between line and X axis
	 * @param n		length of line
	 * @return		line points in 2d pixel space with corresponding order numbers as Map keys
	 */
	private static Map<Integer,Pair<Integer,Integer>> getPixelAxis(double theta, int n) {

		if (n % 2 == 0) {
			throw new IllegalArgumentException("n should be odd");
		}
		int halfSize = (n - 1)/2;

		double[][] distances = new double[n][n];
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances.length; j++) {
				double dist = distance(i - halfSize,j - halfSize,theta);
				distances[j][i] = (abs(dist) > 0.5 ? 0 : 1);
			}
		}
		List<Pair<Integer,Integer>> line = new ArrayList<>();
		for (int i = 0; i < distances.length; i++) {
			for (int j = 0; j < distances.length; j++) {
				if (distances[j][i] > 0) {
					line.add(new ImmutablePair<>(i - halfSize, j - halfSize));
				}
			}
		}
		Collections.sort(line, new Comparator<Pair<Integer, Integer>>() {

			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {

				return o1.getLeft().compareTo(o2.getLeft());
			}
		});
		int centerIndex = line.indexOf(new ImmutablePair<>(0,0));
		Map<Integer,Pair<Integer,Integer>> result = new HashMap<>();
		result.put(0, line.get(centerIndex));
		for (int i = 1; i <= halfSize; i++) {
			result.put(i, line.get(centerIndex + i));
			result.put(-i, line.get(centerIndex - i));
		}
		return result;
	}

	/**
	 * Find signature of image block with given parameters
	 */
	private static List<Double> getSignature(double[][] image, int row, int column, int xSize, int ySize, double theta) {

		Map<Integer,Pair<Integer,Integer>> xAxis = getPixelAxis(theta + PI/2, xSize);
		Map<Integer,Pair<Integer,Integer>> yAxis = getPixelAxis(theta, ySize);
		List<Double> signature = new ArrayList<>();
		Range<Integer> rowsRange = Range.closed(0, image.length - 1);
		Range<Integer> columnsRange = Range.closed(0, image[0].length - 1);

		for (int i = -(xSize -1)/2; i <= (xSize - 1)/2; i++) {

			double value = 0;
			Pair<Integer,Integer> currentCenter = new ImmutablePair<>(xAxis.get(i).getRight() + row,
					xAxis.get(i).getLeft() + column);

			for (Pair<Integer,Integer> pair : yAxis.values()) {
				int rowCor = currentCenter.getLeft() + pair.getRight();
				int colCor = currentCenter.getRight() + pair.getLeft();
				if (!(rowsRange.contains(rowCor) && columnsRange.contains(colCor))) {
					value = 0;
					break;
				} else {
					value += image[rowCor][colCor];
				}
			}
			if (abs(value) > 1e-4) {
				signature.add(value);
			}
		}
		return signature;
	}

	/**
	 * Estimates ridges period in the block with given parameters
	 * @param image		array of pixels of the source image
	 * @param row		block center row
	 * @param column	block center column
	 * @param xSize		block length along X axis (signature length)
	 * @param ySize		block length along Y axis
	 * @param theta		angle block rotation
	 * @return			ridges period
	 */
	public static double getPeriod(double[][] image, int row, int column, int xSize, int ySize, double theta) {

		List<Double> signature = getSignature(image, row, column, xSize, ySize, theta);
		for (int i = 0; i < 1; i++) {
			signature = smooth(signature, 5);
		}

		return getMaxPeakDistance(findPeaks(CommonUtils.toArray(signature)));
	}

	/**
	 * Smooths data using moving average
	 * @param data 			given data
	 * @param windowSize 	averaging window size
	 * @return	smoothed data
	 */
	public static List<Double> smooth(List<Double> data, int windowSize) {

		if (windowSize % 2 == 0) {
			throw new IllegalArgumentException("Smoothing window size must be odd");
		}

		int halfWindowSize = (windowSize - 1)/2;

		List<Double> smoothed = new ArrayList<>(data.size());
		for (int i = 0; i < data.size(); i++) {

			double windowSum = data.get(i);
			int elementsSummed = 1;
			for (int k = 1; k <= halfWindowSize; k++) {

				if ((i - k >= 0) && (i + k < data.size())) {
					windowSum += data.get(i - k);
					windowSum += data.get(i + k);
					elementsSummed += 2;
				}
			}
			smoothed.add(windowSum/elementsSummed);
		}
		return smoothed;
	}

	private static double getMaxPeakDistance(List<Pair<Double,Integer>> peaks) {
		if (peaks.size() < 2) {
			return 10;
		}

		int max = peaks.get(1).getRight() - peaks.get(0).getRight();
		for (int i = 1; i < peaks.size() - 1; i++) {
			if (peaks.get(i + 1).getRight() - peaks.get(i).getRight() > max) {
				max = peaks.get(i + 1).getRight() - peaks.get(i).getRight();
			}
		}
		return max;
	}

	private static List<Pair<Double,Integer>> findPeaks(double[] array) {

		List<Pair<Double,Integer>> peaks = new ArrayList<>();
		for (int i = 1; i < array.length - 2; i++) {
			if (array[i] > array[i - 1] && array[i] > array[i + 1]) {
				peaks.add(new ImmutablePair<>(array[i],i));
			}
		}
		return peaks;
	}
}