package tm.presenter.interfaces;

import javafx.collections.ObservableList;
import tm.model.dtos.StudentDTO;

public interface MainPresenterInterface extends GenericPresenterInterface {
    ObservableList<StudentDTO> getStudentDTOs();
}
