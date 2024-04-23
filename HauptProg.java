import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class HauptProg {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //aufgaben_02();
        aufgaben_04_02();
    }
    public static void aufgaben_02() {
        Bild eingabe = new Bild("Blume.jpg");
        Histogramm eingabe_histo = new Histogramm();
        eingabe_histo.generiere(eingabe.bildMatrix);
        //eingabe.showHistogramm("Eingabe", 30000, 0);
        int [] histogramm = eingabe_histo.gibHistogramm(eingabe.bildMatrix);
        float [] lut = eingabe_histo.gibNormalisierteLUT(histogramm, eingabe.bildMatrix.cols() * eingabe.bildMatrix.rows());
        Mat ausgabe = eingabe_histo.gibAequalisierteMatrix(lut, eingabe.bildMatrix);
        Bild ausgabe_bild = new Bild(ausgabe);
        ausgabe_bild.showHistogramm("Ausgabe", 30000, 0);
        ausgabe_bild.speichereIn("ausgabe.jpg");
    }

    public static void aufgaben_04_02() {
        Mat eingabe = new Mat (6,5, CvType.CV_16UC1);
        eingabe.put(0,0,gibData());
        new Bild(eingabe).speichereIn("Original.jpg");
        Histogramm hist = new Histogramm();
        int[] histogramm = hist.gibHistogramm(eingabe);
        float[] lut = hist.gibNormalisierteLUT(histogramm, eingabe.width() * eingabe.height());
        Mat ausgabe = hist.gibAequalisierteMatrix(lut, eingabe);
        for(int i=0; i< ausgabe.cols(); i++) {
            for(int j = 0; j< ausgabe.rows(); j++) {
                System.out.print(ausgabe.get(j,i)[0] + " ");
            }
            System.out.println(" ");
        }
        new Bild(ausgabe).speichereIn("Aequalisiert.jpg");
    }

    public static short[] gibData() {
        short[] data = {102, 102, 105, 104, 104,
                        103, 101, 101, 101, 103,
                        105, 101, 107, 101, 105,
                        104, 101, 101, 101, 104,
                        102, 101, 106, 101, 102,
                        106, 104, 108, 105, 108 };
        return data;
    }
}