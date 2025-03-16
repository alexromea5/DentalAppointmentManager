// programare.java

package domain;

import java.util.Calendar;
import java.util.Date;

public class Programare  extends Entity {
    private Pacient pacient;
    private Date data;
    private String scop;

    public Programare(int id, Pacient pacient, Date data, String scop) {
        super(id);
        this.pacient = pacient;
        this.data = data;
        this.scop = scop;
    }

    // getteri
    public Pacient getPacient() {
        return pacient;
    }

    public Date getData() {
        return data;
    }

    public String getScop() {
        return scop;
    }

    // setteri
    public void setPacient(Pacient pacient) {
        this.pacient = pacient;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setScop(String scop) {
        this.scop = scop;
    }

    // functie pentru a afla ora de sfarsit a unei programari
    public Date getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.MINUTE, 60); // fiecare programare dureaza 60 de minute
        return calendar.getTime();
    }

    @Override
    public String toString() {
        return "==============================\n" +
                "Programare Detalii:\n" +
                "------------------------------\n" +
                "ID Programare: " + id + "\n" +
                "Pacient: " + pacient.getNume() + " " + pacient.getPrenume() + " (Id: " + pacient.getId() + ")"  + "\n" +
                "Data Programarii: " + data + "\n" +
                "Scopul Programarii: " + scop + "\n" +
                "------------------------------\n" +
                "==============================";
    }


}