package com.searcher;

import com.searcher.entities.PotentialFile;
import com.searcher.util.ForkJoinCompute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import static com.searcher.Main.patternToFindInFile;

public class FilePatternSearcherWithFunctionalInterfaceTask extends ForkJoinTask<List<String>> {

    List<String> result;
    private final PotentialFile pFile;

    public FilePatternSearcherWithFunctionalInterfaceTask(PotentialFile pFile) {
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

        };

        result = fj.compute();
        return true;
    }
}


