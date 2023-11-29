package tm.model;

import java.sql.SQLException;

import tm.model.daos.GenericDAO;
import tm.model.dtos.StudentDTO;

public class InputDialogModel {
    
    private GenericDAO<StudentDTO> studentDAO;

    public InputDialogModel( GenericDAO<StudentDTO> studentDAO ) { this.studentDAO = studentDAO; }

    /**
     * Adds a Student to the database
     * @param firstName
     * @param surname
     * @param fhIdentifier
     * @param matriculationNumber
     * @return {@code true} if successful
     * @throws SQLException
     */
    public boolean addStudent(
        String firstName,
        String surname,
        int matriculationNumber,
        String fhIdentifier
        ) throws SQLException
    {
        StudentDTO student = new StudentDTO(firstName, surname, matriculationNumber, fhIdentifier);
        
        return studentDAO.add(student);
    }
}