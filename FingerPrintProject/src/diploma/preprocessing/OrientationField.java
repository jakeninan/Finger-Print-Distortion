package diploma.preprocessing;

import diploma.CommonUtils;
import java.io.Serializable;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class OrientationField implements Serializable {

    /**
     * Calculates image gradients Gx and Gy using Sobel operator First
     * ImageProcessor in result List is Gx, second - Gy
     *
     * @return gradients
     */
    public static List<double[][]> sobelGradient(double[][] pixels) {

        double[][] Gy = Convolution.convolve(pixels, new double[][]{{-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}});

        double[][] Gx = Convolution.convolve(pixels, new double[][]{{-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}});
        return Arrays.asList(Gx, Gy);
    }

    public static List<Pair<Pair<Integer, Integer>, Integer>> findPoincarePoints(double[][] vectorField) {
        List<Pair<Pair<Integer, Integer>, Integer>> poincarePoints = new LinkedList<>();
        try {
            int[][] angleField = new int[vectorField.length][vectorField[0].length];
            for (int i = 0; i < angleField.length; i++) {
                for (int j = 0; j < angleField[0].length; j++) {

                    int angle = (int) round(180 * vectorField[i][j] / PI);
                    angleField[i][j] = angle % 360;
                    if (angleField[i][j] < 0) {
                        angleField[i][j] += 360;
                    }
                }
            }

            for (int i = 1; i < angleField.length - 1; i++) {
                for (int j = 1; j < angleField[0].length - 1; j++) {

                    int poincareIndex = poincareIndex(angleField, i, j);
                    if (poincareIndex != 0) {
                        poincarePoints.add(new ImmutablePair<>(new ImmutablePair<>(i, j), poincareIndex));
                    }
                }
            }
        } catch (Exception e) {
        }
        return poincarePoints;
    }

    public static Pair<Double, Double> getCorePoint(double[][] vectorField) {
        return getCorePoint(findPoincarePoints(vectorField));
    }

    private static Pair<Double, Double> getCorePoint(List<Pair<Pair<Integer, Integer>, Integer>> poincarePoints) {

        List<Pair<Integer, Integer>> points = new LinkedList<>();
        List<Set<Pair<Integer, Integer>>> clusters = new LinkedList<>();

        for (Pair<Pair<Integer, Integer>, Integer> point : poincarePoints) {
            if (point.getRight() != -180) {
                points.add(new ImmutablePair<>(point.getLeft().getLeft(), point.getLeft().getRight()));
            }
        }
        for (Pair<Integer, Integer> point : points) {

            boolean isClustered = false;
            for (Set<Pair<Integer, Integer>> cluster : clusters) {
                for (Pair<Integer, Integer> clusterPoint : cluster) {

                    if (isNeighbour(clusterPoint, point)) {
                        cluster.add(point);
                        isClustered = true;
                        break;
                    }
                }
            }

            if (!isClustered) {
                Set<Pair<Integer, Integer>> newCluster = new HashSet<>();
                newCluster.add(point);
                clusters.add(newCluster);
            }
        }

        List<Pair<Double, Double>> clusterCenters = new ArrayList<>();
        for (Set<Pair<Integer, Integer>> cluster : clusters) {

            double x = 0;
            double y = 0;
            for (Pair<Integer, Integer> point : cluster) {
                x += point.getLeft();
                y += point.getRight();
            }
            clusterCenters.add(new ImmutablePair<>(x / cluster.size(), y / cluster.size()));
        }

        Pair<Double, Double> corePoint = clusterCenters.get(0);
        for (int i = 1; i < clusterCenters.size(); i++) {
            if (clusterCenters.get(i).getLeft() < corePoint.getLeft()) {
                corePoint = clusterCenters.get(i);
            }
        }
        return corePoint;
    }

    private static boolean isNeighbour(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {

        Set<Pair<Integer, Integer>> neighbourSet = new HashSet<>();
        int i = p1.getLeft();
        int j = p1.getRight();
        neighbourSet.add(new ImmutablePair<>(i - 1, j - 1));
        neighbourSet.add(new ImmutablePair<>(i - 1, j));
        neighbourSet.add(new ImmutablePair<>(i - 1, j + 1));
        neighbourSet.add(new ImmutablePair<>(i, j - 1));
        neighbourSet.add(new ImmutablePair<>(i, j));
        neighbourSet.add(new ImmutablePair<>(i, j + 1));
        neighbourSet.add(new ImmutablePair<>(i + 1, j - 1));
        neighbourSet.add(new ImmutablePair<>(i + 1, j));
        neighbourSet.add(new ImmutablePair<>(i + 1, j + 1));

        return neighbourSet.contains(p2);
    }

    public static int poincareIndex(int[][] angleField, int i, int j) {

        int[] d = new int[8];
        d[0] = angleField[i + 1][j - 1];
        d[1] = angleField[i][j - 1];
        d[2] = angleField[i - 1][j - 1];
        d[3] = angleField[i - 1][j];
        d[4] = angleField[i - 1][j + 1];
        d[5] = angleField[i][j + 1];
        d[6] = angleField[i + 1][j + 1];
        d[7] = angleField[i + 1][j];

        int index = 0;
        for (int k = 0; k < 7; k++) {

            int angleDiff;
            int d1 = d[k];
            int da = d[k + 1];
            int db = (da + 180) % 360;

            int aDiff = min(abs(d1 - da), 360 - abs(d1 - da));
            int bDiff = min(abs(d1 - db), 360 - abs(d1 - db));

            if (aDiff < bDiff) {
                d[k + 1] = da;
                angleDiff = aDiff;
            } else {
                d[k + 1] = db;
                angleDiff = bDiff;
            }

            if ((d[k + 1] + angleDiff) % 360 == d[k]) {
                index -= angleDiff;
            } else {
                index += angleDiff;
            }
        }

        if (min(abs(d[7] - d[0]), 360 - abs(d[7] - d[0])) > 90) {
            d[0] = (d[0] + 180) % 360;
        }

        int angleDiff = min(abs(d[7] - d[0]), 360 - abs(d[7] - d[0]));
        if ((d[0] + angleDiff) % 360 == d[7]) {
            index -= angleDiff;
        } else {
            index += angleDiff;
        }

        return index;
    }

    private double[][] pixels;
    private double[][] orientationField;
    private double[][] coherences;

    public OrientationField(double[][] pixels) {
        this.pixels = pixels;
    }

    public OrientationField calculate(int segmentSize) {

        if (segmentSize % 2 != 1) {
            throw new IllegalArgumentException("Segment size should be odd");
        }

        List<double[][]> gradients = sobelGradient(pixels);
        SegmentedImage Gx = new SegmentedImage(segmentSize, gradients.get(0));
        SegmentedImage Gy = new SegmentedImage(segmentSize, gradients.get(1));

        orientationField = new double[Gx.getSegmentsInRow()][Gx.getSegmentsInColumn()];
        coherences = new double[Gx.getSegmentsInRow()][Gx.getSegmentsInColumn()];
        for (int i = 0; i < orientationField.length; i++) {
            for (int j = 0; j < orientationField[0].length; j++) {

                double Gxx = 0, Gyy = 0, Gxy = 0;
                SegmentedImage.Segment dxSegment = Gx.getSegment(i, j);
                SegmentedImage.Segment dySegment = Gy.getSegment(i, j);

                int halfSize = (segmentSize - 1) / 2;
                for (int k = -halfSize; k <= halfSize; k++) {
                    for (int l = -halfSize; l <= halfSize; l++) {

                        double dx = dxSegment.pixels[k + halfSize][l + halfSize];
                        double dy = dySegment.pixels[k + halfSize][l + halfSize];
                        Gxy += dx * dy;
                        Gxx += dx * dx;
                        Gyy += dy * dy;
                    }
                }
                coherences[i][j] = sqrt(pow(Gxx - Gyy, 2) + 4 * pow(Gxy, 2)) / (Gxx + Gyy);
                orientationField[i][j] = 0.5 * atan2(2 * Gxy, Gxx - Gyy) + Math.PI / 2;
            }
        }
        return this;
    }

    public Pair<double[][], double[][]> smoothField(int borderOffset) {

        int n = orientationField.length;
        int m = orientationField[0].length;
        double[][] smoothedField = new double[n - 2 * borderOffset][m - 2 * borderOffset];
        double[][] smoothedCoherences = new double[n - 2 * borderOffset][m - 2 * borderOffset];

        for (int i = borderOffset; i < n - borderOffset; i++) {
            for (int j = borderOffset; j < m - borderOffset; j++) {

                double xCord = 0;
                double yCord = 0;
                double normSum = 0;

                for (int k = i - 1; k <= i + 1; k++) {
                    for (int p = j - 1; p <= j + 1; p++) {

                        double rx = coherences[k][p] * cos(2 * orientationField[k][p]);
                        double ry = coherences[k][p] * sin(2 * orientationField[k][p]);
                        xCord += rx;
                        yCord += ry;
                        normSum += sqrt(rx * rx + ry * ry);
                    }
                }
                smoothedCoherences[i - borderOffset][j - borderOffset] = sqrt(xCord * xCord + yCord * yCord) / normSum;
                smoothedField[i - borderOffset][j - borderOffset] = 0.5 * atan2(yCord, xCord);
            }
        }
        return new ImmutablePair<>(smoothedField, smoothedCoherences);
    }

    public double[][] getOrientationField() {
        return orientationField;
    }

    public double[][] getCoherences() {
        return coherences;
    }

    @Override
    public String toString() {

        return CommonUtils.toString(orientationField);
    }

    public String toString(Set<Pair<Integer, Integer>> roi, int roiWidth) {

        Pair<Integer, Integer> roiStart = CommonUtils.getROIStart(roi);
        int startRow = roiStart.getLeft();
        int startColumn = roiStart.getRight();
        double[][] roiOrientation = CommonUtils.subArray(orientationField, startRow, startColumn, roiWidth);
        return CommonUtils.toString(roiOrientation);
    }
}
