package com.searcher;

import com.searcher.entities.PotentialFile;
import com.searcher.threads.MyBlockingQueueThread;
import com.searcher.threads.MyThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Resources
 * https://stackabuse.com/java-list-files-in-a-directory/
 */
public class Main {
    // bashExecutor should be specified
    private static final String bashExecutor = "C:/Program Files/Git/bin/bash.exe";
    private static final String homeDirectory = System.getProperty("user.home") + "\\" + "test_directory";

    public static final String patternToFindInFile = "text";
    public static volatile List<String> filesFromWalk;
    public static volatile Queue<String> queue = new LinkedList<>();
    public static volatile BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
    public static volatile boolean exit = false;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        // env preparation
        prepareExampleDirectory();
        filesFromWalk = new ArrayList<>();

        // realization
        queue = new LinkedList<>();
        queue.add(homeDirectory);

        int capacity = 10;
        blockingQueue = new LinkedBlockingQueue<>(capacity);
        blockingQueue.add(homeDirectory);


        // №1 - Multithreading using ExecutorService
        // runThreadsUsingThreadPool(3,2);

        // №2 - Multithreading using ExecutorService with BlockingQueue
        // runThreadUsingThreadPoolWithBlockingQueue(3, 2);

        // ForkJoinPool Approach
        // №3 - ForkJoinPool via Task
        // runForkJoinPoolUsingTask();
        // №4 - ForkJoinPool via Task and using Functional Interface
        // runForkJoinPoolWithFunctionalInterfaceTask();

    }

    public static void runThreadsUsingThreadPool(int numberOfThreads, int timeToRunInSeconds) throws InterruptedException, ExecutionException, TimeoutException {
        filesFromWalk = new ArrayList<>();

        List<Future<?>> futureTasks = new ArrayList<>();
        List<Thread> lstThreads = new ArrayList<>();

        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new MyThread());
            thread.setName("thread number " + (i + 1));
            Future<?> futureResult = service.submit(thread);
            Thread.sleep(200);
            lstThreads.add(thread);
            futureTasks.add(futureResult);
        }

        for (Future<?> task : futureTasks) {
            task.get(timeToRunInSeconds, TimeUnit.SECONDS); // wait the end of each task no more than 10 seconds
        }

        service.shutdown();
        printBeautifiedList(filesFromWalk);
    }

    public static void runThreadUsingThreadPoolWithBlockingQueue(int numberOfThreads, int timeToRunInSeconds) throws InterruptedException {
        filesFromWalk = new ArrayList<>();

        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new MyBlockingQueueThread(blockingQueue));
            thread.setName("thread number " + (i + 1));
            service.submit(thread);
        }
        Thread.sleep(timeToRunInSeconds * 1000L);
        exit = true;
        service.shutdown();

        Thread.sleep(200);
        printBeautifiedList(filesFromWalk);
    }

    public static void runForkJoinPoolUsingTask() {
        PotentialFile rootPotentialFile = new PotentialFile(homeDirectory);
        filesFromWalk = new ForkJoinPool().invoke(new FilePatternSearcherTask(rootPotentialFile));
        printBeautifiedList(filesFromWalk);
    }

    public static void runForkJoinPoolWithFunctionalInterfaceTask() {
        PotentialFile rootPotentialFile = new PotentialFile(homeDirectory);
        filesFromWalk = new ForkJoinPool().invoke(new FilePatternSearcherWithFunctionalInterfaceTask(rootPotentialFile));
        printBeautifiedList(filesFromWalk);
    }

    // --------------------------------
    private static void printBeautifiedList(List<String> lst) {
        System.out.println("\nList of files matching pattern: ");
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
