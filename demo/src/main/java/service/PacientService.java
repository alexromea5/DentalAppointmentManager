package service;

import domain.Pacient;
import repository.Repository;
import repository.RepositoryException;

import java.util.List;

public class PacientService {
    private final Repository<Pacient> pacientRepository;

    // Constructor
    public PacientService(Repository<Pacient> pacientRepository) {
        this.pacientRepository = pacientRepository;
    }

    public void adaugaPacient(Pacient pacientNou) throws Exception {
        if (pacientRepository.findById(pacientNou.getId()) != null) {
            throw new Exception("Pacientul cu ID-ul specificat exista deja.");
        }
        pacientRepository.add(pacientNou);
    }

    public void modificaPacient(Pacient pacientActualizat) throws Exception {
        Pacient existenta = pacientRepository.findById(pacientActualizat.getId());
        if (existenta == null) {
            throw new Exception("Pacientul cu ID-ul specificat nu exista.");
        }

        // Actualizeaza datele pacientului
        existenta.setNume(pacientActualizat.getNume());
        existenta.setPrenume(pacientActualizat.getPrenume());
        existenta.setVarsta(pacientActualizat.getVarsta());

        pacientRepository.update(existenta);
    }

    public void stergePacient(int id) throws Exception {
        Pacient pacient = pacientRepository.findById(id);
        if (pacient == null) {
            throw new Exception("Pacientul cu ID-ul specificat nu exista.");
        }
        pacientRepository.delete(id);
    }

    public Pacient gasestePacient(int id) throws RepositoryException {
        return pacientRepository.findById(id);
    }

    public List<Pacient> getAllPacienti() {
        return pacientRepository.getAll();
    }
}
