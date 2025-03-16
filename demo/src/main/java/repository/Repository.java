// repository.java

package repository;

import domain.Entity;
import domain.Pacient;
import domain.Programare;

import java.util.ArrayList;
import java.util.List;

public class Repository<T extends Entity> implements GenericRepository<T> {
    private List<T> entities = new ArrayList<>();

    @Override
    public void add(T entity) throws RepositoryException {
        if (findById(entity.getId()) != null) {
            throw new RepositoryException(entity.getId() + " already exists");
        }
        entities.add(entity);
        System.out.println("Entitatea " + entity.getId() + " a fost adaugata cu success!");
    }

    @Override
    public T findById(int id) throws RepositoryException {
        for (T entity : entities) {
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
            throw new RepositoryException("Entitatea cu ID-ul " + entity.getId() + " nu exista.");
        }
        int index = entities.indexOf(existingEntity);
        entities.set(index, entity);
        System.out.println("Entitatea " + entity.getId() + " a fost actualizata cu succes!");
    }


    @Override
    public void delete(int id) throws RepositoryException {
        T entity = findById(id);
        entities.remove(entity);
        System.out.println("Entitatea " + entity.getId() + " a fost stearsa cu success!");

    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entities);
    }


}

