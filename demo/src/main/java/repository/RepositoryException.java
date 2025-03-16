package repository;

public class RepositoryException extends Exception {
    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    // Metode statice pentru cazuri frecvente
    public static RepositoryException duplicateId(int id) {
        return new RepositoryException("Entitatea cu ID-ul " + id + " exista deja.");
    }

    public static RepositoryException notFound(int id) {
        return new RepositoryException("Entitatea cu ID-ul " + id + " nu a fost gasita.");
    }

    public static RepositoryException fileError(String fileName, Throwable cause) {
        return new RepositoryException("Eroare la fisierul " + fileName, cause);
    }
}