package tm.presenter.interfaces;

import tm.model.dtos.StudentDTO;

public interface InputDialogPresenterInterface extends GenericPresenterInterface {
    void showAndWait();
    void hide();
    void showAndWaitWithData(StudentDTO student);
    //boolean showAndWaitWithData(StudentDTO student);
}
