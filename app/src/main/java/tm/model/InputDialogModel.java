package tm.model;

import java.sql.SQLException;

import tm.model.classes.Student;
import tm.model.daos.GenericDAO;

public class InputDialogModel {
    
    private GenericDAO<Student> studentDAO;

    public InputDialogModel( GenericDAO<Student> studentDAO ) { this.studentDAO = studentDAO; }

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
        String fhIdentifier,
        int matriculationNumber) throws SQLException
    {
        Student student = new Student(firstName, surname, matriculationNumber, fhIdentifier);
        
        return studentDAO.add(student);
    }
}