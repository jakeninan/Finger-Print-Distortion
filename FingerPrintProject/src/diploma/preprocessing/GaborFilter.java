package diploma.preprocessing;

import ij.ImagePlus;

import static java.lang.Math.*;

public class GaborFilter {

	private double lambda, theta, psi, sigma, gamma;
	private double[][] kernel;

	/**
	 * Creates object for Gabor filtering with following parameters:
	 * @param lambda - wavelength in pixels
	 * @param theta - orientation of normal to the parallel stripes of Gabor function
	 * @param psi	- phase offset
	 * @param gamma - aspect ratio, specifies the ellipticity of the support of the Gabor function. For gamma = 1 -
	 *              support is circular
	 * @param b - half-response spatial frequency bandwidth. Used for sigma calculation. Number of stripes of the Gabor
	 *          function depends on this value
	 */
	public GaborFilter(double lambda, double theta, double psi, double gamma, double b) {

		this.lambda = lambda;
		this.theta = theta;
		this.psi = psi;
		this.gamma = gamma;

		sigma = lambda/PI*pow(log(2)/2, 0.5)*(pow(2,b) + 1)*(pow(2,b) - 1);
		generateKernel();
	}

	private void generateKernel() {

		double posSum = 0, negSum = 0;
		int n = (int) ceil(2.5*sigma/gamma);
		kernel = new double[2*n + 1][2*n + 1];
		for (int i = -n; i <= n; i++) {
			for (int j = -n; j <= n; j++) {

				double temp = G(i,j);
				kernel[i + n][j + n] = temp;
				if (temp > 0) {
					posSum += temp;
				} else {
					negSum -= temp;
				}
			}
		}

		//normalization
		double meanSum = (posSum + negSum)/2;
		posSum /= meanSum;
		negSum /= meanSum;
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel.length; j++) {
				if (kernel[i][j] > 0) {
					kernel[i][j] *= posSum;
				} else {
					kernel[i][j] *= negSum;
				}
			}
		}
	}

	private float[] getKernelVector() {

		float[] kerVector = new float[kernel.length*kernel.length];
		for (int i = 0; i < kernel.length; i++) {
			for (int j = 0; j < kernel.length; j++) {
				kerVector[i*kernel.length + j] = (float) kernel[i][j];
			}
		}
		return kerVector;
	}

	/**
	 * Gabor function
	 */
	private double G(double i, double j) {

		double B = 1./2/pow(sigma, 2);
		double a = B/PI;
		double f = 2*PI/lambda;
		double x_ = i*cos(theta) + j*sin(theta);
		double y_ = -i*sin(theta) + j*cos(theta);
		return a*exp(-B*(x_*x_ + gamma*gamma*(y_*y_)))*cos(f*x_ + psi);
	}

	public void filter(ImagePlus imagePlus, int row, int column, int width, int heigth) {
		imagePlus.setRoi(column, row, width, heigth);
		imagePlus.getProcessor().convolve(getKernelVector(), kernel.length, kernel.length);
	}

	public double[][] getKernel() {
		return kernel;
	}
}
