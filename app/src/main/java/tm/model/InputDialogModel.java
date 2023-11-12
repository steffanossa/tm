package tm.model;

import java.sql.SQLException;

import tm.model.classes.Student;
import tm.model.daos.StudentDAO;

public class InputDialogModel {
    
    private StudentDAO studentDAO;

    public InputDialogModel( StudentDAO studentDAO ) { this.studentDAO = studentDAO; }

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