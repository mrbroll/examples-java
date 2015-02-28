package imagefun;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class ImageFun {
    public static void main(String[] args) {
        System.out.println("arg 0: " + args[0]);
        System.out.println("arg 1: " + args[0]);
        File imgFile = new File(args[0]);
        BufferedImage img = null;
        try {
            img = ImageIO.read(imgFile);
        } catch (IOException e) {
        }
        WritableRaster imgEdit = img.getRaster();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
            }
        }
    }
}
