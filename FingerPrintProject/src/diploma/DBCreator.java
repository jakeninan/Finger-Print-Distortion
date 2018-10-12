package diploma;

import db.datas;
import diploma.matching.FingerprintsDatabase;
import diploma.matching.Matcher;
import diploma.model.Finger;
import diploma.model.Fingerprint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DBCreator {

    static final String sourceFile1Path = "src/datas/Distroted";
    static final String sourceFile2Path = "src/datas/Normal";

    static final String mergedFilePath = "src/datas/train";

    static File files = new File(sourceFile1Path);
    static File files1 = new File(sourceFile2Path);
    static File mergedFile = new File(mergedFilePath);

    public static String ACTION = null;

    private static FingerprintsDatabase formDB(String trainingSamplePath) throws FileNotFoundException, ClassNotFoundException, SQLException {
        File files = new File(Fingerprint.fileName);
        files.delete();
        FingerprintsDatabase fingerprintsDatabase = new FingerprintsDatabase();
        Set<Finger> fingers = new HashSet<>();
        for (File file : new File(trainingSamplePath).listFiles()) {

            System.out.println("Working with " + file.getName());

            String fileName = file.getName();
            String[] parts = fileName.split("\\D+");
            Finger finger = new Finger(parts[0]);
            if (!fingers.contains(finger)) {
                fingerprintsDatabase.addFinger(finger);
                fingers.add(finger);
            }
            fingerprintsDatabase.addFingerprintToFinger(finger, Fingerprint.extractFeatures(file.getAbsolutePath()));
        }
        //   System.out.println("" + fingerprintsDatabase.getFingerDb());

        return fingerprintsDatabase;
    }

    private static List<Fingerprint> extractFeatures(String testSamplePath) throws FileNotFoundException, ClassNotFoundException, SQLException {

        File[] testFiles = new File(testSamplePath).listFiles();
        List<Fingerprint> fingerprints = new LinkedList<>();

        for (File file : testFiles) {

            System.out.println("Working with " + file.getName());

            fingerprints.add(Fingerprint.extractFeatures(file.getAbsolutePath()));
        }
        return fingerprints;
    }

    private static String performTest(FingerprintsDatabase fingerprintsDatabase, List<Fingerprint> testSample,
            double threshold) {

        int matched = 0, unmatched = 0, wrongMatched = 0;
        for (Fingerprint fingerprint : testSample) {

//			System.out.println(Arrays.toString(fingerprint.getFeatureValues()));
            Fingerprint matchedFingerprint = Matcher.match(fingerprintsDatabase, fingerprint, threshold);

            if (matchedFingerprint == null) {
                unmatched++;
            } else if (matchedFingerprint.getId() == fingerprint.getId()) {
                matched++;
            } else {
                wrongMatched++;
            }
        }
        return threshold + " " + matched + " " + unmatched + " " + wrongMatched;
    }

    private static final void transfer(final Reader source, final Writer destination) throws IOException {
        char[] buffer = new char[1024 * 16];
        int len = 0;
        while ((len = source.read(buffer)) >= 0) {
            destination.write(buffer, 0, len);
        }
    }

    public static void mergeFiles(final File output, final File inputfile1, final File inputfile2)
            throws IOException {

        try (
                Reader sourceA = Files.newBufferedReader(inputfile1.toPath());
                Reader sourceB = Files.newBufferedReader(inputfile2.toPath());
                Writer destination = Files.newBufferedWriter(output.toPath(), StandardCharsets.UTF_8);) {

            transfer(sourceA, destination);
            transfer(sourceB, destination);

        }
    }

    public static void step1() throws FileNotFoundException, Exception {
        files.delete();
        files1.delete();
        mergedFile.delete();
//        datas as = new datas();
//        as.truncate("fingerprint.referencetable");
        ACTION = "Distroted";
        formDB("src/distroted/").saveDB("db");
    }

    public static void step2() throws FileNotFoundException, Exception {
        ACTION = "Normal";
        formDB("src/normal/");
    }

    public static void step3() throws FileNotFoundException, Exception {

        mergeFiles(mergedFile, files, files1);

    }

    public static void main(String[] args) throws Exception {
        step1();
        step2();
        step3();
//        formDB("C:\\Users\\user54\\Desktop\\DB4_B\\data").saveDB("db3test");
//
//        FingerprintsDatabase trainDB = FingerprintsDatabase.loadExistent("db3train");
//        FingerprintsDatabase testDB = FingerprintsDatabase.loadExistent("db3test");
//        double t = 0.01;
//        while (t <= 0.2) {
//            System.out.println(performTest(trainDB, testDB.getFingerprints(), t));
//            t += 0.01;
//        }
    }
}
