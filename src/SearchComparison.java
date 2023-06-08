import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
            // Create a FileWriter to write the results to a csv file
            FileWriter resultsLinear = new FileWriter("resultsLinear.csv");
            FileWriter resultsBinary = new FileWriter("resultsBinary.csv");
            FileWriter resultsHashTable = new FileWriter("resultsHashTable.csv");
            resultsLinear.write("Size,Time\n");
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

                // Sequential Search
                sequentialSearch(searchArray, elementsArray);

                // Timer end
                long endTime = System.nanoTime();

                // Binary Search
                for (Integer element : elementsArray) {
                    binarySearch(searchArray, element);
                }

                long endTime2 = System.nanoTime();

                // Hash Table Search
                HashTable hashTable = createHashTable(searchArray);
                for (Integer element : elementsArray) {
                    hashTable.search(element);
                }

                long endTime3 = System.nanoTime();

                // Calculate total time
                long totalTimeLinear = endTime - startTime;
                long totalTimeBinary = endTime2 - endTime;
                long totalTimeHashTable = endTime3 - endTime2;

                // Writing results to file
                resultsLinear.write(n + "," + totalTimeLinear + "\n");
                resultsBinary.write(n + "," + totalTimeBinary + "\n");
                resultsHashTable.write(n + "," + totalTimeHashTable + "\n");

                // Memory management and garbage collection
                searchArray = null;
                hashTable = null;
                System.gc();
            }

            // Closing the files
            resultsLinear.close();
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

    // SEARCHING

    /**
     * Linear search implementation by iterating through each element of the search array and searching for elements from
     * the elements array in the search array.
     *
     * @param searchArray   the array to be searched
     * @param elementsArray the array containing elements to search in the search array
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

    /**
     * Searches for elements from the elements array in the provided hash table.
     *
     * @param elementsArray the array containing elements to search in the hash table
     * @param hashTable     the hash table to be searched
     */
    private static void searchHashTable(ArrayList<Integer> elementsArray, HashTable hashTable) {
        for (int element : elementsArray) {
            hashTable.search(element);
        }
    }

    // QUICK SORT. Source: D2L Code Sorting.java (By Dr. Jalal Kawash)

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
                    // Swap elements
                    Collections.swap(list, i, j);
                }
            }
        }
        if (high != i + 1) Collections.swap(list, high, i + 1);
        return i + 1;
    }

    /**
     * Custom implementation of a hash table using chaining.
     */
    private static class HashTable {
        private final int TABLE_SIZE = 9973;  // Prime number for the hash table size
        private ArrayList<LinkedList<Integer>> table;

        public HashTable() {
            table = new ArrayList<>(TABLE_SIZE);
            for (int i = 0; i < TABLE_SIZE; i++) {
                table.add(new LinkedList<>());
            }
        }

        /**
         * Inserts an element into the hash table.
         *
         * @param element the element to be inserted
         */
        public void insert(int element) {
            int hashValue = hash(element);
            LinkedList<Integer> list = table.get(hashValue);
            list.add(element);
        }

        /**
         * Searches for an element in the hash table.
         *
         * @param element the element to be searched
         * @return true if the element is found, false otherwise
         */
        public boolean search(int element) {
            int hashValue = hash(element);
            LinkedList<Integer> list = table.get(hashValue);
            return list.contains(element);
        }

        /**
         * Hash function to determine the index in the hash table for a given element.
         *
         * @param x the element to be hashed
         * @return the hash value (index) in the hash table
         */
        private int hash(int x) {
            x = ((x >>> 16) ^ x) * 0x45d9f3b; // >>> is unsigned right shift
            x = ((x >>> 16) ^ x) * 0x45d9f3b;
            x = (x >>> 16) ^ x;
            return Math.abs(x) % TABLE_SIZE;
        }
    }
}
