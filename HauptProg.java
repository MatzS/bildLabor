import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class HauptProg {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //aufgaben_02();
        //aufgaben_04_02();
        //aufgaben_05_03();
        //aufgaben_06();
        //aufgaben_07();
        aufgaben_08();
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

    public static void aufgaben_05_03() {

        Mat eingabe = new Mat(9, 8, CvType.CV_16UC1);
        eingabe.put(0,0, gibData_aufgaben_05());
        Bild eingabe_bild = new Bild(eingabe);
        eingabe_bild.speichereIn("saved_as_jpg.jpg", 1);
        Bild ausgabe = new Bild("saved_as_jpg.jpg");
        System.out.println(ausgabe.bildMatrix.dump());
        System.out.println(eingabe_bild.bildMatrix.dump());
        Mat differenz = gibDifferenz(eingabe_bild.bildMatrix, ausgabe.bildMatrix);
        System.out.println(differenz.dump());
        Imgproc.threshold(differenz,differenz, 0, 200, Imgproc.THRESH_BINARY);
        System.out.println(differenz.dump());
        Bild test = new Bild(differenz);
        test.speichereIn("test.jpg");


        // Test mit eigenem Bild
        Mat test_eingabe = Imgcodecs.imread("test_bild.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println("TEST");
        Bild test_eingabe_bild = new Bild(test_eingabe);
        test_eingabe_bild.speichereIn("test_bild_save.jpg", 1);
        Bild ausgabe_bild = new Bild("test_bild_save.jpg");
        Mat test_differenz = gibDifferenz(test_eingabe_bild.bildMatrix, ausgabe_bild.bildMatrix);
        Bild differenz_bild = new Bild(test_differenz);
        differenz_bild.speichereIn("differenz_bild.bmp");
        return;
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

    public static short[] gibData_aufgaben_05() {
        short[] data = {
                255, 255, 255, 255, 255, 10, 255, 255, 255,
                255, 255, 255, 255, 10, 10, 10, 255, 255,
                255, 255, 255, 10, 10, 10, 10, 10, 255,
                255, 255, 10, 10, 10, 10, 10, 10, 10,
                255, 255, 255, 10, 180, 180,180, 10, 255,
                255, 255, 255, 10, 180, 180,180, 10, 255,
                255, 255, 255, 10, 10, 10, 10, 10, 255,
                255, 255, 255, 255, 255, 255,255, 255, 255
        };
        return data;
    }

    public static short[] gibData_auffgaben_06_02() {
        short[] data = {
                185, 190, 195, 203, 190, 190, 186, 190,
                137, 166, 178, 195, 190, 195, 190, 184,
                104, 104, 104, 162, 182, 190, 190, 184,
                131, 104, 104, 117, 159, 171, 182, 178,
                141, 145, 150, 138, 146, 159, 169, 178,
                150, 177, 152, 162, 152, 159, 169, 178,
                126, 165, 157, 155, 159, 159, 164, 169,
                78, 111, 163, 150, 152, 156, 162, 169
        };
        return data;
    }

    public static short[][] gibData_auffgaben_06_03() {
        short[][] data = {
                {185, 190, 195, 203, 190, 190, 186, 190},
                {137, 166, 178, 195, 190, 195, 190, 184},
                {104, 104, 104, 162, 182, 190, 190, 184},
                {131, 104, 104, 117, 159, 171, 182, 178},
                {141, 145, 150, 138, 146, 159, 169, 178},
                {150, 177, 152, 162, 152, 159, 169, 178},
                {126, 165, 157, 155, 159, 159, 164, 169},
                {78, 111, 163, 150, 152, 156, 162, 169}
        };
        return data;
    }

    public static Mat gibDifferenz(Mat matrix1, Mat matrix2) {
        // Size und Type müssen für alle Matrizen identisch definiert werden.
        Mat differenz = new Mat(matrix1.rows(), matrix1.cols(), matrix1.type());
        // absdiff findet den Absolutbetrag der Differenz zwischen Grauwerten,
        // die unter gleichen Positionen sind (bzw. differenz = matrix1 – matrix2)
        Core.absdiff(matrix1, matrix2, differenz);
        return differenz;
    }

    public static void aufgaben_06() {
        // Aufgabe 1
        float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        Mat matrix = new Mat(5, 3, CvType.CV_32FC1);
        matrix.put(0, 0,data);
        System.out.println(matrix.get(1,1)[0]);

        // Aufgabe 2
        /*
        Mat eingabe = new Mat(8, 8, CvType.CV_16UC1);
        eingabe.put(0,0, punkt_operation_06_2(gibData_auffgaben_06_02(), 0.1));
        Bild eingabe_bild = new Bild(eingabe);
        eingabe_bild.showHistogramm("Test", 10000, 0);
        */
        // Aufgabe 3
        int[][] quant_tabelle = {
                {16, 11, 10, 16, 24, 40, 51, 61},
                {12, 12, 14, 19, 26, 58, 60, 55},
                {14, 13, 16, 24, 40, 57, 69, 56},
                {14, 17, 22, 29, 51, 87, 80, 62},
                {18, 22, 37, 56, 68, 109, 103, 77},
                {24, 35, 55, 64, 81, 104, 113, 92},
                {49, 64, 78, 87, 103, 121, 120, 101},
                {72, 92, 95, 98, 112, 100, 103, 99}
        };
        short[][] data_array = gibData_auffgaben_06_03();
        double [][] trans_werte = gibTransformierteMatrix(data_array);
        for(int i = 0; i < trans_werte.length; i++) {
            for(int j = 0; j< trans_werte[i].length; j++) {
                System.out.print(" " + trans_werte[i][j] + " ");
            }
            System.out.println(" ");
        }
        System.out.println();
        double[][] quant_werte = quantisiere(trans_werte, quant_tabelle);
        for(int i = 0; i < quant_werte.length; i++) {
            for(int j = 0; j< quant_werte[i].length; j++) {
                System.out.print(" " + quant_werte[i][j] + " ");
            }
            System.out.println(" ");
        }
        System.out.println();
        int[][] reconstruct = rekonstruiere(quant_werte,quant_tabelle);
        for(int i = 0; i < reconstruct.length; i++) {
            for(int j = 0; j< reconstruct[i].length; j++) {
                System.out.print(" " + reconstruct[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    public static short[] punkt_operation_06_2(short[] data, double gamma) {
        for(int i = 0; i< data.length; i++) {
            double value = 256* Math.pow((data[i]/ (double)256), 1/gamma);
            data[i] = (short) value;
        }
        return data;
    }

    public static double gibKoeffizient (short[][] grauwerte, int u, int v) {
        double koeffizient = 0;
        int n = grauwerte.length;
        double korrekturFaktor_u = u == 0 ? Math.sqrt(1.0 / n) : Math.sqrt(2.0 / n);
        double korrekturFaktor_v = v == 0 ? Math.sqrt(1.0 / n) : Math.sqrt(2.0 / n);
        // TODO: Verschachtelte Iterationen über die Originalmatrix.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                koeffizient += grauwerte[i][j] * Math.cos( (u * Math.PI *(2 * i + 1)) / (2 * n))
                        * Math.cos((v * Math.PI *(2 * j + 1)) / (2 * n));
            }
        }
        // Benutzen Sie die DCT-Funktion aus den Folien.
        // Sie können die Java-Funktionen Math.cos(…) und Math.PI verwenden.
        return korrekturFaktor_u * korrekturFaktor_v * koeffizient;
    }
    public static double[][] gibTransformierteMatrix (short[][] grauwerte) {
        int n = grauwerte.length;
        double[][] koeffizienten = new double[n][n];
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                koeffizienten[u][v] = gibKoeffizient(grauwerte, u, v);
            }
        }
        return koeffizienten;
    }

    public static double[][] quantisiere(double[][] koeffizienten, int[][] quantisierungsTabelle) {
        int n = koeffizienten.length;
        double[][] quantisierteWerte = new double[n][n];
        for (int i = 0; i < koeffizienten.length; i++) {
            for (int j = 0; j < koeffizienten[i].length; j++) {
                quantisierteWerte[i][j] = koeffizienten[i][j] / quantisierungsTabelle[i][j];
            }
        }
        return quantisierteWerte;
    }

    public static int[][] rekonstruiere(double[][] quant_werte, int[][] quantisierungstabelle) {
        int n = quant_werte.length;
        int[][] rekonstruierterBlock = new int[n][n];

        // Dequantisierung
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                quant_werte[i][j] *= quantisierungstabelle[i][j];
            }
        }

        // Rücktransformation
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                double summe = 0;
                for (int u = 0; u < n; u++) {
                    for (int v = 0; v < n; v++) {
                        double korrekturFaktor_u = u == 0 ? Math.sqrt(1.0 / n) : Math.sqrt(2.0 / n);
                        double korrekturFaktor_v = v == 0 ? Math.sqrt(1.0 / n) : Math.sqrt(2.0 / n);
                        summe += korrekturFaktor_u * korrekturFaktor_v * quant_werte[u][v] *
                                Math.cos(u * Math.PI * (2 * x + 1) / (2.0 * n)) *
                                Math.cos(v * Math.PI * (2 * y + 1) / (2.0 * n));
                    }
                }
                rekonstruierterBlock[x][y] = (int) Math.round(summe);
            }
        }

        return rekonstruierterBlock;
    }

    public static void aufgaben_07() {
        Mat test = new Mat(5,5, CvType.CV_16UC1);
        test.put(0,0, gib_data_07());
        Mat filter = new Mat(3,3,CvType.CV_16UC1);
        filter.put(0,0,filter());
        Imgproc.filter2D(test,test,-1,filter);
        System.out.println(test.dump());
    }

    public static short[] gib_data_07() {
        return new short[] {
                0, 50, 0, 3, 1,
                4, 50, 3, 2, 1,
                0, 50, 0, 3, 0,
                0, 50, 3, 4, 50,
                5, 50, 1, 3, 1
        };
    }

    public static short[] filter() {
        return new short[] {
                0, 1, 0,
                0, 1, 0,
                0, 1, 0
        };
    }

    public static void aufgaben_08() {
        Bild original = new Bild("Balkon.jpg");
        //Imgproc.cvtColor(eingabe, eingabe, Imgproc.COLOR_RGB2GRAY);
        // Laplace-Filter
        float[] laplaceGewichte = { 0, 1, 0, 1, (-4), 1, 0, 1, 0 };
        Filter laplace = new Filter(laplaceGewichte,3);
        Bild laplaceBild = filter_anwenden(original, laplace);
        laplaceBild.speichereIn("Laplace.bmp");
        Bild laplace_binary = laplaceBild.toBinary(120);
        laplace_binary.speichereIn("laplace_binary.bmp");
        Bild laplace_sharp = bild_schaerfen(laplaceBild, laplace);
        Bild laplace_binary_sharp = bild_schaerfen(laplace_binary, laplace);
        laplace_sharp.speichereIn("laplace_sharp.bmp");
        laplace_binary_sharp.speichereIn("laplace_binary_sharp.bmp");
        laplaceBild.canny(100,200).speichereIn("Laplace_canny.bmp");
        // Vertikal
        float[] vertikalGewichte = { -1, 0, 1, -2, 0, 2, -1,0, 1 };
        Filter vertikal = new Filter(vertikalGewichte,3);
        Bild vertikalBild = filter_anwenden(original, vertikal);
        vertikalBild.speichereIn("vertikal.bmp");
        Bild vertikal_binary = vertikalBild.toBinary(120);
        vertikal_binary.speichereIn("vertikal_binary.bmp");
        Bild vertikal_sharp = bild_schaerfen(vertikalBild, vertikal);
        Bild vertikal_binary_sharp = bild_schaerfen(vertikal_binary, vertikal);
        vertikal_sharp.speichereIn("vertikal_sharp.bmp");
        vertikal_binary_sharp.speichereIn("vertikal_binary_sharp.bmp");
        vertikalBild.canny(100,200).speichereIn("vertikal_canny.bmp");
        // Horizontal
        float[] horizontalGewichte = { -1, -2, -1, 0, 0, 0, 1,2, 1 };
        Filter horizontal = new Filter(horizontalGewichte,3);
        Bild horizontalBild = filter_anwenden(original, horizontal);
        horizontalBild.speichereIn("horizontal.bmp");
        Bild horizontal_binary = laplaceBild.toBinary(120);
        horizontal_binary.speichereIn("horizontal_binary.bmp");
        Bild horizontal_sharp = bild_schaerfen(horizontalBild, horizontal);
        Bild horizontal_binary_sharp = bild_schaerfen(horizontal_binary, horizontal);
        horizontal_sharp.speichereIn("horizontal_sharp.bmp");
        horizontal_binary_sharp.speichereIn("horizontal_binary_sharp.bmp");
        horizontalBild.canny(200,230).speichereIn("horizontal_canny.bmp");
    }

    public static Bild filter_anwenden(Bild original, Filter f) {
        Mat ausgabe = f.passFilter(original.bildMatrix, new Scalar(4));
        return new Bild(ausgabe);
    }

    public static Bild bild_schaerfen(Bild gefiltert, Filter f) {
        Mat filter = new Mat(3,3,CvType.CV_16UC1);
        filter.put(0,0,new short[] {
                1, 1, 1,
                1, 1, 1,
                1, 1, 1
        });
        Mat ausgabe = f.schaerfeEindruck(gefiltert.bildMatrix, new Scalar(1.0));
        Imgproc.filter2D(ausgabe,ausgabe,-1,filter);
        return new Bild(ausgabe);
    }
}