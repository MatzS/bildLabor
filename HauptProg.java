import org.opencv.core.Core;
import org.opencv.core.Mat;

public class HauptProg {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        aufgaben_02();
    }
    public static void aufgaben_02() {
        short[] bildPunkte = {106, 109, 108, 105, 108,
                110, 104, 107, 108 ,107,
                106, 109, 108, 105, 108,
                110 ,104, 107, 108 ,107,
                106, 103, 105, 109, 109};



        Bild eingabe = new Bild("Blume.jpg");
        Histogramm eingabe_histo = new Histogramm();
        eingabe_histo.generiere(eingabe.bildMatrix);
        int [] histogramm = eingabe_histo.gibHistogramm(eingabe.bildMatrix);
        float [] lut = eingabe_histo.gibNormalisierteLUT(histogramm, eingabe.bildMatrix.cols() * eingabe.bildMatrix.rows());
        //eingabe.show("Eingabebild");
        //eingabe.wartung(10000);
        Mat ausgabe = eingabe.gibAequalisierteMatrix(lut, eingabe.bildMatrix);
        Bild ausgabe_bild = new Bild(ausgabe);
        ausgabe_bild.show("Ausgabe");
        ausgabe_bild.wartung(10000);
    }
}