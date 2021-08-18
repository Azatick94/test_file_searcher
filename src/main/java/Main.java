package main.java;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Resources
 * https://stackabuse.com/java-list-files-in-a-directory/
 */
public class Main {
    private static List<String> filesFromWalk;

    public static void main(String[] args) throws IOException {

        // bashExecutor should be specified
        String bashExecutor = "C:/Program Files/Git/bin/bash.exe";

        // bash script running
        String script = System.getProperty("user.dir");
        String scriptDir = script + "/src/main/resources/bash/example-test-folder.sh";
        String[] cmd = new String[]{bashExecutor, scriptDir};
        Process proc = Runtime.getRuntime().exec(cmd);
        readBashRunResult(proc);

        String homeDirectory = System.getProperty("user.home");
        homeDirectory = homeDirectory + "\\" + "test_directory";
        System.out.println("\n");

        // running different approaches
        System.out.println("1) simple approach using File.list():");
        filesFromWalk = new ArrayList<>();
        printBeautifiedList(fileListApproach(homeDirectory));

        System.out.println("2) search using Java 8 Stream API and Files.walk:");
        printBeautifiedList(Objects.requireNonNull(filesWalkStreamApiApproach(homeDirectory)));

    }

    // 1) simple approach to find files in directory with recursion, before Java 8
    public static List<String> fileListApproach(String directory) {

        File f = new File(directory);
        List<String> files = Arrays.asList(Objects.requireNonNull(f.list()));
        List<String> filesAbsoluteDirectory = files.stream()
                .map(dir -> directory + "\\" + dir)
                .collect(Collectors.toList());
        for (String subdir : filesAbsoluteDirectory) {
            if (Files.isRegularFile(Path.of(subdir))) {
                filesFromWalk.add(subdir);
            } else {
                fileListApproach(subdir);
            }
        }

        return filesFromWalk;
    }

    // 2) search using Java 8 Stream API and Files.walk
    public static List<String> filesWalkStreamApiApproach(String directory) {
        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
            // We want to find only regular files
            return walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void printBeautifiedList(List<String> lst) {
        System.out.println("-".repeat(50));
        for (String item : lst) {
            System.out.println(item);
        }
        System.out.println("-".repeat(50));
    }

    private static void readBashRunResult(Process process) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

    }


}
