package diploma;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageMarker {

    private static void markImages(String path) throws Exception {

        Pattern pattern = Pattern.compile("\\d+");

        for (File file : new File(path).listFiles()) {

            ImagePlus imagePlus = new ImagePlus(file.getAbsolutePath());
            Matcher matcher = pattern.matcher(file.getName());
            if (matcher.find()) {

                int number = Integer.parseInt(matcher.group());

                ByteProcessor byteProcessor = (ByteProcessor) imagePlus.getProcessor();

                for (int i = 0; i < byteProcessor.getWidth(); i++) {
                    byteProcessor.set(i, 0, number);
                }
                new FileSaver(imagePlus).saveAsJpeg();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        markImages("C:\\Users\\user54\\Desktop\\data");
    }
}
