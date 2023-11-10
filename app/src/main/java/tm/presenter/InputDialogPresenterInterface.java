package tm.presenter;

import tm.model.Student;

public interface InputDialogPresenterInterface extends GenericPresenter {
    void showAndWait();
    void hide();
    void showAndWaitWithData(Student student);
}
