import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ArrayRandomizer {
    private static final int ELEMENTS_SIZE = 100;
    private static final int INITIAL_SIZE = 1000;
    private static final int MAXIMUM_SIZE = 1000000;
    private static final int INCREMENT = 10000;
    private static final int MAX_VALUE = 5000;
    private static final int LAST_VALUE = 5001;

    public static void main(String[] args) {
        try {
            FileWriter results = new FileWriter("results_part1.csv");
            results.write("Size,Time\n");

            ArrayList<Integer> elementsArray = generateElementsArray();

            for (int n = INITIAL_SIZE; n <= MAXIMUM_SIZE; n += INCREMENT) {
                ArrayList<Integer> searchArray = generateSearchArray(n);

                long startTime = System.nanoTime();

                for (Integer element : elementsArray) {
                    sequentialSearch(searchArray, element);
                }

                long endTime = System.nanoTime();
                long totalTime = endTime - startTime;

                results.write(n + "," + totalTime + "\n");

                searchArray = null;
                System.gc();
            }

            results.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static ArrayList<Integer> generateSearchArray(int n) {
        Random rand = new Random();
        ArrayList<Integer> searchArray = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            searchArray.add(rand.nextInt(MAX_VALUE + 1));
        }

        return searchArray;
    }

    private static ArrayList<Integer> generateElementsArray() {
        HashSet<Integer> numbers = new HashSet<>();
        Random rand = new Random();

        while (numbers.size() < ELEMENTS_SIZE - 1) {
            int randomNum = rand.nextInt(MAX_VALUE + 1);
            numbers.add(randomNum);
        }

        ArrayList<Integer> elementsArray = new ArrayList<>(numbers);
        elementsArray.add(LAST_VALUE);

        return elementsArray;
    }

    private static void sequentialSearch(ArrayList<Integer> searchArray, int element) {
        for (int j = 0; j < searchArray.size(); j++) {
            if (searchArray.get(j).equals(element)) {
                break;
            }
        }
    }
}