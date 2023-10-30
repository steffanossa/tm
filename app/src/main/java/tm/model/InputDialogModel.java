package tm.model;

public class InputDialogModel {
    public final String NAME_PATTERN = "^[A-Za-zÄÖÜäöüß\\-]+(?: [A-Za-zÄÖÜäöüß\\-]+)?$";
    public final String FHKENNUNG_PATTERN = "^[A-Za-z]{2}\\d{6}$";
    public final String MATRNR_PATTERN = "^\\d{6,7}$";
    private StudentDAO studentDAO;

    public InputDialogModel(
        StudentDAO studentDAO
    ) {
        this.studentDAO = studentDAO;
    }

    public boolean validateName(String name) {
        return name.matches(NAME_PATTERN);
    }

    public boolean validateFhKennung(String fhKennung) {
        return fhKennung.matches(FHKENNUNG_PATTERN);
    }

    public boolean validateMatrikelnummer(String matrikelnummer) {
        return matrikelnummer.matches(MATRNR_PATTERN);
    }

    public boolean validateAll(String firstname, String lastname, String fhKennung, String matrikelnummer) throws Exception {
        if (!validateName(firstname)){
            return false;
        }
        if (!validateName(lastname)) {
            return false;
        }
        if (!validateFhKennung(fhKennung)) {
            return false;
        }
        if (!validateMatrikelnummer(matrikelnummer)){
            return false;
        }
        return true;
    }

    public void addStudent(String firstname, String lastname, String fhKennung, int matrikelnummer) {
        Student student = new Student(matrikelnummer, firstname, lastname, fhKennung);
        studentDAO.addStudent(student);
    }
}