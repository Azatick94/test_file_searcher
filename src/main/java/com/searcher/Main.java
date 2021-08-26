package com.searcher;

import com.searcher.threads.MyThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Resources
 * https://stackabuse.com/java-list-files-in-a-directory/
 */
public class Main {
    // bashExecutor should be specified
    private static final String bashExecutor = "C:/Program Files/Git/bin/bash.exe";

    public static final String patternToFindInFile = "text";
    public static volatile List<String> filesFromWalk;
    public static volatile Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        // directory initialization using bash script
        prepareExampleDirectory();
        // directory where to test
        String homeDirectory = System.getProperty("user.home") + "\\" + "test_directory";
        System.out.println("\n");

        // realization
        filesFromWalk = new ArrayList<>();
        queue = new LinkedList<>();
        queue.add(homeDirectory);

        runThreadsSimpleApproach();
        // runThreadsUsingThreadPool();

        System.out.println("List of files matching pattern: ");
        printBeautifiedList(filesFromWalk);
    }

    public static void runThreadsSimpleApproach() throws InterruptedException {
        Thread thread1 = new Thread(new MyThread());
        thread1.setName("№1");
        Thread thread2 = new Thread(new MyThread());
        thread2.setName("№2");
        Thread thread3 = new Thread(new MyThread());
        thread3.setName("№3");

        thread1.start();
        Thread.sleep(200);
        thread2.start();
        Thread.sleep(200);
        thread3.start();

        // waiting when job is done
        thread1.join();
        thread2.join();
        thread3.join();
    }

    public static void runThreadsUsingThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new MyThread());
        Thread.sleep(200);
        executor.submit(new MyThread());
        Thread.sleep(200);
        executor.submit(new MyThread());
    }

    private static void printBeautifiedList(List<String> lst) {
        System.out.println("-".repeat(50));
        for (String item : lst) {
            System.out.println(item);
        }
        System.out.println("-".repeat(50));
    }

    private static void prepareExampleDirectory() throws IOException {
        // bash script running
        String script = System.getProperty("user.dir");
        String scriptDir = script + "/src/main/resources/bash/example-test-folder.sh";
        String[] cmd = new String[]{bashExecutor, scriptDir};
        Process proc = Runtime.getRuntime().exec(cmd);
        readBashRunResult(proc);
    }

    private static void readBashRunResult(Process process) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

    }

}
