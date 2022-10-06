package tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class JavaEmulator {
    private String sourcePath, outputPath;

    public JavaEmulator(String sourcePath, String outputPath) {
        this.sourcePath = sourcePath;
        this.outputPath = outputPath;
    }

    public void run() {
        ArrayList<String> command = new ArrayList<String>();
        command.add("cmd.exe");
        command.add("/c");
        command.add("start");
        command.add("cmd.exe");
        command.add("/k");
        command.add("powershell");
        command.add("java " + sourcePath + " | tee " + outputPath);

        ExecutorService pool = Executors.newSingleThreadExecutor();

        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            Process p = pb.start();
            ProcessReadTask task = new ProcessReadTask(p.getInputStream());
            Future<List<String>> f = pool.submit(task);
            f.get();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private static class ProcessReadTask implements Callable<List<String>> {

        private InputStream inputStream;

        public ProcessReadTask(InputStream inputStream) {

            this.inputStream = inputStream;

        }

        @Override

        public List<String> call() {

            return new BufferedReader(new InputStreamReader(inputStream))

                    .lines()

                    .collect(Collectors.toList());

        }

    }
}