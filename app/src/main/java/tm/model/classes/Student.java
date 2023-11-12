package tm.model.classes;

public class Student {

    private String firstName;
    private String surname;
    private int matriculationNumber;
    private String fhIdentifier;

    public Student(
        String firstName,
        String surname,
        int matriculationNumber,
        String fhIdentifier)
    {
        this.firstName = firstName;
        this.surname = surname;
        this.matriculationNumber = matriculationNumber;
        this.fhIdentifier = fhIdentifier;
    }

    public int getMatriculationNumber()
    {
        return matriculationNumber;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getFhIdentifier()
    {
        return fhIdentifier;
    }

    public void setFirstName(String firstname)
    {
        this.firstName = firstname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setFhIdentifier(String fhKennung)
    {
        this.fhIdentifier = fhKennung;
    }

    @Override
    public String toString()
    {
        return fhIdentifier;
    }
}

