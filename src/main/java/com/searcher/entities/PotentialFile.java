package com.searcher.entities;

import java.nio.file.Files;
import java.nio.file.Path;

public class PotentialFile {

    private final String directory;

    public PotentialFile(String directory) {
        this.directory = directory;
    }

    public boolean isRegularFile() {
        return Files.isRegularFile(Path.of(directory));
    }

    public boolean containsPattern(String pattern) {
        return directory.contains(pattern);
    }

    public String getDirectory() {
        return directory;
    }

}
