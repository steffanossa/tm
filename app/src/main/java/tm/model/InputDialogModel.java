package tm.model;

import java.sql.SQLException;

import tm.model.classes.Student;
import tm.model.daos.StudentDAO;

public class InputDialogModel {
    
    private StudentDAO studentDAO;

    public InputDialogModel(
        StudentDAO studentDAO
    ) {
        this.studentDAO = studentDAO;
    }

    public boolean addStudent(
        String firstname,
        String lastname,
        String fhKennung,
        int matrikelnummer
    )
    throws SQLException
    {
        Student student = new Student(matrikelnummer, firstname, lastname, fhKennung);
        
        return studentDAO.add(student);
    }
}