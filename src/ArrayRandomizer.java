/**
 * Monmoy Maahdie (30149094)
 * Fairooz Shafin (30149774)
 * CPSC 331 Spring 2023 A4
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * This class is used to compare the running time of linear search, binary search, and searching a hash table by generating
 * random search arrays between size 1,000 and 1,000,000 (with increments of 10,000) and element array of constant size 100.
 * The program searches the search array for elements found in the element array and records the time in a csv file.
 */
public class ArrayRandomizer {
    // Constants
    private static final int ELEMENTS_SIZE = 100;      // The size of the elements array
    private static final int INITIAL_SIZE = 1000;      // The initial size for the search array
    private static final int MAXIMUM_SIZE = 1000000;   // The final size for the search array
    private static final int INCREMENT = 10000;		   // The increment size for the search array in each iteration

    private static final int MAX_VALUE = 5000;         // The maximum value for any element in the arrays
    private static final int LAST_VALUE = 5001;        // The value to be added as the 100th element in the elements array


    public static void main(String[] args) {
        try {
            // Create a FileWriter to write the results to a csv file
            FileWriter resultsLinear = new FileWriter("resultsLinear.csv");
            FileWriter resultsBinary = new FileWriter("resultsBinary.csv");
            resultsLinear.write("Size,Time\n");
            resultsBinary.write("Size,Time\n");

            // Create the fixed element array
            ArrayList<Integer> elementsArray = generateElementsArray();

            // Create search arrays starting from INITIAL_SIZE to MAXIMUM_SIZE, with each iteration incrementing by 10,000
            for (int n = INITIAL_SIZE; n <= MAXIMUM_SIZE; n += INCREMENT) {
                // Create Search arrray of size n
                ArrayList<Integer> searchArray = generateSearchArray(n);
                ArrayList<Integer> tempSearchArray = searchArray;
                quickSort(tempSearchArray);
                // Timer Start
                long startTime = System.nanoTime();

                // SEQUENTIAL SEARCH
                sequentialSearch(searchArray, elementsArray);

                // Timer end
                long endTime = System.nanoTime();

                //Binary Search
                for (Integer element : elementsArray) {
                    binarySearch(tempSearchArray, element);
                }

                long endTime2 = System.nanoTime();

                // Calculate total time
                long totalTimeLinear = endTime - startTime;
                long totalTimeBinary = endTime2 - endTime;

                // Writing results to file
                resultsLinear.write(n + "," + totalTimeLinear + "\n");
                resultsBinary.write(n + "," + totalTimeBinary + "\n");

                System.out.println(n + "," + totalTimeLinear + "\n");

                // Memory management and garbage collection. Source: https://www.freecodecamp.org/news/garbage-collection-in-java-what-is-gc-and-how-it-works-in-the-jvm/
                searchArray = null;
                System.gc();
            }

            // Closing the file
            resultsLinear.close();
            resultsBinary.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Randomly generates a search array of size n (between INITIAL_SIZE (1000) and MAXIMUM_SIZE(1000000)) and the values in the
     * array are from 0 to MAX_VALUE (5000)
     * @param n the size of the search array
     * @return the search array
     */
    private static ArrayList<Integer> generateSearchArray(int n) {
        // Random variable
        Random rand = new Random();

        // Initialize search array
        ArrayList<Integer> searchArray = new ArrayList<>();

        // Add randomly generated integers to search array
        for (int i = 0; i < n; i++) {
            searchArray.add(rand.nextInt(MAX_VALUE + 1)); // Random value between 0 and MAX_VALUE
        }

        return searchArray;
    }

    /**
     * Randomly generates an elements array of size ELEMENTS_SIZE, and the elemnts in the array are distinct integers between
     * 0 and MAX_VALUE(5000), except for the last element which is set to LAST_VALUE (5001).
     * @return the elements array
     */
    private static ArrayList<Integer> generateElementsArray() {
        // HashSet for distinct integers. Souce: https://www.w3schools.com/java/java_hashset.asp
        HashSet<Integer> numbers = new HashSet<>();

        // Random Variable
        Random rand = new Random();

        // Add randomly generated distinct integers to HashSet
        while (numbers.size() < ELEMENTS_SIZE - 1) {
            int randomNum = rand.nextInt(MAX_VALUE + 1);
            numbers.add(randomNum);
        }

        // Convert the HashSet to an ArrayList
        ArrayList<Integer> elementsArray = new ArrayList<>(numbers);

        // Add LAST_VALUE as the 100th element
        elementsArray.add(LAST_VALUE);

        return elementsArray;
    }

    // SEARCHING

    /** Linear search implementation by iterating through element of search array and searching for elements from element array
     * in the search array.
     *
     * @param searchArray: Array to be searched
     * @param elementsArray: Array containing elements to search in the Search Array.
     */
    private static void sequentialSearch(ArrayList<Integer> searchArray, ArrayList<Integer> elementsArray) {
        for (int element : elementsArray) {
            for (int j = 0; j < searchArray.size(); j++) {
                if (searchArray.get(j).equals(element)) {
                    break;
                }
            }
        }
    }

    //Used reference from: https://www.geeksforgeeks.org/binary-search/
    /**
     * Binary search implementation on the search array to search for elements from element array
     *
     * @param searchArray: Array to be searched
     * @param element The integer value that is being searched for
     *
     * @return The index of the target value within the searchArray if it is present, otherwise returns -1.
     */
    private static int binarySearch(ArrayList<Integer> searchArray, int element) {
        int left = 0;
        int right = searchArray.size() - 1;

        while (left <= right) {
            // Calculate the middle index
            int middle = (left + right) / 2;

            // If the element value is found at the middle index, return the middle index.
            if (searchArray.get(middle) == element) {
                return middle;
            }
            // If the element value is greater than the value at the middle index,
            // update to middle index + 1 to for left bound.
            else if (searchArray.get(middle) < element) {
                left = middle + 1;
            }
            // If the element value is less than the value at the middle index,
            // update to middle index - 1 for right bound.
            else {
                right = middle - 1;
            }
        }


        return -1;  // return -1 if element not found
    }

    // QUICK SORT. Source: D2L Code Sorting.java (By Dr Jalal Kawash)

    public static void quickSort(ArrayList<Integer> list) {
        if (list.isEmpty()) return;
        else recQuickSort(list, 0, list.size() - 1);
    }

    private static void recQuickSort(ArrayList<Integer> list, int low, int high) {
        if (low < high) {
            int pivot = partition(list, low, high);
            recQuickSort(list, low, pivot - 1);
            recQuickSort(list, pivot + 1, high);
        }
    }

    private static int partition(ArrayList<Integer> list, int low, int high) {
        Integer x = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(x) <= 0) {
                i++;
                if (i != j) {
                    //Source: https://www.tutorialspoint.com/swap-elements-of-arraylist-with-java-collections#:~:text=In%20order%20to%20swap%20elements,specified%20positions%20in%20the%20list.
                    Collections.swap(list, i, j);
                }
            }
        }
        if (high != i + 1) Collections.swap(list, high, i + 1);
        return i + 1;
    }
}