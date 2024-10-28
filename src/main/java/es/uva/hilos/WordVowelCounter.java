package es.uva.hilos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class WordVowelCounter implements Runnable {
    private BlockingQueue<String> wordQueue;
    private BlockingQueue<Result> resultQueue;

    // Constructor
    public WordVowelCounter(BlockingQueue<String> wordQueue, BlockingQueue<Result> resultQueue) {
        this.wordQueue = wordQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        // TODO
        String palabra;
        int numero=0;
        boolean flag=true;
        try {
            while(!wordQueue.isEmpty()) {
                palabra=wordQueue.take();
                numero=countVowels(palabra);
                try {
                    resultQueue.put(new Result(palabra, numero));
                } catch (InterruptedException e) {}
                flag=true;
            }
        } catch (InterruptedException e) {}
    }

    private int countVowels(String word) {
        int contador=0;
        char letra;
        for(int i=0; i<word.length(); i++) {
            letra=Character.toLowerCase(word.charAt(i));
            if(letra=='a' || letra=='e' || letra=='i' || letra=='o' || letra=='u') {
                contador++;
            }
        }
        return contador;
    }
}

