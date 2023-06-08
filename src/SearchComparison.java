import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class SearchComparison {
    // Constants
    private static final int ELEMENTS_SIZE = 100;      // The size of the elements array
    private static final int INITIAL_SIZE = 1000;      // The initial size for the search array
    private static final int MAXIMUM_SIZE = 1000000;   // The final size for the search array
    private static final int INCREMENT = 10000;        // The increment size for the search array in each iteration

    private static final int MAX_VALUE = 5000;         // The maximum value for any element in the arrays
    private static final int LAST_VALUE = 5001;        // The value to be added as the 100th element in the elements array
    private static final int HASH_TABLE_SIZE = 9973;    // The size of the hash table

    public static void main(String[] args) {
        try {
            // Create a FileWriter to write the results to a CSV file
            FileWriter resultsBinary = new FileWriter("resultsBinary2.csv");
            FileWriter resultsHashTable = new FileWriter("resultsHashTable.csv");
            resultsBinary.write("Size,Time\n");
            resultsHashTable.write("Size,Time\n");

            // Create the fixed element array
            ArrayList<Integer> elementsArray = generateElementsArray();

            // Create search arrays starting from INITIAL_SIZE to MAXIMUM_SIZE, with each iteration incrementing by 10,000
            for (int n = INITIAL_SIZE; n <= MAXIMUM_SIZE; n += INCREMENT) {
                // Create Search array of size n
                ArrayList<Integer> searchArray = generateSearchArray(n);

                // Timer Start
                long startTime = System.nanoTime();

                // Binary Search
                for (Integer element : elementsArray) {
                    binarySearch(searchArray, element);
                }

                long endTime = System.nanoTime();

                // Hash Table Search
                HashTable hashTable = createHashTable(searchArray);
                for (Integer element : elementsArray) {
                    hashTable.search(element);
                }

                long endTime2 = System.nanoTime();

                // Calculate total time
                long totalTimeBinary = endTime - startTime;
                long totalTimeHashTable = endTime2 - endTime;

                // Writing results to file
                resultsBinary.write(n + "," + totalTimeBinary + "\n");
                resultsHashTable.write(n + "," + totalTimeHashTable + "\n");

                // Memory management and garbage collection
                searchArray = null;
                hashTable = null;
                System.gc();
            }

            // Closing the files
            resultsBinary.close();
            resultsHashTable.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Randomly generates a search array of size n (between INITIAL_SIZE and MAXIMUM_SIZE) and the values in the
     * array are from 0 to MAX_VALUE.
     *
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
     * Randomly generates an elements array of size ELEMENTS_SIZE, and the elements in the array are distinct integers
     * between 0 and MAX_VALUE, except for the last element which is set to LAST_VALUE.
     *
     * @return the elements array
     */
    private static ArrayList<Integer> generateElementsArray() {
        // HashSet for distinct integers
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

    /**
     * Binary search implementation on the search array to search for elements from the elements array.
     *
     * @param searchArray the array to be searched
     * @param element     the integer value that is being searched for
     * @return the index of the target value within the searchArray if it is present, otherwise returns -1
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
            // update to middle index + 1 to set the left bound.
            else if (searchArray.get(middle) < element) {
                left = middle + 1;
            }
            // If the element value is less than the value at the middle index,
            // update to middle index - 1 to set the right bound.
            else {
                right = middle - 1;
            }
        }

        return -1;  // return -1 if element not found
    }

    /**
     * Creates a hash table using chaining and inserts elements from the search array into the hash table.
     *
     * @param searchArray the array to be inserted into the hash table
     * @return the created hash table
     */
    private static HashTable createHashTable(ArrayList<Integer> searchArray) {
        HashTable hashTable = new HashTable();

        for (int element : searchArray) {
            hashTable.insert(element);
        }

        return hashTable;
    }
}