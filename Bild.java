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
    // Konstruktoren
    public Bild(String dateiName) {
        bildMatrix = Imgcodecs.imread(dateiName, Imgcodecs.IMREAD_GRAYSCALE);
        //Imgproc.cvtColor (bildMatrix, bildMatrix, Imgproc.COLOR_BGR2GRAY);
        /*
        Mat convertedMatrix = new Mat(bildMatrix.rows(), bildMatrix.cols(), CvType.CV_16UC1);

        // Konvertiere die Werte von 8-Bit auf 16-Bit
        for (int i = 0; i < bildMatrix.rows(); i++) {
            for (int j = 0; j < bildMatrix.cols(); j++) {
                // Lese den Wert aus der Originalmatrix und skaliere ihn auf den Bereich von 16 Bit
                double originalValue = bildMatrix.get(i, j)[0]; // Grauwerte sind normalerweise im Index 0
                int convertedValue = (int) (originalValue); // Skaliere den Wert auf den Bereich von 16 Bit
                convertedMatrix.put(i, j, convertedValue);
            }
        }
        bildMatrix = convertedMatrix;
        */

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

    public void speichereIn(String dateiName, int qualitaet) {
        MatOfInt map = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, qualitaet);
        Imgcodecs.imwrite(dateiName, bildMatrix, map);
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

    public Bild toBinary(double thresholdValue) {
        Mat ausgabe = new Mat(bildMatrix.rows(), bildMatrix.cols(), bildMatrix.type());
        Imgproc.threshold(bildMatrix,ausgabe,thresholdValue,255, Imgproc.THRESH_BINARY);
        return new Bild(ausgabe);
    }

    public Bild canny(double schwelle1, double schwelle2) {
        Mat edges = new Mat();
        Imgproc.Canny(bildMatrix, edges, schwelle1, schwelle2);
        return new Bild(edges);
    }
}
