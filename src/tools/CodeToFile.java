package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javafx.scene.control.TextArea;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.blankfilemaker.BlankFileMaker;
import kr.dogfoot.hwplib.writer.HWPWriter;

public class CodeToFile {
    private static int number = 0;
    private StringBuilder code = new StringBuilder();
    private StringBuilder algorithm = new StringBuilder();
    private StringBuilder result = new StringBuilder();
    private BufferedReader file;
    private String recognizer = "//";
    private boolean autoNumbering = true;
    private String outputPath = "..\\temp.txt";
    private String inputPath;
    // private Counter c = new Counter();

    public CodeToFile(String inputPath, boolean autoNumbering) throws IOException {
        try {
            file = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            this.autoNumbering = autoNumbering;
            this.inputPath = inputPath;
        } catch (FileNotFoundException e) {
            System.out.println("Can't find file at this path");
        }
    }

    public void analysisDatas(int num, int all, TextArea ta) throws IOException, InterruptedException {
        String line = file.readLine();
        int count = 0, index;
        while (line != null) {
            code.append(line + "\n");
            if (line.contains(recognizer)) {
                index = line.indexOf(recognizer) + 2;
                if (autoNumbering) {
                    algorithm.append(++count + ". " + line.substring(index, line.length()) + "\n");
                } else {
                    algorithm.append(line.substring(index, line.length()) + "\n");
                }
            }
            line = file.readLine();
        }
        runJava(this.inputPath);
        BufferedReader output = new BufferedReader(new InputStreamReader(new FileInputStream(outputPath), "UTF-16"));
        while ((line = output.readLine()) != null) {
            result.append(line + "\n");
        }
        output.close();
        PrintWriter pw = new PrintWriter(outputPath);
        pw.close();
        // c.count(++num, all, ta);
    }

    public void toFile(String pagePath, String outputpath, String fileName) throws Exception {
        HwpControl hwpControl = new HwpControl(pagePath);
        HWPFile target;
        hwpControl.setFieldText("number", String.valueOf(++number));
        hwpControl.setFieldText("algorithm", algorithm.toString());
        hwpControl.setFieldText("code", code.toString());
        hwpControl.setFieldText("result", result.toString());
        // hwpControl.setCellTextByField("result", fileName);
        try {
            target = HWPReader.fromFile(outputpath + "/" + fileName);
        } catch (Exception e) {
            target = BlankFileMaker.make();
        }
        hwpControl.addHWPFile(target);
        HWPWriter.toFile(target, outputpath + "/" + fileName);
    }

    public void runJava(String sourcePath) throws IOException, InterruptedException {
        JavaEmulator javaEmulator = new JavaEmulator(sourcePath, outputPath);
        javaEmulator.run();
        // System.out.println("after join");
    }

    public void setRecognizer(String recognizer) {
        this.recognizer = recognizer;
    }
}
