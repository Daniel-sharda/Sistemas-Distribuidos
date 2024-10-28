package es.uva.hilos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TextVowelCounter {

    // Method that takes a String input and returns an ArrayList of words
    public static ArrayList<String> getWords(String input) {
        ArrayList<String> palabras = new ArrayList<>(Arrays.asList(input.split(" ")));

        return palabras;
    }

    // Method that counts vowels in a string using parallelism
    public static int getVowels(String input, int parallelism) throws InterruptedException {

        // Create queues
        BlockingQueue<String> wordQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Result> resultQueue = new LinkedBlockingQueue<>();

        ArrayList<String> palabras = getWords(input);
        for(String palabra : palabras) {
            wordQueue.put(palabra);
        }

        // Create and start the worker threads based on the parallelism parameter
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < parallelism; i++) {
            WordVowelCounter worker = new WordVowelCounter(wordQueue, resultQueue);
            Thread workerThread = new Thread(worker);
            workers.add(workerThread);
            workerThread.start();  // Start each worker thread
        }

        // Wait for all worker threads to finish
        for (Thread worker : workers) {
            worker.join();
        }

        // Gather results from resultQueue
        // TODO
        int total=0;
        for(Result resultado : resultQueue) {
            total+=resultado.getVowelCount();
        }
        return total;
    }
}
