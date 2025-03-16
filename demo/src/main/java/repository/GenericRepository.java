package repository;

import domain.Entity;
import java.util.List;

// operatii CRUD
public interface GenericRepository<T extends Entity> {
    void add(T entity) throws RepositoryException;
    T findById(int id) throws RepositoryException;
    void update(T entity) throws RepositoryException;
    void delete(int id) throws RepositoryException;
    List<T> getAll();
}