package tm.presenter.interfaces;

import tm.model.dtos.StudentDTO;

public interface InputDialogPresenterInterface extends GenericPresenterInterface {
    //void showAndWait();
    boolean showAndWait();
    void hide();
    void showAndWaitWithData(StudentDTO student);
}
