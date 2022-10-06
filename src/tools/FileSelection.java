package tools;

import java.io.File;
import java.util.List;

import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileSelection {
    private Stage primaryStage;

    public List<File> selectionFile(TextArea ta) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("자바 소스 파일 : Java Source File", "*.java"));
        List<File> file = fileChooser.showOpenMultipleDialog(primaryStage);
        ta.clear();
        for (File i : file) {
            ta.appendText(i.toPath() + "\n");
        }
        return file;
    }

    public String selectPageFile(TextArea ta) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Hwp file", "*.hwp"));
        File page = fileChooser.showOpenDialog(primaryStage);
        ta.clear();
        ta.appendText(page.toPath().toString());
        return page.toPath().toString();
    }

    public String selectionOuput() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File dir = dirChooser.showDialog(primaryStage);
        return dir.getPath();
    }
}
