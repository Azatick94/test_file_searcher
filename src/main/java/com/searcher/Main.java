package com.searcher;

import com.searcher.entities.PotentialFile;
import com.searcher.mbean.ThreadController;

import javax.management.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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

    // TODO
    public static volatile boolean exit = false;

    public static void main(String[] args) throws IOException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {

        // --------------------------------
        // env preparation
        prepareExampleDirectory();
        filesFromWalk = new ArrayList<>();

        // prepare MBeans
        prepareMBeans();

        // ForkJoinPool via Task and using Functional Interface
        runForkJoinPoolWithFunctionalInterfaceTask();
    }

    public static void runForkJoinPoolWithFunctionalInterfaceTask() {
        PotentialFile rootPotentialFile = new PotentialFile(homeDirectory);
        filesFromWalk = new ForkJoinPool().invoke(new FilePatternSearcherFunctionalTask(rootPotentialFile));
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

    private static void prepareMBeans() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("com.searcher.mbean:type=ThreadController");
        ThreadController threadController = new ThreadController();
        mbs.registerMBean(threadController, name);
    }
}
