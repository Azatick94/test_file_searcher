package com.searcher.threads;

import com.searcher.Main;
import com.searcher.entities.PotentialFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import static com.searcher.Main.filesFromWalk;
import static com.searcher.Main.patternToFindInFile;

public class MyBlockingQueueThread implements Runnable {

    private BlockingQueue<String> blockingQueue;

    public MyBlockingQueueThread(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        System.out.println("STARTING thread number " + Thread.currentThread().getName() + "!!!");

        while (!Main.exit) {

            String directory = blockingQueue.poll(); // how to do with take???
            if (directory != null) {
                PotentialFile pFile = new PotentialFile(directory);
                System.out.println("Thread " + Thread.currentThread().getName() + " processing directory: " + directory);

                if (!pFile.isRegularFile()) {
                    File f = new File(pFile.getDirectory());
                    List<String> files = Arrays.asList(Objects.requireNonNull(f.list()));
                    List<String> filesAbsoluteDirectory = files.stream()
                            .map(dir -> pFile.getDirectory() + "\\" + dir)
                            .collect(Collectors.toList());
                    blockingQueue.addAll(filesAbsoluteDirectory);
                }
                if (pFile.isRegularFile() & pFile.containsPattern(patternToFindInFile)) {
                    filesFromWalk.add(directory);
                }
            }
        }
        System.out.println("FINISHED Thread " + Thread.currentThread().getName() + " has done job !!!");
    }
}
