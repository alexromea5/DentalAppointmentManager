package ui;

import domain.Pacient;
import domain.Programare;
import repository.RepositoryException;
import service.PacientService;
import service.ProgramareService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private final ProgramareService programareService;
    private final PacientService pacientService;

    public ConsoleUI(ProgramareService programareService, PacientService pacientService) {
        this.programareService = programareService;
        this.pacientService = pacientService;
    }

    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("(1) Adauga pacient");
            System.out.println("(2) Modifica pacient");
            System.out.println("(3) Sterge pacient");
            System.out.println("(4) Afiseaza pacienti");
            System.out.println("<----------------->");
            System.out.println("(5) Adauga programare");
            System.out.println("(6) Modifica programare");
            System.out.println("(7) Sterge programare");
            System.out.println("(8) Afiseaza toate programarile");
            System.out.println("<----------------->");
            System.out.println("(9)  Numar programari per pacient");
            System.out.println("(10) Numar programari per luna");
            System.out.println("(11) Zile de la ultima programare");
            System.out.println("(0) Iesire");

            System.out.print("Optiune: ");
            int optiune = scanner.nextInt();

            switch (optiune) {
                case 1:
                    adaugaPacient(scanner);
                    break;
                case 2:
                    modificaPacient(scanner);
                    break;
                case 3:
                    stergePacient(scanner);
                    break;
                case 4:
                    afiseazaPacienti();
                    break;
                case 5:
                    adaugaProgramare(scanner);
                    break;
                case 6:
                    modificaProgramare(scanner);
                    break;
                case 7:
                    stergeProgramare(scanner);
                    break;
                case 8:
                    afiseazaProgramari();
                    break;
                case 9:
                    afiseazaNumarProgramariPerPacient();
                    break;
                case 10:
                    afiseazaNumarProgramariPerLuna();
                    break;
                case 11:
                    afiseazaZileDeLaUltimaProgramare();
                    break;
                case 0:
                    running = false;
                    System.out.println("Programul se inchide..");
                    break;
                default:
                    System.out.println("Optiune invalida");
            }
        }
    }

    private void adaugaPacient(Scanner scanner) {
        int id;
        while (true) {
            try {
                System.out.print("ID pacient: ");
                id = validareId(scanner.nextInt());
                break; // Iesim din bucla daca ID-ul este valid
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        System.out.print("Nume: ");
        String nume = scanner.next();

        System.out.print("Prenume: ");
        String prenume = scanner.next();

        int varsta;
        while (true) {
            try {
                System.out.print("Varsta: ");
                varsta = validareVarsta(scanner.nextInt());
                break; // Iesim din bucla daca varsta este valida
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        try {
            Pacient pacient = new Pacient(id, nume, prenume, varsta);
            pacientService.adaugaPacient(pacient);
            System.out.println("Pacient adaugat cu succes!");
        } catch (Exception e) {
            System.out.println("Eroare la adaugarea pacientului: " + e.getMessage());
        }
    }

    private int validareId(int id) throws RepositoryException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID-ul trebuie sa fie un numar pozitiv.");
        }
        if (pacientService.gasestePacient(id) != null) {
            throw RepositoryException.duplicateId(id);
        }
        return id;
    }

    private int validareVarsta(int varsta) {
        if (varsta <= 0) {
            throw new IllegalArgumentException("Varsta trebuie sa fie un numar pozitiv.");
        }
        return varsta;
    }

    private Date validareData(String dataString, String ora) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        dateFormat.setLenient(false);
        return dateFormat.parse(dataString + " " + ora);
    }

    private void modificaPacient(Scanner scanner) {
        try {
            System.out.print("Introduceti ID-ul pacientului pe care doriti sa il modificati: ");
            int id = scanner.nextInt();
            Pacient pacient = pacientService.gasestePacient(id);

            System.out.print("Introduceti noul nume (curent: " + pacient.getNume() + "): ");
            String nume = scanner.next();

            System.out.print("Introduceti noul prenume (curent: " + pacient.getPrenume() + "): ");
            String prenume = scanner.next();

            System.out.print("Introduceti noua varsta (curenta: " + pacient.getVarsta() + "): ");
            int varsta = validareVarsta(scanner.nextInt());

            pacient.setNume(nume);
            pacient.setPrenume(prenume);
            pacient.setVarsta(varsta);

            pacientService.modificaPacient(pacient);
            System.out.println("Pacientul a fost actualizat cu succes!");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void adaugaProgramare(Scanner scanner) {
        int id;
        while (true) {
            try {
                System.out.print("ID programare: ");
                id = validareIdProgramare(scanner.nextInt()); // Functie separata pentru validarea ID-ului programarii
                break; // Iesim din bucla daca ID-ul este valid
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        Pacient pacient;
        while (true) {
            try {
                System.out.print("ID pacient: ");
                int pacientId = scanner.nextInt();
                pacient = pacientService.gasestePacient(pacientId);
                if (pacient == null) {
                    throw RepositoryException.notFound(pacientId);
                }
                break; // Iesim din bucla daca pacientul este valid
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }

        System.out.print("Scopul programarii: ");
        String scop = scanner.next();

        Date data;
        while (true) {
            System.out.print("Introduceti data (format DD-MM-YYYY): ");
            String dataString = scanner.next();
            System.out.print("Introduceti ora (format HH:MM): ");
            String ora = scanner.next();
            try {
                data = validareData(dataString, ora);
                break; // Iesim din bucla daca data este valida
            } catch (ParseException e) {
                System.out.println("Formatul datei sau al orei este invalid.");
            }
        }

        try {
            Programare programare = new Programare(id, pacient, data, scop);
            programareService.adaugaProgramare(programare);
            System.out.println("Programare adaugata cu succes!");
        } catch (Exception e) {
            System.out.println("Eroare la adaugarea programarii: " + e.getMessage());
        }
    }

    private int validareIdProgramare(int id) throws Exception { // Functie separata pentru validarea ID-ului programarii
        if (id <= 0) {
            throw new IllegalArgumentException("ID-ul trebuie sa fie un numar pozitiv.");
        }
        if (programareService.gasesteProgramare(id) != null) {
            throw RepositoryException.duplicateId(id);
        }
        return id;
    }

    private void modificaProgramare(Scanner scanner) {
        try {
            System.out.print("Introduceti ID-ul programarii de modificat: ");
            int id = scanner.nextInt();
            Programare programare = programareService.gasesteProgramare(id);

            System.out.print("Scop nou (curent: " + programare.getScop() + "): ");
            String scop = scanner.next();

            Date data;
            while (true) {
                System.out.print("Introduceti data (format DD-MM-YYYY): ");
                String dataString = scanner.next();
                System.out.print("Introduceti ora (format HH:MM): ");
                String ora = scanner.next();
                try {
                    data = validareData(dataString, ora);
                    break; // Iesim din bucla daca data este valida
                } catch (ParseException e) {
                    System.out.println("Formatul datei sau al orei este invalid.");
                }
            }


            programare.setScop(scop);
            programare.setData(data);

            programareService.modificaProgramare(programare);
            System.out.println("Programare modificata cu succes!");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void stergePacient(Scanner scanner) {
        System.out.print("ID pacient de sters: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            // Verificam daca exista programari asociate pacientului
            List<Programare> programariPacient = programareService.getAllProgramari().stream()
                    .filter(programare -> programare.getPacient().getId() == id)
                    .toList();

            if (!programariPacient.isEmpty()) {
                // Afiseaza mesajul de avertizare
                System.out.println("Atentie! Stergerea acestui pacient va sterge si toate programarile asociate. Continuati? (da/nu)");

                // Citeste raspunsul utilizatorului
                String raspuns = scanner.nextLine();

                if (raspuns.equalsIgnoreCase("da")) {
                    // Sterge toate programarile asociate pacientului
                    for (Programare programare : programariPacient) {
                        programareService.stergeProgramare(programare.getId());
                    }

                    // Sterge pacientul
                    pacientService.stergePacient(id);
                    System.out.println("Pacient sters cu succes!");
                } else {
                    System.out.println("Operatie anulata.");
                }
            } else {
                // Sterge pacientul
                pacientService.stergePacient(id);
                System.out.println("Pacient sters cu succes!");
            }
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void afiseazaPacienti() {
        try {
            for (Pacient pacient : pacientService.getAllPacienti()) {
                System.out.println(pacient);
            }
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void stergeProgramare(Scanner scanner) {
        System.out.print("ID programare de sters: ");
        int id = scanner.nextInt();

        try {
            programareService.stergeProgramare(id);
            System.out.println("Programare stearsa cu succes!");
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void afiseazaProgramari() {
        try {
            for (Programare programare : programareService.getAllProgramari()) {
                System.out.println(programare);
            }
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    private void afiseazaNumarProgramariPerPacient() {
        Map<Pacient, Long> raport = programareService.numarProgramariPerPacient();
        raport.forEach((pacient, numarProgramari) -> System.out.println(pacient.getNume() + " " + pacient.getPrenume() + ": " + numarProgramari));
    }

    private void afiseazaNumarProgramariPerLuna() {
        Map<String, Long> raport = programareService.numarProgramariPerLuna();
        raport.forEach((luna, numarProgramari) ->
                System.out.println(luna + ": " + numarProgramari)
        );
    }


    private void afiseazaZileDeLaUltimaProgramare() {
        Map<Pacient, Long> raport = programareService.zileDeLaUltimaProgramare();
        raport.forEach((pacient, zile) -> System.out.println(pacient.getNume() + " " + pacient.getPrenume() + ": " + zile + " zile"));
    }


}