//package com.searcher;
//
//import com.searcher.entities.PotentialFile;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.RecursiveAction;
//
//import static com.searcher.Main.filesFromWalk;
//import static com.searcher.Main.patternToFindInFile;
//
//public class FilePatternSearcherAction extends RecursiveAction {
//
//    private final PotentialFile pFile;
//
//    public FilePatternSearcherAction(PotentialFile pFile) {
//        this.pFile = pFile;
//    }
//
//    @Override
//    protected void compute() {
//
//        if (pFile.isRegularFile() && pFile.containsPattern(patternToFindInFile)) {
//            filesFromWalk.add(pFile.getDirectory());
//        }
//
//        List<FilePatternSearcherTask> subTasks = new LinkedList<>();
//        for (PotentialFile child : pFile.getChildren()) {
//            FilePatternSearcherTask task = new FilePatternSearcherTask(child);
//            task.fork(); // asynchronous start of job
//            subTasks.add(task);
//        }
//
//        for (FilePatternSearcherTask task : subTasks) {
//            task.join(); // wait end of task
//        }
//    }
//}
