package domain;

public interface EntityFactory<T extends Entity> {
    T createEntity(String line); // transforma linia din fisier intr-o entitate
    String toFileString(T entity); // transforma entitatea in linie din fisier
}
