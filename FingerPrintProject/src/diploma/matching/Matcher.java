package diploma.matching;

import diploma.model.Fingerprint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

public class Matcher {

    public static final EuclideanDistance EUCLIDEAN_DISTANCE = new EuclideanDistance();

    private static Map<Fingerprint, Double> getDistances(List<Fingerprint> fpBase, Fingerprint fingerprint, DistanceMeasure distanceMeasure) {

        Map<Fingerprint, Double> distances = new HashMap<>();
        for (Fingerprint fpFromBase : fpBase) {
            distances.put(fpFromBase, distanceMeasure.compute(fpFromBase.getFeatureValues(), fingerprint.getFeatureValues()));
        }
        return distances;
    }

    public static Fingerprint match(FingerprintsDatabase fpBase, Fingerprint fingerprint, double threshold) {

        Map<Fingerprint, Double> distances = getDistances(fpBase.getFingerprints(), fingerprint, EUCLIDEAN_DISTANCE);

        Fingerprint nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Fingerprint fpFromBase : fpBase.getFingerprints()) {

            if (distances.get(fpFromBase) < minDistance) {
                minDistance = distances.get(fpFromBase);
                nearest = fpFromBase;
            }
        }

        if (minDistance > threshold) {
            return null;
        } else {
            return nearest;
        }
    }
}
