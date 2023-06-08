import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * This class is used to compare the running time of binary search and searching a hash table by generating
 * random search arrays between size 1,000 and 1,000,000 (with increments of 10,000) and element array of constant size 100.
 * The program searches the search array for elements found in the element array and records the time in a csv file.
 */
public class ArrayRandomizerPart2 {
    // Constants
    private static final int ELEMENTS_SIZE = 100;          // The size of the elements array
    private static final int INITIAL_SIZE = 1000;          // The initial size for the search array
    private static final int MAXIMUM_SIZE = 1000000;       // The final size for the search array
    private static final int INCREMENT = 10000;            // The increment size for the search array in each iteration

    private static final int MAX_VALUE = 5000;             // The maximum value for any element in the arrays
    private static final int LAST_VALUE = 5001;            // The value to be added as the 100th element in the elements array

    private static final int HASH_TABLE_SIZE = 9973;       // The size of the hash table

    public static void main(String[] args) {
        try {
            // Create a FileWriter to write the results to a csv file
            FileWriter results = new FileWriter("results_part2.csv");
            results.write("Size,Binary Search Time,Hash Table Search Time\n");

            // Create the fixed element array
            ArrayList<Integer> elementsArray = generateElementsArray();

            // Create search arrays starting from INITIAL_SIZE to MAXIMUM_SIZE, with each iteration incrementing by 10,000
            for (int n = INITIAL_SIZE; n <= MAXIMUM_SIZE; n += INCREMENT) {
                // Create Search array of size n
                ArrayList<Integer> searchArray = generateSearchArray(n);

                // Timer Start for Binary Search
                long binarySearchStartTime = System.nanoTime();

                // Binary Search
                for (Integer element : elementsArray) {
                    binarySearch(searchArray, element);
                }

                // Timer end for Binary Search
                long binarySearchEndTime = System.nanoTime();
                long binarySearchTotalTime = binarySearchEndTime - binarySearchStartTime;

                // Timer Start for Hash Table Search
                long hashTableSearchStartTime = System.nanoTime();

                // Hash Table Search
                HashTable hashTable = createHashTable(searchArray);
                for (Integer element : elementsArray) {
                    hashTableSearch(hashTable, element);
                }

                // Timer end for Hash Table Search
                long hashTableSearchEndTime = System.nanoTime();
                long hashTableSearchTotalTime = hashTableSearchEndTime - hashTableSearchStartTime;

                // Writing results to file
                results.write(n + "," + binarySearchTotalTime + "," + hashTableSearchTotalTime + "\n");

                // Memory management and garbage collection
                searchArray = null;
                hashTable = null;
                System.gc();
            }

            // Closing the file
            results.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Randomly generates a search array of size n (between INITIAL_SIZE (1000) and MAXIMUM_SIZE(1000000)) and the values in the
     * array are from 0 to MAX_VALUE(5000). The search array will have 99 distinct random numbers and one fixed value LAST_VALUE(5001).
     * @param n The size of the search array
     * @return ArrayList<Integer> The generated search array
     */
    private static ArrayList<Integer> generateSearchArray(int n) {
        Random random = new Random();
        HashSet<Integer> uniqueValues = new HashSet<>();
        ArrayList<Integer> searchArray = new ArrayList<>();

        // Generate 99 distinct random numbers
        while (uniqueValues.size() < ELEMENTS_SIZE - 1) {
            uniqueValues.add(random.nextInt(MAX_VALUE + 1));
        }

        // Add the fixed value LAST_VALUE as the 100th element
        uniqueValues.add(LAST_VALUE);

        // Add the values to the search array
        searchArray.addAll(uniqueValues);

        // Add remaining random numbers to fill the search array
        for (int i = searchArray.size(); i < n; i++) {
            searchArray.add(random.nextInt(MAX_VALUE + 1));
        }

        return searchArray;
    }

    /**
     * Generates an ArrayList of size ELEMENTS_SIZE(100) containing 99 distinct random numbers and one fixed value LAST_VALUE(5001).
     * @return ArrayList<Integer> The generated elements array
     */
    private static ArrayList<Integer> generateElementsArray() {
        Random random = new Random();
        HashSet<Integer> uniqueValues = new HashSet<>();
        ArrayList<Integer> elementsArray = new ArrayList<>();

        // Generate 99 distinct random numbers
        while (uniqueValues.size() < ELEMENTS_SIZE - 1) {
            uniqueValues.add(random.nextInt(MAX_VALUE + 1));
        }

        // Add the fixed value LAST_VALUE as the 100th element
        uniqueValues.add(LAST_VALUE);

        // Add the values to the elements array
        elementsArray.addAll(uniqueValues);

        return elementsArray;
    }

    /**
     * Performs binary search on the given sorted array to find the specified element.
     * @param array The sorted array to search in
     * @param target The element to search for
     * @return boolean True if the element is found, false otherwise
     */
    private static boolean binarySearch(ArrayList<Integer> array, int target) {
        int left = 0;
        int right = array.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midValue = array.get(mid);

            if (midValue == target) {
                return true;
            }

            if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

    /**
     * Creates a hash table using chaining and stores the given search array in it.
     * @param searchArray The array to store in the hash table
     * @return HashTable The created hash table
     */
    private static HashTable createHashTable(ArrayList<Integer> searchArray) {
        HashTable hashTable = new HashTable(HASH_TABLE_SIZE);

        for (Integer value : searchArray) {
            hashTable.insert(value);
        }

        return hashTable;
    }

    /**
     * Searches the given hash table for the specified element.
     * @param hashTable The hash table to search in
     * @param target The element to search for
     * @return boolean True if the element is found, false otherwise
     */
    private static boolean hashTableSearch(HashTable hashTable, int target) {
        return hashTable.search(target);
    }

    /**
     * Custom implementation of a hash table using chaining.
     */
    private static class HashTable {
        private ArrayList<ArrayList<Integer>> table;
        private int size;

        public HashTable(int size) {
            this.size = size;
            table = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                table.add(new ArrayList<>());
            }
        }

        public void insert(int value) {
            int index = hash(value);
            table.get(index).add(value);
        }

        public boolean search(int value) {
            int index = hash(value);
            ArrayList<Integer> chain = table.get(index);

            for (Integer element : chain) {
                if (element == value) {
                    return true;
                }
            }

            return false;
        }

        private int hash(int x) {
            x = ((x >>> 16) ^ x) * 0x45d9f3b;
            x = ((x >>> 16) ^ x) * 0x45d9f3b;
            x = (x >>> 16) ^ x;
            return Math.abs(x) % size;
        }
    }
}