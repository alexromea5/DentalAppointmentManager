package domain;

public class PacientFactory implements EntityFactory<Pacient> {
    @Override
    public Pacient createEntity(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String nume = parts[1];
        String prenume = parts[2];
        int varsta = Integer.parseInt(parts[3]);
        return new Pacient(id, nume, prenume, varsta);
    }

    @Override
    public String toFileString(Pacient pacient) {
        return pacient.getId() + "," + pacient.getNume() + "," + pacient.getPrenume() + "," + pacient.getVarsta();
    }
}
