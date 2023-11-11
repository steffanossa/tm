package tm.customcontrols;

import javafx.scene.control.TextField;
/**
 * TextField extended by the option of specifying a regex pattern (as String),
 * so that the 'validate()' method (also new) can be used to check whether
 * the input text fits the pattern.
 */
public class PatternTextField extends TextField {
    private String pattern;

    public PatternTextField(String pattern)
    {
        super();
        this.pattern = pattern;
    }

    public boolean validate()
    {
        if (pattern == null)
            throw new IllegalStateException("Pattern not set.");
        return this.getText().matches(pattern);
    }
}
