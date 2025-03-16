package com.example.demo;

import com.github.javafaker.Faker;
import domain.*;
import javafx.application.Application;
import repository.*;
import service.PacientService;
import service.ProgramareService;
import ui.ConsoleUI;
import ui.Settings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        // Citim setarile din fisierul settings.properties folosind clasa Settings
        String repositoryType = Settings.getProperty("Repository");
        String startMode = Settings.getProperty("StartMode");

        String basePath = System.getProperty("user.dir") + "/src/main/java/files/";
        String patientsFile = basePath + "pacienti.bin";
        String appointmentsFile = basePath + "programari.bin";


        // repo
        Repository<Pacient> pacientRepository;
        Repository<Programare> programareRepository;

        switch (repositoryType) {
            case "text" -> {
                System.out.println("Text File Repository");
                pacientRepository = new TextFileRepository<>(basePath + "pacienti.txt", new PacientFactory());
                programareRepository = new TextFileRepository<>(basePath + "programari.txt", new ProgramareFactory());
            }
            case "binary" -> {
                System.out.println("Binary Repository");
                pacientRepository = new BinaryFileRepository<>(patientsFile);
                programareRepository = new BinaryFileRepository<>(appointmentsFile);
            }
            case "database" -> {
                System.out.println("SQL Repository");
                pacientRepository = new SQLPacientRepository();
                programareRepository = new SQLProgramareRepository((SQLPacientRepository) pacientRepository);
                // adauga100Entitati((SQLPacientRepository) pacientRepository, (SQLProgramareRepository) programareRepository);
            }
            default ->
                    throw new RuntimeException("Tip de repository invalid in settings.properties: " + repositoryType);
        }

        System.out.println();

        // service
        PacientService pacientService = new PacientService(pacientRepository);
        ProgramareService programareService = new ProgramareService(programareRepository);

        // Pornim aplicatia folsosind interfata
        if ("gui".equalsIgnoreCase(startMode)) {
            HelloApplication.main(args);
        } else {
            // Pornim aplicatia in consola
            ConsoleUI consoleUI = new ConsoleUI(programareService, pacientService);
            consoleUI.start();
        }
    }
















    private static void adauga100Entitati(SQLPacientRepository pacientRepository, SQLProgramareRepository programareRepository) {
        Random random = new Random();

        String[] listaNume = {
                "Popescu", "Ionescu", "Georgescu", "Popa", "Stan", "Dumitrescu", "Radu", "Matei", "Andrei", "Alexandru",
                "Constantin", "Marin", "Florea", "Dobre", "Barbu", "Iancu", "Stoica", "Diaconu", "Vlad", "Stefan",
                "Petrescu", "Dragomir", "Solomon", "Albu", "Vasilescu", "Cristea", "Toma", "Oprea", "Manole", "Tudor"
        };

        String[] listaPrenume = {
                "Ion", "Maria", "Ana", "Elena", "Mihai", "George", "Andreea", "Cristina", "Laura", "Ioana",
                "Alexandru", "Andrei", "Gabriel", "Marius", "Daniel", "David", "Stefan", "Sorin", "Adrian", "Mircea",
                "Anca", "Ioana", "Teodora", "Alexandra", "Gabriela", "Diana", "Simona", "Raluca", "Andreea", "Mihaela"
        };

        for (int i = 1; i <= 100; i++) {
            // Genereaza pacient
            String nume = listaNume[random.nextInt(listaNume.length)];
            String prenume = listaPrenume[random.nextInt(listaPrenume.length)];
            int varsta = random.nextInt(100) + 1; // Varsta intre 1 si 100
            Pacient pacient = new Pacient(i, nume, prenume, varsta);
            try {
                pacientRepository.add(pacient);
            } catch (Exception e) {
                System.err.println("Eroare la adaugarea pacientului: " + e.getMessage());
            }

            // Genereaza programare
            LocalDate dataStart = LocalDate.of(2023, 1, 1);
            LocalDate dataEnd = LocalDate.of(2024, 12, 31);
            long randomDay = dataStart.toEpochDay() + random.nextInt((int) ((dataEnd.toEpochDay() - dataStart.toEpochDay()) + 1));
            LocalDate randomLocalDate = LocalDate.ofEpochDay(randomDay);

            // Genereaza ora aleatorie intre 8:00 si 18:00
            int ora = random.nextInt(11) + 8; // Genereaza un numar intre 8 si 18
            LocalTime randomLocalTime = LocalTime.of(ora, 0); // Setam minutele la 0

            LocalDateTime randomLocalDateTime = LocalDateTime.of(randomLocalDate, randomLocalTime);
            Date data = Date.from(randomLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

            String[] scopuri = {"Control", "Consultatie", "Tratament"};
            String scop = scopuri[random.nextInt(scopuri.length)];
            Programare programare = new Programare(i, pacient, data, scop);
            try {
                programareRepository.add(programare);
            } catch (Exception e) {
                System.err.println("Eroare la adaugarea programarii: " + e.getMessage());
            }
        }
    }


}

