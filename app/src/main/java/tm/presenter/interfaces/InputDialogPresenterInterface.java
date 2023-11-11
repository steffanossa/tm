package tm.presenter.interfaces;

import tm.model.classes.Student;

public interface InputDialogPresenterInterface extends GenericPresenterInterface {
    void showAndWait();
    void hide();
    void showAndWaitWithData(Student student);
}
