package com.searcher.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.searcher.Main.patternToFindInFile;

public class MainUtil {

    private static List<String> filesFromWalk;

    //     1) simple approach to find files in directory with recursion, before Java 8
    public static List<String> fileListApproach(String directory) {

        File f = new File(directory);
        List<String> files = Arrays.asList(Objects.requireNonNull(f.list()));
        List<String> filesAbsoluteDirectory = files.stream()
                .map(dir -> directory + "\\" + dir)
                .collect(Collectors.toList());
        for (String subdir : filesAbsoluteDirectory) {
            if (Files.isRegularFile(Path.of(subdir))) {
                if (subdir.contains(patternToFindInFile)) {
                    filesFromWalk.add(subdir);
                }
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
                    .map(Path::toString)
                    .filter(dir -> dir.contains(patternToFindInFile))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
