package tm.model;

public class Student {

    private int matrikelnummer;
    private String firstname;
    private String surname;
    private String fhKennung;

    public Student(
        int matrikelnummer,
        String firstname,
        String surname,
        String fhKennung)
    {
        this.matrikelnummer = matrikelnummer;
        this.firstname = firstname;
        this.surname = surname;
        this.fhKennung = fhKennung;
    }

    public int getMatrikelnummer()
    {
        return matrikelnummer;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getFhKennung()
    {
        return fhKennung;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setFhKennung(String fhKennung)
    {
        this.fhKennung = fhKennung;
    }
}

