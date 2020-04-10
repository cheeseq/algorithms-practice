import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int stringReaded = 0;
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (stringReaded < k) {
            randomizedQueue.enqueue(StdIn.readString());
            stringReaded++;
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}
