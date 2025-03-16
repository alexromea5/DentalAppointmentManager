package repository;

import domain.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileRepository<T extends Entity> extends Repository<T> {
    private final String fileName;
    private List<T> data = new ArrayList<>();

    public BinaryFileRepository(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    private void loadData() {
        if (new File(fileName).length() == 0) {
            this.data = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            this.data = (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            this.data = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Eroare la incarcarea datelor din fisierul binar: " + e.getMessage(), e);
        }
    }


    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la salvarea datelor în fisierul binar: " + e.getMessage(), e);
        }
    }

    @Override
    public void add(T entity) throws RepositoryException {
        if (findById(entity.getId()) != null) {
            throw RepositoryException.duplicateId(entity.getId());
        }
        data.add(entity);
        saveData();
    }

    @Override
    public T findById(int id) {
        for (T entity : data) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public void update(T entity) throws RepositoryException {
        T existingEntity = findById(entity.getId());
        if (existingEntity == null) {
            throw RepositoryException.notFound(entity.getId());
        }
        int index = data.indexOf(existingEntity);
        data.set(index, entity);
        saveData();
    }

    @Override
    public void delete(int id) throws RepositoryException {
        T entity = findById(id);
        if (entity == null) {
            throw RepositoryException.notFound(id);
        }
        data.remove(entity);
        saveData();
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(data);
    }
}