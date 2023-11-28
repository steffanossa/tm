package tm.presenter;

import javafx.collections.ObservableList;
import tm.model.dtos.StudentDTO;
import tm.presenter.interfaces.GenericPresenterInterface;

public interface MainPresenterInterface extends GenericPresenterInterface {
    ObservableList<StudentDTO> getStudentDTOs();
}
