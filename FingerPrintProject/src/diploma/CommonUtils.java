package diploma;

import diploma.ui.MainFrame;
import static diploma.ui.MainFrame.loadedFileLabel;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CommonUtils {

    private static SampleModel sampleModel;

    public static ImagePlus resize(ImagePlus imagePlus, int width, int heigth) {

        ImageProcessor imageProcessor = imagePlus.getProcessor().resize(width, heigth);
        return new ImagePlus("new", imageProcessor);
    }

    public static java.awt.Image getImage(double pixels[][]) {
        //  sampleModel = Raster.getSampleModel();
        WritableRaster raster = Raster.createWritableRaster(new PixelInterleavedSampleModel(0, 240, 320, 1, 1920, new int[]{0}), new Point(0, 0));
        int w = pixels.length;
        int h = pixels[0].length;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                raster.setSample(i, j, 0, pixels[i][j]);
            }
        }
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        image.setData(raster);

        File output = new File("src/rectified/" + loadedFileLabel.getText());
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            System.out.println("" + e);
        }
        MainFrame.rightPanel.setImage(output.getPath());
        return image;
    }

    public static float[][] toFloat(double[][] a) {

        float[][] b = new float[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = (float) a[i][j];
            }
        }
        return b;
    }

    public static float[][] toFloat(int[][] a) {

        float[][] b = new float[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = a[i][j];
            }
        }
        return b;
    }

    public static double[][] toDouble(float[][] a) {

        double[][] b = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = a[i][j];
            }
        }
        return b;
    }

    public static int[][] toInt(double[][] a) {

        int[][] b = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                b[i][j] = (int) a[i][j];
            }
        }
        return b;
    }

    public static void printSignalToFile(double[] signal, String fileName) {

        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            for (double s : signal) {
                printWriter.format("%f ", s);
            }
            printWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printSignalToFile(double[][] signal, String fileName) {

        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            for (double[] row : signal) {
                for (double s : row) {
                    printWriter.format("%.3f ", s);
                }
                printWriter.println();
            }
            printWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static float[][] subtract(float[][] a, float[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        float[][] result = new float[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = a[i][j] - b[i][j];

            }
        }
        return result;
    }

    public static int[][] subtract(int[][] a, int[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        int[][] result = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    public static double[][] ConvertInt2Double(int[][] intarray) {

        double[][] doublearray = new double[intarray.length][intarray[0].length];

        for (int i = 0; i < intarray.length; i++) {
            for (int j = 0; j < intarray[0].length; j++) {
                doublearray[i][j] = (double) intarray[i][j];
            }
        }
        return doublearray;

    }

    public static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printMatrix(float[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printMatrix(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double[][] add(double[][] a, double[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    public static void printMatrix(Jama.Matrix transformedData) {
        for (int r = 0; r < transformedData.getRowDimension(); r++) {
            for (int c = 0; c < transformedData.getColumnDimension(); c++) {
                System.out.print(transformedData.get(r, c));
                if (c == transformedData.getColumnDimension() - 1) {
                    continue;
                }
                System.out.print(", ");
            }
            System.out.println("");
        }
    }

    public static void gaussian(double a[][], int index[]) {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i = 0; i < n; ++i) {
            index[i] = i;
        }

        // Find the rescaling factors, one from each row
        for (int i = 0; i < n; ++i) {
            double c1 = 0;
            for (int j = 0; j < n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) {
                    c1 = c0;
                }
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j = 0; j < n - 1; ++j) {
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; ++i) {
                double pj = a[index[i]][j] / a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l = j + 1; l < n; ++l) {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
    }

    public static double[][] invert(double a[][]) {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i = 0; i < n; ++i) {
            b[i][i] = 1;
        }

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    b[index[j]][k]
                            -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }

        // Perform backward substitutions
        for (int i = 0; i < n; ++i) {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; --j) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; ++k) {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    public static double[][] transpose(double[][] a) {

        double[][] b = new double[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            arrayToColumn(b, a[i], i);
        }
        return b;
    }

    public static double mean(double[][] a) {

        double res = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                res += a[0][j];
            }
        }
        return res / a.length / a[0].length;
    }

    public static double[] columnToArray(double[][] x, int i) {

        double[] y = new double[x.length];
        for (int k = 0; k < x.length; k++) {
            y[k] = x[k][i];
        }
        return y;
    }

    public static void arrayToColumn(double[][] a, double[] x, int i) {

        for (int k = 0; k < x.length; k++) {
            a[k][i] = x[k];
        }
    }

    public static double[] toOneDim(double[][] a) {

        double[] b = new double[a.length * a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {

                b[i * a[0].length + j] = a[i][j];
            }
        }
        return b;
    }

    public static double variance(double[][] a) {

        double m = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                m += a[i][j];
            }
        }
        m /= a[0].length * a.length;

        double res = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                res += Math.pow(m - a[i][j], 2);
            }
        }
        return res / a[0].length / a.length;
    }

    public static void showImage(double[][] pixels) {

        ImagePlus image = new ImagePlus("image", new FloatProcessor(toFloat(pixels)));
        image.show();
    }

    public static void printToFile(String s, String fileName) {

        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.print(s);
            printWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static double[] toArray(List<Double> list) {

        return ArrayUtils.toPrimitive(list.toArray(new Double[0]));
    }

    public static Pair<Integer, Integer> getROIStart(Set<Pair<Integer, Integer>> roi) {

        int minRow = 0, minColumn = 0;
        for (Pair<Integer, Integer> cell : roi) {
            if (cell.getLeft() < minRow) {
                minRow = cell.getLeft();
            }
            if (cell.getRight() < minColumn) {
                minColumn = cell.getRight();
            }
        }
        return new ImmutablePair<>(minRow, minColumn);
    }

    public static String toString(double[][] a) {

        StringBuilder sb = new StringBuilder();
        for (double[] row : a) {

            for (double value : row) {
                sb.append(String.format("%3.2f ", value));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static double[][] subArray(double[][] a, int startRow, int startColumn, int size) {

        double[][] b = new double[size][size];
        for (int i = startRow; i < startRow + size; i++) {
            for (int j = startColumn; j < startColumn + size; j++) {
                b[i - startRow][j - startColumn] = a[i][j];
            }
        }
        return b;
    }

}
