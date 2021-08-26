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

public class PotentialFileProcessingThread implements Runnable {

    private final PotentialFile pFile;

    public PotentialFileProcessingThread(PotentialFile pFile) {
        this.pFile = pFile;
    }

    @Override
    public void run() {
        if (!pFile.isRegularFile()) {
            File f = new File(pFile.getDirectory());
            List<String> files = Arrays.asList(Objects.requireNonNull(f.list()));
            List<String> filesAbsoluteDirectory = files.stream()
                    .map(dir -> pFile.getDirectory() + "\\" + dir)
                    .collect(Collectors.toList());
            queue.addAll(filesAbsoluteDirectory);
        }
        if (pFile.isRegularFile() & pFile.containsPattern(patternToFindInFile)) {
            filesFromWalk.add(pFile.getDirectory());
        }
    }
}
