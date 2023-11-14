package tm.customcontrols;

import java.util.regex.Pattern;

import javafx.scene.control.TextField;
/**
 * TextField extended by the option of specifying a regex pattern (as String),
 * so that the 'validate()' method (also new) can be used to check whether
 * the input text fits the pattern.
 * 
 * @see TextField
 */
public class PatternTextField extends TextField {
    // private Pattern pattern;
    private String pattern;

    public PatternTextField(String pattern)
    {
        super();
        this.pattern = pattern;
    }

    /**
     * Tells whether or not current text matches pattern assigned to this PatternTextField
     * @return {@code true} if current text matches assigned pattern
     * @throws IllegalStateException
     */
    public boolean validate()
    {
        if (pattern == null)
            throw new IllegalStateException("Pattern not set.");
        return this.getText().matches(pattern);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return this.pattern;
    }
}
