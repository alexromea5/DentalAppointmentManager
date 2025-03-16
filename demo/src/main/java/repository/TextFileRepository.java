package repository;

import domain.Entity;
import domain.EntityFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileRepository<T extends Entity> extends Repository<T> {
    private final String fileName;
    private final EntityFactory<T> factory;

    public TextFileRepository(String fileName, EntityFactory<T> factory) throws RepositoryException {
        this.fileName = fileName;
        this.factory = factory;
        this.readFromFile();
    }

    private void readFromFile() throws RepositoryException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T entityToAdd = factory.createEntity(line);
                super.add(entityToAdd);
            }
        } catch (FileNotFoundException e) {
            // Fisierul va fi creat la prima scriere
            System.out.println("Fisierul nu a fost gasit. Se va crea la prima salvare.");
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citirea din fisier: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile() throws RepositoryException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (T entity : getAll()) {
                writer.write(factory.toFileString(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scrierea in fisier: " + e.getMessage());
        }
    }

    public void add(T entity) throws RepositoryException {
        try {
            super.add(entity);
            writeToFile();
        } catch (Exception e) {
            throw new RepositoryException("Eroare la adaugarea entitatii", e);
        }
    }

    @Override
    public void update(T entity) throws RepositoryException {
        try {
            super.update(entity);
            writeToFile();
        } catch (Exception e) {
            throw new RepositoryException("Eroare la actualizarea entitatii", e);
        }
    }

    @Override
    public void delete(int id) throws RepositoryException {
        try {
            super.delete(id);
            writeToFile();
        } catch (Exception e) {
            throw new RepositoryException("Eroare la stergerea entitatii", e);
        }
    }

    // ... restul metodelor (findById, getAll) sunt neschimbate
}