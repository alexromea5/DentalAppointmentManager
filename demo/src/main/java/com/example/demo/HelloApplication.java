package com.example.demo;

import domain.Programare;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.Repository;
import repository.SQLPacientRepository;
import repository.SQLProgramareRepository;
import service.PacientService;
import service.ProgramareService;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SQLPacientRepository pacientRepository;
        Repository<Programare> programareRepository;

        // Initializeaza serviciile
        pacientRepository = new SQLPacientRepository();
        programareRepository = new SQLProgramareRepository(pacientRepository);

        PacientService pacientService = new PacientService(pacientRepository);
        ProgramareService programareService = new ProgramareService(programareRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 680);

        HelloController controller = fxmlLoader.getController();
        controller.setServices(pacientService, programareService);

        stage.setTitle("Gestionare Pacienti si Programari");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

