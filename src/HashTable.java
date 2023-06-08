import java.util.ArrayList;
import java.util.LinkedList;

public class HashTable {
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
