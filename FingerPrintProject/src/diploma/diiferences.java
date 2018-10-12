/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diploma;

import Jama.Matrix;
import ij.ImagePlus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pca_transform.PCA;

/**
 *
 * @author user54
 */
public class diiferences {

    public static String im1 = null;
    public static String im2 = null;
    public static int i = 0;
    public static double[][] im1x;
    public static double[][] im2x;
    public static double[][] im3x;
    public static PCA pca;
    public static Matrix eigenvectorsMatrix;
    public static double[][] outImg;
    public static Matrix trainingData;
    public static HashMap<Integer, Matrix> distrotion = new HashMap<>();
    public static List< String> results = new ArrayList<String>();

    public static double[][] getDifferenceImage(String img1, String img2) {

        ImagePlus first = new ImagePlus(img1);
        ImagePlus Second = new ImagePlus(img2);
        im1x = CommonUtils.transpose(CommonUtils.toDouble(first.getProcessor().getFloatArray()));
        im2x = CommonUtils.transpose(CommonUtils.toDouble(Second.getProcessor().getFloatArray()));
        Matrix first1 = new Matrix(im1x);
        Matrix second1 = new Matrix(im2x);

        Matrix sub = first1.minus(second1);
        double[][] dubs = sub.getArray();

        return dubs;

    }

    public static HashMap<Integer, Matrix> distrot() {

        File[] files = new File("src/distroted/").listFiles();
//If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
                if (file.getName().equalsIgnoreCase("101_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 1;
                    outImg = getDifferenceImage("src/distroted/101_1.tif", "src/distroted/101_8.tif");

                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    //printMatrix(eigenvectorsMatrix);
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("102_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();

                    i = 2;
                    outImg = getDifferenceImage("src/distroted/102_1.tif", "src/distroted/102_5.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("103_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 3;
                    outImg = getDifferenceImage("src/distroted/103_1.tif", "src/distroted/103_6.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("104_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 4;
                    outImg = getDifferenceImage("src/distroted/104_1.tif", "src/distroted/104_8.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("105_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 5;

                    outImg = getDifferenceImage("src/distroted/105_1.tif", "src/distroted/105_6.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();

                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("106_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 6;
                    outImg = getDifferenceImage("src/distroted/106_1.tif", "src/distroted/106_8.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("107_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 7;
                    outImg = getDifferenceImage("src/distroted/107_1.tif", "src/distroted/107_8.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("108_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 8;
                    outImg = getDifferenceImage("src/distroted/108_1.tif", "src/distroted/108_7.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();
                    distrotion.put(i, eigenvectorsMatrix);
                } else if (file.getName().equalsIgnoreCase("109_1.tif")) {
                    im1 = file.getAbsolutePath();
                    im2 = file.getAbsolutePath();
                    i = 9;
                    outImg = getDifferenceImage("src/distroted/109_1.tif", "src/distroted/109_2.tif");
                    // printMatrix(outImg);
                    trainingData = new Matrix(outImg);
                    pca = new PCA(trainingData);
                    eigenvectorsMatrix = pca.getEigenvectorsMatrix();

                    distrotion.put(i, eigenvectorsMatrix);
                }

            }
        }
        return distrotion;

    }

    public HashMap<Integer, Matrix> getvalues() {
        return distrotion;
    }

    public static void main(String[] args) throws IOException {

        diiferences.distrot();

        FileOutputStream fos = new FileOutputStream("new");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(diiferences.distrot());
        oos.close();
        fos.close();
    }

}
