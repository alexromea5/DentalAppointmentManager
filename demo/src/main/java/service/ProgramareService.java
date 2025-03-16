package service;

import domain.Pacient;
import domain.Programare;
import repository.Repository;
import repository.RepositoryException;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ProgramareService {
    private final Repository<Programare> programareRepository;

    // Constructor
    public ProgramareService(Repository<Programare> programareRepository) {
        this.programareRepository = programareRepository;
    }

    public void adaugaProgramare(Programare programareNoua) throws Exception {
        for (Programare programareExistenta : programareRepository.getAll()) {
            // Verificare suprapunere data pentru acelasi pacient
            if (programareExistenta.getPacient().getId() == programareNoua.getPacient().getId()) {
                Date startNou = programareNoua.getData();
                Date endNou = programareNoua.getEndTime();
                Date startExistenta = programareExistenta.getData();
                Date endExistenta = programareExistenta.getEndTime();

                boolean suprapunere = (startNou.before(endExistenta) && endNou.after(startExistenta));
                if (suprapunere) {
                    throw new Exception("Programarea se suprapune cu o alta programare existenta pentru acest pacient");
                }
            }
        }
        programareRepository.add(programareNoua);
    }

    public void modificaProgramare(Programare programareActualizata) throws Exception {
        Programare existenta = programareRepository.findById(programareActualizata.getId());
        if (existenta == null) {
            throw new Exception("Programarea cu ID-ul specificat nu exista.");
        }

        // Actualizeaza datele programarii
        existenta.setScop(programareActualizata.getScop());
        existenta.setData(programareActualizata.getData());
        existenta.setPacient(programareActualizata.getPacient());

        programareRepository.update(existenta);
    }

    public void stergeProgramare(int id) throws Exception {
        Programare programare = programareRepository.findById(id);
        if (programare == null) {
            throw new Exception("Programarea cu ID-ul specificat nu exista.");
        }
        programareRepository.delete(id);
    }

    public Programare gasesteProgramare(int id) throws RepositoryException, RepositoryException {
        return programareRepository.findById(id); // null daca nu exista
    }

    public List<Programare> getAllProgramari() {
        return programareRepository.getAll();
    }

    // Cerinta JAVA 8 Streams

    public Map<Pacient, Long> numarProgramariPerPacient() {
        return getAllProgramari().stream()
                .collect(Collectors.groupingBy(
                        Programare::getPacient, // Grupam programarile după pacient
                        Collectors.counting()   // Numaram programarile
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Pacient, Long>comparingByValue(Comparator.reverseOrder())) // Ordonam descrescator
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Rezolvam conflictele (dacă ar aparea
                        LinkedHashMap::new // pastram ordinea sortarii
                ));
    }

    // Identic cu cerinta 'Cele mai aglomerate luni'
    public Map<String, Long> numarProgramariPerLuna() {
        return getAllProgramari().stream()
                .collect(Collectors.groupingBy(
                        programare -> Month.of(programare.getData().getMonth() + 1).name(), // Convertim numarul lunii în nume
                        Collectors.counting() // Numărăm programările
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())) // Ordonam descrescator
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Pastram ordinea sortarii
                ));
    }

    public Map<Pacient, Long> zileDeLaUltimaProgramare() {
        return getAllProgramari().stream()
                .collect(Collectors.groupingBy(
                        Programare::getPacient, // Grupăm programările după pacient
                        Collectors.maxBy(Comparator.comparing(Programare::getData)) // Gasim ultima programare
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue().isPresent()) // Filtram cazurile fara programari
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Programare ultimaProgramare = entry.getValue().get();
                            return ChronoUnit.DAYS.between(
                                    ultimaProgramare.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                                    LocalDate.now()
                            ); // Calculăm numărul de zile
                        }
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Pacient, Long>comparingByValue(Comparator.reverseOrder())) // Ordonam descrescator
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


}
