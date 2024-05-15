import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Filter {
    float[] gewichte = null;
    Mat filterMatrix;
    public Filter(float[] werte, int groesse) {
        gewichte = werte;
        filterMatrix = new Mat(groesse, groesse, CvType.CV_32F);
        filterMatrix.put(0, 0, werte);
    }
    public Mat passFilter(Mat eingabe, Scalar faktor) {
        Mat ausgabe = new Mat(eingabe.rows(), eingabe.cols(), eingabe.type());
        Imgproc.filter2D(eingabe, ausgabe, -1, filterMatrix);
        Core.multiply(ausgabe, faktor, ausgabe);
        return ausgabe;
    }
    public Mat schaerfeEindruck(Mat eingabe, Scalar faktor) {
        Mat ausgabe = passFilter(eingabe, faktor);
        Core.subtract(eingabe, ausgabe, ausgabe);
        return ausgabe;
    }
}
