package com.searcher;

import com.searcher.entities.PotentialFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import static com.searcher.Main.patternToFindInFile;

public class FilePatternSearcherFunctionalTask extends ForkJoinTask<List<String>> {

    List<String> result;
    private final PotentialFile pFile;

    public FilePatternSearcherFunctionalTask(PotentialFile pFile) {
        this.pFile = pFile;
    }

    @Override
    public List<String> getRawResult() {
        return result;
    }

    @Override
    protected void setRawResult(List<String> value) {
        result = value;
    }

    @Override
    protected boolean exec() {

        ForkJoinCompute<String> fj = () -> {

            List<String> filesFromWalk = new ArrayList<>();
            if (pFile.isRegularFile() && pFile.containsPattern(patternToFindInFile)) {
                filesFromWalk.add(pFile.getDirectory());
            }

            List<FilePatternSearcherFunctionalTask> subTasks = new LinkedList<>();
            for (PotentialFile child : pFile.getChildren()) {
                FilePatternSearcherFunctionalTask task = new FilePatternSearcherFunctionalTask(child);
                // asynchronous start of job
                task.fork();
                subTasks.add(task);
            }

            for (FilePatternSearcherFunctionalTask task : subTasks) {
                // wait end of task
                filesFromWalk.addAll(task.join());
            }
            return filesFromWalk;
        };

        result = fj.compute();
        return true;
    }
}


