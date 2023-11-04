package tm.presenter;

import tm.model.Student;

public interface InputDialogPresenterInterface {
    void showAndWait();
    void hide();
    void showAndWaitWithData(Student student);
}
