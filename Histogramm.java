import java.util.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
public class Histogramm {
    int histSize = 256;
    int breite = 512;
    int hoehe = 400;
    float[] range = { 0, 256 };
    Scalar hintergrund = new Scalar(255, 255, 255);
    Scalar vordergrund = new Scalar(0, 0, 0);
    Mat histogrammMatrix;
    public void generiere(Mat bildMatrix) {
        // das Bild in drei RGB-Kanäle verteilen.
        List<Mat> bgr = new ArrayList<>();
        Core.split(bildMatrix, bgr);
        // Erstelle eine Matrix, um das Histogramm-Bild nur für Kanal_0 dort zu speichern
        histogrammMatrix = gibHistogrammMatrix(bgr, 0);
    }
    public Mat gibHistogrammMatrix(List<Mat> bgr, int kanal) {
        Mat matrix = new Mat();
        Mat histImage = new Mat(hoehe, breite, CvType.CV_8UC1, hintergrund);
        // Setze die Werte zwischen 0 und 255
        MatOfFloat histRange = new MatOfFloat(range);
        // Rechne die Histogramm-Werte
        Imgproc.calcHist(bgr, new MatOfInt(kanal), new Mat(), matrix, new MatOfInt(histSize), new MatOfFloat(histRange), false);
        // Gib Feldern eine ähnliche Größe
        int pixelBreite = (int) Math.round((double) breite / histSize);
        // Vor dem Zeichnen muss Histogramm normalisiert werden.
        Core.normalize(matrix, matrix, 0, histImage.rows(), Core.NORM_MINMAX);
        // matrix in einem 1D-Array eintragen
        float[] histData = new float[(int) (matrix.total() * matrix.channels())];
        matrix.get(0, 0, histData);
        // zeichnen
        for (int i = 1; i < histSize; i++) {
            Imgproc.line(histImage, new Point(pixelBreite * (i - 1), hoehe - Math.round(histData[i - 1])),
                    new Point(pixelBreite * (i), hoehe - Math.round(histData[i])), vordergrund, 2);
        }
        return histImage;
    }
    public void showMittelWertUndAbweichung(Mat bildMatrix, int kanal) {
        MatOfDouble mittelwert = new MatOfDouble();
        MatOfDouble abweichung = new MatOfDouble();
        Core.meanStdDev(bildMatrix, mittelwert, abweichung);
        double[] mittelwertKanaele = mittelwert.get(0,0);
        double[] abweichungKanaele = abweichung.get(0,0);
        System.out.println("Mittelwert:" + mittelwertKanaele[kanal]);
        System.out.println("Abweichung:" + abweichungKanaele[kanal]);
    }

    public int[] gibHistogramm(Mat bildMatrix) {
        int[] histogramm = new int[256];
        for (int y = 0; y < bildMatrix.rows(); y++) {
            for (int x = 0; x < bildMatrix.cols(); x++) {
                short pixelValue = (short) bildMatrix.get(y, x)[0];
                histogramm[pixelValue] += 1;
            }
        }
        return histogramm;
    }

    public float[] gibNormalisierteLUT(int[] histogramm, int anzahlPixel) {
        float[] lut = new float[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = ((float) histogramm[i]) / ((float) anzahlPixel);
        }
        return lut;
    }

    public Mat gibAequalisierteMatrix(float[] lut, Mat bildMatrix) {
        Mat ausgabe = new Mat(bildMatrix.rows(), bildMatrix.cols(), bildMatrix.type());
        float [] sums = new float [lut.length];
        sums[0] = lut[0] * 255;
        for(int i=1; i< lut.length; i++) {
            sums[i] = sums[i-1] + lut[i] * 255;
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
