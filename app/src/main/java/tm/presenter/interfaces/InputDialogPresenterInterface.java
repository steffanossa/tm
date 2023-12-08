package tm.presenter.interfaces;

import tm.model.dtos.StudentDTO;

public interface InputDialogPresenterInterface extends GenericPresenterInterface {
    boolean showAndWait();
    void hide();
    void showAndWaitWithData(StudentDTO student);
}
