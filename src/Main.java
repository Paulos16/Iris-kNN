import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        int prawidloweWyniki, k;
        double dokladnosc;

        // Wczytanie iris training set:

        String path = "res/iristrain.csv";
        List<Data> trainingSet = readData(path);

        // Wczytanie iris testing set:

        path = "res/iristest.csv";
        List<Data> testingSet = readData(path);

        // Polecenia uzytkownika:

        System.out.println("\nAby przeprowadzic test dla wprowadzonej wartosci 'k', wpisz '1',");
        System.out.println("Aby wyswietlic wykres dokladnosci algorytmu, wpisz '2',");
        System.out.print("Aby zamknac program, wpisz '0': ");
        String response = Main.scan.nextLine();

        while (!response.equals("0")) {
            switch (response) {


                case "1":

                    System.out.print("\nPodaj wartosc 'k': ");
                    k = Main.scan.nextInt();

                    prawidloweWyniki = kNN(k, trainingSet, testingSet);

                    dokladnosc = 100 * (double)prawidloweWyniki / (double)testingSet.size();

                    System.out.println("\nWielkosc danych testowych:\t" + testingSet.size());
                    System.out.println("Prawidlowe wyniki:\t" + prawidloweWyniki);
                    System.out.println("Dokladnosc procentowa:\t" + dokladnosc + "%\n");
                    break;


                case "2":
                    double[] accuracy = new double[100];

                    for (k = 1; k <= 100; k++) {

                        prawidloweWyniki = kNN(k, trainingSet, testingSet);

                        accuracy[k - 1] = 100 * (double)prawidloweWyniki / (double)testingSet.size();

                    }

                    writeData(accuracy);

                    runBash();

                    break;
            }
            System.out.println("\nAby przeprowadzic test dla wprowadzonej wartosci 'k', wpisz '1',");
            System.out.println("Aby wyswietlic wykres dokladnosci algorytmu, wpisz '2',");
            System.out.print("Aby zamknac program, wpisz '0': ");
            response = Main.scan.nextLine();
        }

        System.out.println("\nDziekuje, do zobaczenia! :)");

    }


    private static int kNN(int k, List<Data> trainingSet, List<Data> testingSet) {

        int prawidloweWyniki = 0;

        for (Data d : testingSet) {

            // sortowanie

            trainingSet.sort((e1, e2) -> {
                if (d.calculateDistance(e1) < d.calculateDistance(e2)) {
                    return -1;
                }
                return 1;
            });




            // wybranie k-najbliższych elementów z trainingList

            int setosa = 0, versicolor = 0, virginica = 0;

            for (int i = 0; i < k; i++) {


                if (trainingSet.get(i).getValue().equals("\"setosa\"")) {

                    setosa++;

                } else if (trainingSet.get(i).getValue().equals("\"versicolor\"")) {

                    versicolor++;

                } else {

                    virginica++;
                }


            }




            // wynik KNN:

            String target;

            if (setosa >= versicolor && setosa >= virginica) {

                target = "\"setosa\"";

            } else if (versicolor >= setosa && versicolor >= virginica) {

                target = "\"versicolor\"";

            } else target = "\"virginica\"";





            // poprawka zmiennych wynikowych

            if (d.getValue().equals(target)) {
                prawidloweWyniki++;
            }

        }

        return prawidloweWyniki;
    }






    private static List<Data> readData(String src) {

        List<Data> dataSet = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(src))) {

            String line = br.readLine();
            String[] dataLine = line.split(",");
            String value;
            double[] data;


            while ((line = br.readLine()) != null) {

                data = new double[dataLine.length - 2];

                dataLine = line.split(",");

                value = dataLine[dataLine.length - 1];

                for (int i = 1; i < dataLine.length - 1; i++) {
                    data[i - 1] = Double.parseDouble(dataLine[i]);
                }

                dataSet.add(new Data(value, data));

            }

        } catch (FileNotFoundException ex) {
            System.err.println("Data reading threw an exception:");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("Data reading threw an exception:");
            ex.printStackTrace();
        }

        return dataSet;
    }




    private static void writeData(double[] accuracy) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/accuracy.csv"))) {

            StringBuilder sb = new StringBuilder();


            for (int k = 0; k < accuracy.length - 1; k++) {

                sb.append((k + 1) + "," + String.valueOf(accuracy[k]) + "\n");

            }
            sb.append((accuracy.length) + "," + String.valueOf(accuracy[accuracy.length - 1]));


            bw.write(sb.toString());

        } catch (IOException ex) {
            System.err.println("Data writing threw an exception:");
            ex.printStackTrace();
        }

    }

    private static void runBash() {

        String command = "python python/plotAccuracy.py res/accuracy.csv";
        String[] commands = {
                "/bin/bash",
                "-c",
                "python python/plotAccuracy.py res/accuracy.csv"
        };

        try {

            Runtime.getRuntime().exec(command);

        } catch (IOException ex) {
            System.err.println("Bash commands threw an exception:");
            ex.printStackTrace();
        }

    }

}
