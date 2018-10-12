package diploma.model;

import db.datas;
import diploma.CommonUtils;
import static diploma.DBCreator.ACTION;
import diploma.preprocessing.Convolution;
import diploma.preprocessing.FrequencyFiled;
import diploma.preprocessing.GaborFilter;
import diploma.preprocessing.OrientationField;
import diploma.preprocessing.SegmentedImage;
import diploma.ui.FingerprintPanel;
import diploma.wavelets.Wavelet;
import diploma.wavelets.Wavelet.Subband;
import ij.ImagePlus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Fingerprint implements Serializable {

    public static final int PROCESSING_BLOCK_SIZE = 17;
    public static final int SMOOTHING_BORDER_OFFSET = 1;
    public static final int SIZE_OF_ROI = 8;
    public static final String fileName = "src/datas/train.dat";
    public static final String testfileName = "src/datas/test.dat";
    private final Map<Feature, Double> featureVector;
    private transient double[][] pixels;
    private ImageIcon image;
    private OrientationField orientationField;
    private Pair<Integer, Integer> startROIBlock;

    public int getId() {
        return id;
    }

    private int id;
    private String imageName;

    private Fingerprint() {
        featureVector = new HashMap<>();
    }

    public static Fingerprint extractFeatures(String imagePath) throws FileNotFoundException, ClassNotFoundException, SQLException {

        Fingerprint fingerprint = new Fingerprint();
        fingerprint.setName(imagePath);

        ImagePlus imagePlus = new ImagePlus(imagePath);
        fingerprint.pixels = CommonUtils.transpose(CommonUtils.toDouble(imagePlus.getProcessor().getFloatArray()));
        fingerprint.image = new ImageIcon(CommonUtils.resize(imagePlus, FingerprintPanel.IMG_WIDTH, FingerprintPanel.IMG_HEIGHT).getBufferedImage());

        //pre-processing
        OrientationField orientationField = new OrientationField(fingerprint.pixels);
        fingerprint.orientationField = orientationField;

        System.out.println("Orientation map,period map calculation and smoothing");

        orientationField.calculate(PROCESSING_BLOCK_SIZE);

        Pair<double[][], double[][]> smoothedField = orientationField.smoothField(SMOOTHING_BORDER_OFFSET);

        System.out.println("Core point detection");

        Pair<Double, Double> corePoint = OrientationField.getCorePoint(smoothedField.getLeft());
        fingerprint.startROIBlock = getStartROIBlock(corePoint, fingerprint).getLeft();
        double[][] smoothedOrientationField = mergeFields(orientationField.getOrientationField(), smoothedField.getLeft());

        Map<Pair<Integer, Integer>, Double> roiFrequencies = calculateROIFrequencies(fingerprint.pixels, smoothedOrientationField, fingerprint.startROIBlock);

        double[][] filteredROI = getGaborFilteredROI(fingerprint.pixels, smoothedOrientationField, roiFrequencies, fingerprint.startROIBlock);

        System.out.println(" feature extraction");

        Wavelet wavelet = Wavelet.Haar;  //to produce a vector output
        Map<Subband, double[][]> transformedROI = wavelet.transform(filteredROI);
        ArrayList asdf = new ArrayList();
        fillFeatureArray(fingerprint, transformedROI);
        if (ACTION.equalsIgnoreCase("distroted")) {
//            datas as = new datas();
//
//            as.insert(orientationField.getOrientationField(), filteredROI, fingerprint.imageName);
        }
        for (Object as : fingerprint.getFeatureValues()) {
            String value = String.valueOf(as);
            // System.out.println("v" + value);
            if (value.equals("NaN")) {

                value = "0";
                asdf.add(value);
            } else {
                asdf.add(value);
            }
        }
        if (ACTION.equals("test")) {
            appendToCheckbook(String.join(",", asdf));
        } else {
            appendToCheckbook(String.join(",", asdf), ACTION);
        }
        return fingerprint;
    }
    /* Transfer chars from source to destination in efficient chunks */

    public static void appendToCheckbook(String data, String filename) {

        try (FileWriter fw = new FileWriter("src/datas/" + filename, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(data + "," + filename);
            //more code

            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

    public static void appendToCheckbook(String data) {

        try (FileWriter fw = new FileWriter("src/datas/test", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(data);
            //more code

            //more code
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

    private static void fillFeatureArray(Fingerprint fingerprint, Map<Subband, double[][]> waveletTransformResult) {

        fingerprint.featureVector.put(Feature.Mean, CommonUtils.mean(fingerprint.pixels));
        fingerprint.featureVector.put(Feature.LH_Variance, CommonUtils.variance(waveletTransformResult.get(Subband.LH)));
        fingerprint.featureVector.put(Feature.HL_Variance, CommonUtils.variance(waveletTransformResult.get(Subband.HL)));
        fingerprint.featureVector.put(Feature.HH_Variance, CommonUtils.variance(waveletTransformResult.get(Subband.HH)));
    }

    private static double[][] getGaborFilteredROI(double[][] pixels, double[][] orientationField,
            Map<Pair<Integer, Integer>, Double> frequencies, Pair<Integer, Integer> cornerBlock) {

        int startBlockRow = cornerBlock.getLeft();
        int startBlockColumn = cornerBlock.getRight();
        double[][] filteredROI = new double[PROCESSING_BLOCK_SIZE * SIZE_OF_ROI][PROCESSING_BLOCK_SIZE * SIZE_OF_ROI];

        SegmentedImage segmentedImage = new SegmentedImage(PROCESSING_BLOCK_SIZE, pixels);
        for (Pair<Integer, Integer> block : frequencies.keySet()) {

            int blockRow = block.getLeft();
            int blockColumn = block.getRight();
            GaborFilter gaborFilter = new GaborFilter(frequencies.get(block), orientationField[blockRow][blockColumn]
                    + Math.PI / 2, 0, 0.5, 2);

            double[][] filteredBlockPixels = Convolution.convolve(segmentedImage.getSegment(blockRow, blockColumn).getPixels(),
                    gaborFilter.getKernel());

            for (int i = 0; i < PROCESSING_BLOCK_SIZE; i++) {
                for (int j = 0; j < PROCESSING_BLOCK_SIZE; j++) {

                    int roiRow = PROCESSING_BLOCK_SIZE * (blockRow - startBlockRow) + i;
                    int roiColumn = PROCESSING_BLOCK_SIZE * (blockColumn - startBlockColumn) + j;
                    filteredROI[roiRow][roiColumn] = filteredBlockPixels[i][j];
                }
            }
        }

        return filteredROI;
    }

    /**
     * Returns orientation field with the same size as source field but with
     * values of smoothed field (if it's not source field borders)
     */
    private static double[][] mergeFields(double[][] sourceOrientationField, double[][] smoothedField) {

        int n = sourceOrientationField.length;
        int m = sourceOrientationField[0].length;
        double[][] mergedField = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                if (i < SMOOTHING_BORDER_OFFSET || i >= n - SMOOTHING_BORDER_OFFSET || j < SMOOTHING_BORDER_OFFSET
                        || j >= m - SMOOTHING_BORDER_OFFSET) {

                    mergedField[i][j] = sourceOrientationField[i][j];
                } else {
                    mergedField[i][j] = smoothedField[i - SMOOTHING_BORDER_OFFSET][j - SMOOTHING_BORDER_OFFSET];
                }
            }
        }
        return mergedField;
    }

    private void setName(String imagePath) {

        Matcher matcher = Pattern.compile("\\d+").matcher(new StringBuilder(imagePath).reverse().toString());
        int i = 0;
        String possibleId = null;
        while (matcher.find()) {
            String matchedString = matcher.group();
            i++;

            if (i == 2) {
                possibleId = new StringBuilder(matchedString).reverse().toString();
                break;
            }
        }
        if (possibleId != null) {
            id = Integer.parseInt(possibleId);
        } else {
            id = 0;
        }
        File file = new File(imagePath);
        imageName = file.getName();
    }

    /**
     * Calculates coordinates of left upper border block of ROI
     *
     * @param corePoint	estimated core point which was calculated from some
     * smoothed orientation field (in blocks)
     * @return	coordinates of left upper border block of ROI (first pair) and
     * actual estimated core point (in pixels)
     */
    private static Pair<ImmutablePair<Integer, Integer>, ImmutablePair<Integer, Integer>> getStartROIBlock(
            Pair<Double, Double> corePoint, Fingerprint fingerprint) {

        int row = SMOOTHING_BORDER_OFFSET + (int) corePoint.getLeft().doubleValue();
        int column = SMOOTHING_BORDER_OFFSET + (int) corePoint.getRight().doubleValue();

        int startRowOfROI = row - SIZE_OF_ROI / 2 + SMOOTHING_BORDER_OFFSET;
        int startColumnOfROI = column - SIZE_OF_ROI / 2 + SMOOTHING_BORDER_OFFSET;

        int corePointRow = (startRowOfROI + SIZE_OF_ROI / 2) * PROCESSING_BLOCK_SIZE;
        int corePointColumn = (startColumnOfROI + SIZE_OF_ROI / 2) * PROCESSING_BLOCK_SIZE;

        if (startRowOfROI < 0 || startColumnOfROI < 0
                || startRowOfROI + SIZE_OF_ROI - 1 >= fingerprint.orientationField.getOrientationField().length
                || startColumnOfROI + SIZE_OF_ROI - 1 >= fingerprint.orientationField.getOrientationField()[0].length) {

            //  throw new IllegalStateException("Invalid input image. Cannot crop sufficient area around core point");
        }

        return new ImmutablePair<>(new ImmutablePair<>(startRowOfROI, startColumnOfROI),
                new ImmutablePair<>(corePointRow, corePointColumn));
    }

    /**
     * Calculates local ridge frequency for each block in ROI
     *
     * @param pixels	pixels of image
     * @param orientationField	evidently
     * @param roiCorner	coordinates of left upper ROI block
     * @return	returns Maps with ROI blocks coordinates and correspondent local
     * frequency
     */
    private static Map<Pair<Integer, Integer>, Double> calculateROIFrequencies(double[][] pixels,
            double[][] orientationField,
            Pair<Integer, Integer> roiCorner) {

        Map<Pair<Integer, Integer>, Double> frequencies = new HashMap<>();
        for (int i = roiCorner.getLeft(); i < roiCorner.getLeft() + SIZE_OF_ROI; i++) {
            for (int j = roiCorner.getRight(); j < roiCorner.getRight() + SIZE_OF_ROI; j++) {

                int blockCenterRow = (int) (PROCESSING_BLOCK_SIZE * (i + 0.5));
                int blockCenterColumn = (int) (PROCESSING_BLOCK_SIZE * (j + 0.5));

                double frequency;
                if (i == 0 || j == 0 || i == orientationField.length - 1 || j == orientationField[0].length - 1) {

                    frequency = FrequencyFiled.getPeriod(pixels, blockCenterRow, blockCenterColumn, 15, 15,
                            orientationField[i][j]);
                } else {
                    frequency = FrequencyFiled.getPeriod(pixels, blockCenterRow, blockCenterColumn, 31, 31,
                            orientationField[i][j]);
                }
                frequencies.put(new ImmutablePair<>(i, j), frequency);
            }
        }
        return frequencies;
    }

    public Map<Feature, Double> getFeatureVector() {
        return featureVector;
    }

    public double[] getFeatureValues() {
        return Feature.getFeatureValues(featureVector);
    }

    public ImageIcon getImage() {
        return image;
    }

    public double[][] getROIOrientations() {

        return CommonUtils.subArray(orientationField.getOrientationField(), startROIBlock.getLeft(), startROIBlock.getRight(),
                SIZE_OF_ROI);
    }

    @Override
    public String toString() {
        return imageName;
    }
}
