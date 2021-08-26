package com.searcher.entities;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PotentialFile {

    private final String directory;

    public PotentialFile(String directory) {
        this.directory = directory;
    }

    public List<PotentialFile> getChildren() {
        if (!this.isRegularFile()) {
            File f = new File(this.getDirectory());
            List<String> files = Arrays.asList(Objects.requireNonNull(f.list()));
            List<PotentialFile> filesAbsoluteDirectory = files.stream()
                    .map(dir -> new PotentialFile(this.getDirectory() + "\\" + dir))
                    .collect(Collectors.toList());
            return filesAbsoluteDirectory;
        } else {
            return new ArrayList<>();
        }
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
