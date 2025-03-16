package com.example.demo;

import domain.Pacient;
import domain.Programare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.PacientService;
import service.ProgramareService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HelloController {
    @FXML
    private TextField pacientIdField;
    @FXML
    private TextField pacientNumeField;
    @FXML
    private TextField pacientPrenumeField;
    @FXML
    private TextField pacientVarstaField;

    @FXML
    private TextField programareIdField;
    @FXML
    private TextField programarePacientIdField;
    @FXML
    private TextField programareScopField;
    @FXML
    private DatePicker programareDataField;
    @FXML
    private TextField programareOraField;

    @FXML
    private ListView<Pacient> pacientiListView;
    @FXML
    private ListView<Programare> programariListView;

    private PacientService pacientService;
    private ProgramareService programareService;

    public void setServices(PacientService pacientService, ProgramareService programareService) {
        this.pacientService = pacientService;
        this.programareService = programareService;
        refreshLists();
    }

    @FXML
    private void handleAdaugaPacient(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(pacientIdField.getText());
            String nume = pacientNumeField.getText();
            String prenume = pacientPrenumeField.getText();
            int varsta = Integer.parseInt(pacientVarstaField.getText());

            Pacient pacient = new Pacient(id, nume, prenume, varsta);
            pacientService.adaugaPacient(pacient);
            refreshLists();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleAdaugaProgramare(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(programareIdField.getText());
            int pacientId = Integer.parseInt(programarePacientIdField.getText());
            String scop = programareScopField.getText();

            // Obtine data si ora din controalele JavaFX
            LocalDate data = programareDataField.getValue();
            String oraString = programareOraField.getText();

            // Parseaza ora
            LocalTime ora = LocalTime.parse(oraString);

            // Combina data si ora intr-un obiect LocalDateTime
            LocalDateTime dataOra = LocalDateTime.of(data, ora);

            // Converteste LocalDateTime la Date
            Date dataProgramare = Date.from(dataOra.atZone(ZoneId.systemDefault()).toInstant());

            Pacient pacient = pacientService.gasestePacient(pacientId);
            if (pacient == null) {
                throw new Exception("Nu exista pacient cu acest id");
            }

            Programare programare = new Programare(id, pacient, dataProgramare, scop);
            programareService.adaugaProgramare(programare);
            refreshLists();
        } catch (Exception e) {
            // Afiseaza un mesaj de eroare utilizatorului
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText("Eroare la adaugarea programarii");
            alert.setContentText(e.getMessage()); // Afiseaza mesajul exceptiei
            alert.showAndWait();
        }
    }

    @FXML
    private void handleStergePacient(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(pacientIdField.getText());

            // Verifica daca exista programari asociate pacientului
            List<Programare> programariPacient = programareService.getAllProgramari().stream()
                    .filter(programare -> programare.getPacient().getId() == id)
                    .toList();

            if (!programariPacient.isEmpty()) {
                // Afiseaza un mesaj de avertizare
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmare stergere");
                alert.setHeaderText("Stergere pacient cu programari existente");
                alert.setContentText("Prin stergerea acestui pacient se vor sterge si programarile asociate. Doriti sa continuati?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Sterge pacientul si programarile asociate
                    programariPacient.forEach(programare -> {
                        try {
                            programareService.stergeProgramare(programare.getId());
                        } catch (Exception e) {
                            System.err.println("Eroare la stergerea programarii: " + e.getMessage());
                        }
                    });

                    pacientService.stergePacient(id);
                    refreshLists();
                }
            } else {
                // Sterge pacientul
                pacientService.stergePacient(id);
                refreshLists();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText("Eroare la stergerea pacientului");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleModificaPacient(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(pacientIdField.getText());
            String nume = pacientNumeField.getText();
            String prenume = pacientPrenumeField.getText();
            int varsta = Integer.parseInt(pacientVarstaField.getText());

            Pacient pacient = new Pacient(id, nume, prenume, varsta);
            pacientService.modificaPacient(pacient);
            refreshLists();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleStergeProgramare(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(programareIdField.getText());
            programareService.stergeProgramare(id);
            refreshLists();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleModificaProgramare(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(programareIdField.getText());
            int pacientId = Integer.parseInt(programarePacientIdField.getText());
            String scop = programareScopField.getText();

            // Obține data și ora din controalele JavaFX
            LocalDate data = programareDataField.getValue();
            String oraString = programareOraField.getText();

            // Parseaza ora
            LocalTime ora = LocalTime.parse(oraString);

            // Combina data si ora intr-un obiect LocalDateTime
            LocalDateTime dataOra = LocalDateTime.of(data, ora);

            // Converteste LocalDateTime la Date
            Date dataProgramare = Date.from(dataOra.atZone(ZoneId.systemDefault()).toInstant());

            Pacient pacient = pacientService.gasestePacient(pacientId);
            if (pacient == null) {
                // Afișează un mesaj de eroare utilizatorului
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare");
                alert.setHeaderText("Pacient invalid");
                alert.setContentText("Nu există pacient cu acest ID.");
                alert.showAndWait();
                return;
            }

            Programare programare = new Programare(id, pacient, dataProgramare, scop);
            programareService.modificaProgramare(programare);
            refreshLists();
        } catch (Exception e) {
            // Mesaj de eroare
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText("Eroare la modificarea programarii");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private TextArea raportTextArea;

    @FXML
    private void handleNumarProgramariPerPacient(ActionEvent actionEvent) {
        Map<Pacient, Long> raport = programareService.numarProgramariPerPacient();

        // Construieste textul raportului
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Pacient, Long> entry : raport.entrySet()) {
            Pacient pacient = entry.getKey();
            Long numarProgramari = entry.getValue();
            sb.append(pacient.getNume()).append(" ").append(pacient.getPrenume()).append(": ").append(numarProgramari).append("\n");
        }

        // Afiseaza raportul in TextArea
        raportTextArea.setText(sb.toString());
    }

    @FXML
    private void handleNumarProgramariPerLuna(ActionEvent actionEvent) {
        Map<String, Long> raport = programareService.numarProgramariPerLuna();

        // Construieste textul raportului
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : raport.entrySet()) {
            String luna = entry.getKey();
            Long numarProgramari = entry.getValue();
            sb.append(luna).append(": ").append(numarProgramari).append("\n");
        }

        // Afiseaza raportul in TextArea
        raportTextArea.setText(sb.toString());
    }

    @FXML
    private void handleZileDeLaUltimaProgramare(ActionEvent actionEvent) {
        Map<Pacient, Long> raport = programareService.zileDeLaUltimaProgramare();

        // Construieste textul raportului
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Pacient, Long> entry : raport.entrySet()) {
            Pacient pacient = entry.getKey();
            Long zile = entry.getValue();
            sb.append(pacient.getNume()).append(" ").append(pacient.getPrenume()).append(": ").append(zile).append(" zile\n");
        }

        // Afiseaza raportul in TextArea
        raportTextArea.setText(sb.toString());
    }

    private void refreshLists() {
        ObservableList<Pacient> pacienti = FXCollections.observableArrayList(pacientService.getAllPacienti());
        pacientiListView.setItems(pacienti);

        ObservableList<Programare> programari = FXCollections.observableArrayList(programareService.getAllProgramari());
        programariListView.setItems(programari);
    }
}