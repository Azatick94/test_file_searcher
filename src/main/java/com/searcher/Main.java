package com.searcher;

import com.searcher.entities.PotentialFile;
import com.searcher.threads.MyThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Predicate;

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

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        // env preparation
        prepareExampleDirectory();

        // realization
        filesFromWalk = new ArrayList<>();
        queue = new LinkedList<>();
        queue.add(homeDirectory);

        // №1 - Multithreading using ExecutorService
        runThreadsUsingThreadPool();

        // ForkJoinPool Approach
        PotentialFile rootPotentialFile = new PotentialFile(homeDirectory);
        // №2 - ForkJoinPool via Task
        // filesFromWalk = new ForkJoinPool().invoke(new FilePatternSearcherTask(rootPotentialFile));
        // №3 - ForkJoinPool via Task and using Functional Interface
        // filesFromWalk = new ForkJoinPool().invoke(new FilePatternSearcherWithFunctionalInterfaceTask(rootPotentialFile));

        System.out.println("List of files matching pattern: ");
        printBeautifiedList(filesFromWalk);
    }

    public static void runThreadsUsingThreadPool() throws InterruptedException, ExecutionException, TimeoutException {
        int numberOfThreads = 3;
        List<Future<?>> futureTasks = new ArrayList<>();

        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new MyThread());
            thread.setName("thread number " + (i + 1));
            Future<?> futureResult = service.submit(thread);
            Thread.sleep(200);
            futureTasks.add(futureResult);
        }

        for (Future<?> task: futureTasks) {
            task.get(10, TimeUnit.SECONDS); // wait the end of each task
        }
        service.shutdown();
    }

    // --------------------------------
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
