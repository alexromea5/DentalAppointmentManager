package repository;

import domain.Pacient;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLPacientRepository extends Repository<Pacient> {
    // private static final String JDBC_URL = "jdbc:sqlite:src/main/java/files/pacienti.db";
    private static final String JDBC_URL = "jdbc:sqlite:C:/Users/Alex/Desktop/MAP/a4-luvisrage16/demo/src/main/java/files/pacienti.db";
    private Connection conn = null;

    public SQLPacientRepository() {
        openConnection();
        createSchema();
        loadData();
    }

    private void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Pacienti");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Pacient pacient = new Pacient(rs.getInt("id"), rs.getString("nume"), rs.getString("prenume"), rs.getInt("varsta"));

                super.add(pacient);
            }
        } catch (SQLException | RepositoryException e) {
            e.printStackTrace();
        }
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea cu baza de date", e);
        }
    }

    private void createSchema() {
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Creating table Pacienti...");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Pacienti (" +
                    "id INTEGER PRIMARY KEY," +
                    "nume VARCHAR(255)," +
                    "prenume VARCHAR(255)," +
                    "varsta INTEGER);");
            System.out.println("Table Pacienti created successfully!");
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public void add(Pacient elem) throws RepositoryException {
        super.add(elem);
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO Pacienti (id, nume, prenume, varsta) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, elem.getId());
            statement.setString(2, elem.getNume());
            statement.setString(3, elem.getPrenume());
            statement.setInt(4, elem.getVarsta());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la salvarea in baza de date");
        }
    }

    @Override
    public void update(Pacient elem) throws RepositoryException {
        super.update(elem);
        try (PreparedStatement statement = conn.prepareStatement("UPDATE Pacienti SET nume = ?, prenume = ?, varsta = ? WHERE id = ?")) {
            statement.setString(1, elem.getNume());
            statement.setString(2, elem.getPrenume());
            statement.setInt(3, elem.getVarsta());
            statement.setInt(4, elem.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) throws RepositoryException {
        super.delete(id);
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM Pacienti WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la stergerea pacientului: " + e.getMessage());
        }
    }

    public void close() throws RepositoryException {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la conexiune!");
        }
    }


    public Optional<Pacient> find(int id) {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Pacienti WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Pacient pacient = new Pacient(
                            rs.getInt("id"),
                            rs.getString("nume"),
                            rs.getString("prenume"),
                            rs.getInt("varsta")
                    );
                    return Optional.of(pacient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}