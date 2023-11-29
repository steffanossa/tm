package tm.customcontrols;

import javafx.scene.control.TextArea;

public class CopyableTextArea extends TextArea{
    public CopyableTextArea() {
        super();
        setEditable(false);
        setWrapText(false);
    }
}
