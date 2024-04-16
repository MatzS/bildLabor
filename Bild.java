import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
public class Bild {
    // Attribute
    public Mat bildMatrix;
    public short [] grauwerteArray;
    // Konstruktoren
    public Bild(String dateiName) {
        bildMatrix = Imgcodecs.imread(dateiName, Imgcodecs.IMREAD_UNCHANGED);
        Imgproc.cvtColor (bildMatrix, bildMatrix, Imgproc.COLOR_BGR2GRAY);
        if (bildMatrix.empty()) {
            System.err.println("nicht gefunden: " + dateiName);
            System.exit(0);
        } else {
            grauwerteArray = new short [bildMatrix.cols() * bildMatrix.rows()];
            int index = 0;
            for (int y = 0; y < bildMatrix.rows(); y++) {
                for (int x = 0; x < bildMatrix.cols(); x++) {
                    double[] pixelWerte = bildMatrix.get(y, x);
                    grauwerteArray[index] = (short) pixelWerte[0];
                    index ++;
                }
            }
            System.out.println(grauwerteArray);
        }
    }


    public Bild(Mat matrix) {
        bildMatrix = matrix;
    }
    public Bild(short[] array) {
        bildMatrix = new Mat(5,5, CvType.CV_16UC1);
        bildMatrix.put(0,0, array);
    }
    // Methoden
    public void speichereIn(String dateiName) {
        Imgcodecs.imwrite(dateiName, bildMatrix);
    }

    public void show(String name) {
        HighGui.imshow(name, bildMatrix);
        HighGui.namedWindow(name, HighGui.WINDOW_AUTOSIZE);
    }

    public void wartung(int zeit) {
        HighGui.waitKey(zeit);
        System.exit(0);
    }

    public void groesseAendern(double faktor) {
        if (faktor > 0) {
            double breite = bildMatrix.size().width * faktor;
            double hoehe = bildMatrix.size().height * faktor;
            Size size = new Size(breite, hoehe);
            Imgproc.resize(bildMatrix, bildMatrix, size);
        }
    }

    // Aufgaben 02-2
    public Bild spaltenFarbeAendern(int n, int m, double[] farbe) {
        Mat ausgabeMatrix = bildMatrix.clone();
        if (n > -1 && m < 256 && n < m) {
            int zeilen = ausgabeMatrix.rows();
            for (int spaltenNr = n; spaltenNr <= m; spaltenNr++) {
                for (int zeilenNr = 0; zeilenNr < zeilen; zeilenNr++) {
                    ausgabeMatrix.put(zeilenNr, spaltenNr, farbe);
                }
            }
        }
        Bild ausgabe = new Bild(ausgabeMatrix);
        return ausgabe;
    }

    public void showHistogramm(String titel, int zeit, int kanal) {
        Histogramm histogramm = new Histogramm();
        histogramm.generiere(bildMatrix);
        histogramm.showMittelWertUndAbweichung(bildMatrix, kanal);
        HighGui.imshow(titel, histogramm.histogrammMatrix);
        HighGui.waitKey(zeit);
        System.exit(0);
    }

    public Mat gibAequalisierteMatrix(float[] lut, Mat bildMatrix) {
        Mat ausgabe = new Mat(bildMatrix.rows(), bildMatrix.cols(), bildMatrix.type());
        int [] sums = new int [lut.length];
        sums[0] = (int) lut[0] * 255;
        for(int i=1; i< lut.length; i++) {
            sums[i] = (int) (sums[i-1] + lut[i] * 255);
        }
        for (int y = 0; y < bildMatrix.rows(); y++) {
            for (int x = 0; x < bildMatrix.cols(); x++) {
                short pixelValue = (short) bildMatrix.get(y, x)[0];
                float newValue = sums[pixelValue];
                ausgabe.put(y, x, newValue);
            }
        }
        return ausgabe;
    }
}
