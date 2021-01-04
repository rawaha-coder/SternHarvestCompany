package harvest.util;

import javafx.scene.control.TextField;

public class TimeTextField extends TextField
{
    public TimeTextField(String init) {
        super(init) ;
        focusedProperty().addListener((o, oldV, newV) -> changed(newV));
    }

    public TimeTextField() {
        this("00:00:00");
    }

    private void changed(boolean focus) {
        if (!focus) {
            if (!validate()) {
                setText("00:00:00");
                selectAll();
                requestFocus();
                AlertMaker alertMaker = new AlertMaker();
                alertMaker.missingInfo("Time");
            }
        }
    }

    public boolean validate()  {
        return getText()
                .matches("(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])$");
    }
}
