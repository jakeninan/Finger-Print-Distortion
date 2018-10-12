package libsvm;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

public class svmclassifiers {

    public void Crossvalidationsvm() throws ClassNotFoundException, SQLException {

    }

    public String result() throws IOException, SQLException, ClassNotFoundException {
        Object predictedClassValue = null;
        Dataset data = FileHandler.loadDataset(new File("src/datas/train"), 4, ",");
        Dataset testDataSet = FileHandler.loadDataset(new File("src/datas/test"), 3, ",");
        Classifier svm = new LibSVM();
        svm.buildClassifier(data);
        for (Instance inst : testDataSet) {
            predictedClassValue = svm.classify(inst);
            System.out.println(predictedClassValue);
        }
        return (String) predictedClassValue;

    }

    public static void main(String[] args) throws Exception {
        svmclassifiers as = new svmclassifiers();
        as.result();

    }
}
