package tm.model.enums;

/**
 * PNG images with transparent backgrounds to be used with alerts, dialogs, ...
 */
public enum PattyImages {
    CONFIRMATION("/images/Confirmation.png"),
    CONFIRMATION_256("/images/Confirmation256.png"),
    ERROR("/images/Error.png"),
    ERROR_256("/images/Error256.png"),
    GRUMMEL_256("/images/Grummel256.png"),
    INFO("/images/Info.png"),
    INFO_256("/images/Info256.png"),
    LOGO("/images/Logo.png"),
    LOGO_256("/images/Logo256.png"),
    QUESTION_256("/images/Question256.png"),
    STOP_256("/images/Stop256.png"),
    SUGGESTION_256("/images/Suggestion256.png");

    private final String path;

    PattyImages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
