package com.searcher.threads;

import com.searcher.entities.PotentialFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.searcher.Main.filesFromWalk;
import static com.searcher.Main.patternToFindInFile;
import static com.searcher.Main.queue;

public class MyThread implements Runnable {
    @Override
    public void run() {
        System.out.println("STARTING thread number " + Thread.currentThread().getName() + "!!! \n");

        while (!queue.isEmpty()) {

            String directory = queue.poll();
            PotentialFile pFile = new PotentialFile(directory);

            System.out.println("Thread " + Thread.currentThread().getName() + " processing directory: " + directory);
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " is sleeping" + "\n");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!pFile.isRegularFile()) {
                File f = new File(pFile.getDirectory());
                List<String> files = Arrays.asList(Objects.requireNonNull(f.list()));
                List<String> filesAbsoluteDirectory = files.stream()
                        .map(dir -> pFile.getDirectory() + "\\" + dir)
                        .collect(Collectors.toList());
                queue.addAll(filesAbsoluteDirectory);
            }
            if (pFile.isRegularFile() & pFile.containsPattern(patternToFindInFile)) {
                filesFromWalk.add(directory);
            }
        }
        System.out.println("FINISHED Thread " + Thread.currentThread().getName() + " has done job !!! \n");
    }
}
