package diploma.preprocessing;

import java.util.Arrays;

public class SegmentedImage {

	public static class Segment {

		public final double[][] pixels;

		public Segment(double[][] pixels) {
			this.pixels = pixels;
		}

		public double[][] getPixels() {
			return pixels;
		}

		@Override
		public String toString() {

			StringBuilder sb = new StringBuilder();
			for (double[] row : pixels) {
				for (double value : row) {
					sb.append(String.format("%5.2f ", value));
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}

	private Segment[][] segments;
	private double[][] imagePixels;
	private int segmentsInRow, segmentsInColumn;

	public SegmentedImage(int segmentSize, double[][] imagePixels) {

		this.imagePixels = imagePixels;
		int rows = imagePixels.length;
		int columns = imagePixels[0].length;

		segmentsInRow = rows/segmentSize;
		segmentsInColumn = columns/segmentSize;
		segments = new Segment[segmentsInRow][segmentsInColumn];
		for (int i = 0; i < segments.length; i++) {
			for (int j = 0; j < segments[0].length; j++) {

				double[][] segmentPixels = new double[segmentSize][];
				int rowOffset = i*segmentSize;
				int columnOffset = j*segmentSize;

				for (int k = 0; k < segmentSize; k++) {
					segmentPixels[k] = Arrays.copyOfRange(imagePixels[rowOffset + k], columnOffset,
							columnOffset + segmentSize);
				}
				segments[i][j] = new Segment(segmentPixels);
			}
		}
	}

	public int getSegmentsInRow() {
		return segmentsInRow;
	}

	public int getSegmentsInColumn() {
		return segmentsInColumn;
	}

	public Segment getSegment(int i, int j) {
		return segments[i][j];
	}

	public double[][] getImagePixels() {
		return imagePixels;
	}

	@Override
	public String toString() {

		if (segments == null) {
			return "null";
		}
		StringBuilder sb = new StringBuilder();
		for (int i  = 0; i < segments.length; i++) {
			for (int k = 0; k < segments[0].length; k++) {
				sb.append(String.format("segment[%d][%d]\n", i, k));
				sb.append(segments[i][k]);
			}
		}
		return sb.toString();
	}
}
