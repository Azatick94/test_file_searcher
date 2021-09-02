package com.searcher;

import com.searcher.entities.PotentialFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import static com.searcher.Main.patternToFindInFile;

public class FilePatternSearcherTask extends RecursiveTask<List<String>> {

    private final PotentialFile pFile;

    public FilePatternSearcherTask(PotentialFile pFile) {
        this.pFile = pFile;
    }

    @Override
    protected List<String> compute() {

        List<String> filesFromWalk = new ArrayList<>();
        if (pFile.isRegularFile() && pFile.containsPattern(patternToFindInFile)) {
            filesFromWalk.add(pFile.getDirectory());
        }

        List<FilePatternSearcherTask> subTasks = new LinkedList<>();
        for (PotentialFile child : pFile.getChildren()) {
            FilePatternSearcherTask task = new FilePatternSearcherTask(child);
            task.fork(); // asynchronous start of job
            subTasks.add(task);
        }

        for (FilePatternSearcherTask task : subTasks) {
            filesFromWalk.addAll(task.join()); // wait end of task
        }

        return filesFromWalk;
    }
}
