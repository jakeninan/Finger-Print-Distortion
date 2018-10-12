package diploma.matching;

import diploma.model.Finger;
import diploma.model.Fingerprint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FingerprintsDatabase implements Serializable {

    public static FingerprintsDatabase loadExistent(String path) throws Exception {

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path));
            FingerprintsDatabase fingerprintsDatabase = (FingerprintsDatabase) objectInputStream.readObject();
            objectInputStream.close();
            return fingerprintsDatabase;
        } catch (Exception e) {
            return new FingerprintsDatabase();
        }
    }

    private List<Fingerprint> fingerprints;
    private Map<Finger, List<Fingerprint>> fingerDb;

    public FingerprintsDatabase() {

        fingerprints = new ArrayList<>();
        fingerDb = new HashMap<>();
    }

    public void addFinger(Finger finger) {
        fingerDb.put(finger, new LinkedList<Fingerprint>());
    }

    public void addFingerprintToFinger(Finger finger, Fingerprint fingerprint) {

        fingerDb.get(finger).add(fingerprint);
        fingerprints.add(fingerprint);
    }

    public void remove(Finger finger) {

        for (Fingerprint fingerprint : fingerDb.get(finger)) {
            fingerprints.remove(fingerprint);
        }
        fingerDb.remove(finger);
    }

    public void remove(Finger finger, Fingerprint fingerprint) {

        fingerprints.remove(fingerprint);
        fingerDb.get(finger).remove(fingerprint);
    }

    public List<Fingerprint> getFingerprints(Finger finger) {
        return fingerDb.get(finger);
    }

    public List<Fingerprint> getFingerprints() {
        return fingerprints;
    }

    public Map<Finger, List<Fingerprint>> getFingerDb() {
        return fingerDb;
    }

    public Finger getFinger(Fingerprint fingerprint) {

        for (Map.Entry<Finger, List<Fingerprint>> entry : fingerDb.entrySet()) {

            if (entry.getValue().contains(fingerprint)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void saveDB(String path) throws Exception {

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(path)));
        objectOutputStream.writeObject(this);
        // System.out.println("" + this.fingerprints);
        objectOutputStream.close();
    }

//    public void writeTOfile(String path, fingerprintsDatabase data) throws Exception {
//        FileOutputStream f = new FileOutputStream(new File(path));
//        f.write(data);
//        f.close();
//    }
}
