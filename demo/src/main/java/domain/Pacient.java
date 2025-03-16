// pacient.java
package domain;

public class Pacient extends Entity {
    private String nume;
    private String prenume;
    private int varsta;

    public Pacient(int id, String nume, String prenume, int varsta) {
        super(id); // folosim ID-ul din Entity
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }

    // shortcut ALT + INSERT !!!!
    // getteri
    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public int getVarsta() {
        return varsta;
    }

    // setteri
    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    // subscriem metoda toString() pentru clasa Pacient
    @Override
    public String toString() {
        return "==============================\n" +
                "Pacient Detalii:\n" +
                "------------------------------\n" +
                "ID Pacient: " + id + "\n" +
                "Nume: " + nume + "\n" +
                "Prenume: " + prenume + "\n" +
                "Varsta: " + varsta + "\n" +
                "------------------------------\n" +
                "==============================";
    }

}