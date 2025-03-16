package repository;

import domain.Pacient;
import domain.Programare;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class SQLProgramareRepository extends Repository<Programare> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Connection conn = null;
    private static final String JDBC_URL = "jdbc:sqlite:C:/Users/Alex/Desktop/MAP/a4-luvisrage16/demo/src/main/java/files/programari.db";
    private final SQLPacientRepository pacientRepository; // Add this line

    public SQLProgramareRepository(SQLPacientRepository pacientRepository) { // Update constructor
        this.pacientRepository = pacientRepository;
        openConnection();
        createSchema();
        loadData();
    }

    private void loadData() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Programari");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int pacientId = rs.getInt("pacient_id");
                String scop = rs.getString("scop");
                Timestamp data = rs.getTimestamp("data"); // Use Timestamp for date and time

                Pacient pacient = getPacientById(pacientId);

                Programare programare = new Programare(id, pacient, data, scop);
                super.add(programare);
            }
        } catch (SQLException | RepositoryException e) {
            e.printStackTrace();
        }
    }

    private Pacient getPacientById(int pacientId) throws RepositoryException {
        Optional<Pacient> optionalPacient = pacientRepository.find(pacientId);
        return optionalPacient.orElseThrow(() -> new RepositoryException("Pacientul cu ID-ul " + pacientId + " nu a fost găsit."));
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
            System.out.println("Creating table Programari...");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Programari (" +
                    "id INTEGER PRIMARY KEY," +
                    "pacient_id INTEGER," +
                    "scop VARCHAR(255)," +
                    "data TIMESTAMP," + // Changed to TIMESTAMP
                    "FOREIGN KEY (pacient_id) REFERENCES Pacienti(id));");
            System.out.println("Table Programari created successfully!");
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public void add(Programare elem) throws RepositoryException {
        super.add(elem);
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO Programari (id, pacient_id, scop, data) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, elem.getId());
            statement.setInt(2, elem.getPacient().getId());
            statement.setString(3, elem.getScop());
            statement.setTimestamp(4, new Timestamp(elem.getData().getTime())); // Use Timestamp
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la salvarea in baza de date: " + e.getMessage());
        }
    }

    @Override
    public void update(Programare elem) throws RepositoryException {
        super.update(elem);
        try (PreparedStatement statement = conn.prepareStatement("UPDATE Programari SET pacient_id = ?, scop = ?, data = ? WHERE id = ?")) {
            statement.setInt(1, elem.getPacient().getId());
            statement.setString(2, elem.getScop());
            statement.setTimestamp(3, new Timestamp(elem.getData().getTime())); // Use Timestamp
            statement.setInt(4, elem.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea in baza de date: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws RepositoryException {
        super.delete(id);
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM Programari WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la stergerea programarii: " + e.getMessage());
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
}