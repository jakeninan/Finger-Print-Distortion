package diploma.model;

import java.util.Map;

public enum Feature {

	Mean, HH_Variance, HL_Variance, LH_Variance;

	public static double[] getFeatureValues(Map<Feature,Double> features) {

		double[] featureValues = new double[Feature.values().length];
		for (int i = 0; i < Feature.values().length; i++) {
			featureValues[i] = features.get(Feature.values()[i]);
		}
		return featureValues;
	}
}
