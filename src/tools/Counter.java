package tools;

import javafx.scene.control.TextArea;

public class Counter {
    public void count(int done, int all, TextArea ta) {
        ta.clear();
        if (done == all) {
            ta.appendText("DONE");
        }
    }
}
