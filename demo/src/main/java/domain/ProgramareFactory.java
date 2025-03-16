package domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProgramareFactory implements EntityFactory<Programare> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Override
    public Programare createEntity(String line) {
        String[] parts = line.split(",");
        if (parts.length != 7) {  // Verificam daca avem 7 elemente
            throw new IllegalArgumentException("Linie invalida in fisier: " + line);
        }
        try {
            int id = Integer.parseInt(parts[0]);
            int pacientId = Integer.parseInt(parts[1]);
            String pacientNume = parts[2];
            String pacientPrenume = parts[3];
            int pacientVarsta = Integer.parseInt(parts[4]);
            Pacient pacient = new Pacient(pacientId, pacientNume, pacientPrenume, pacientVarsta);
            String scop = parts[5];
            String dataString = parts[6];
            return new Programare(id, pacient, dateFormat.parse(dataString), scop);
        } catch (ParseException | NumberFormatException e) {
            throw new RuntimeException("Eroare la parsarea liniei: " + line, e);
        }
    }

    @Override
    public String toFileString(Programare programare) {
        return programare.getId() + "," +
                programare.getPacient().getId() + "," +
                programare.getPacient().getNume() + "," +
                programare.getPacient().getPrenume() + "," +
                programare.getPacient().getVarsta() + "," +
                programare.getScop() + "," +
                dateFormat.format(programare.getData());
    }
}