import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tools.CodeToFile;
import tools.Counter;
import tools.FileSelection;

public class App extends Application implements EventHandler<ActionEvent> {
    List<File> files;
    Button fileSelectionBtn;
    Button setFile;
    Button start;
    TextArea choosenFiles = new TextArea();
    TextArea pageFile = new TextArea();
    TextArea counting = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        fileSelectionBtn = new Button("소스 파일");
        fileSelectionBtn.setOnAction(this);
        fileSelectionBtn.setPrefSize(100, 30);

        choosenFiles.setPrefSize(400, 100);

        setFile = new Button("서식 파일");
        setFile.setOnAction(this);
        setFile.setPrefSize(100, 30);

        pageFile.setPrefSize(400, 100);

        start = new Button("시작");
        start.setOnAction(this);
        start.setPrefSize(100, 30);

        counting.setPrefSize(100, 30);

        hBox.getChildren().add(fileSelectionBtn);
        hBox.getChildren().add(choosenFiles);
        hBox.getChildren().add(setFile);
        hBox.getChildren().add(pageFile);
        hBox.getChildren().add(start);
        hBox.getChildren().addAll(counting);
        primaryStage.setScene(new Scene(hBox));
        primaryStage.show();
    }

    String fileName = "Result.hwp";
    String pageFilePath;

    @Override
    public void handle(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        if (event == fileSelectionBtn) {
            FileSelection fileSelection = new FileSelection();
            files = fileSelection.selectionFile(choosenFiles);
        }
        if (event == start) {
            try {
                FileSelection fileSelection = new FileSelection();
                String outputPath = fileSelection.selectionOuput();
                Counter c = new Counter();
                int tmp = 0;
                for (File i : files) {
                    CodeToFile codeToFile = new CodeToFile(i.toPath().toString(), true);
                    codeToFile.analysisDatas(tmp, files.size(), counting);
                    codeToFile.toFile(pageFilePath, outputPath, fileName);
                    c.count(++tmp, files.size(), counting);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event == setFile) {
            FileSelection fileSelection = new FileSelection();
            pageFilePath = fileSelection.selectPageFile(pageFile);
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
// 한글은 페이지 구분 기능이 없음
// 페이지 나누기 불가능
