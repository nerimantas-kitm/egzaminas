package lt.kitm.model;

import lt.kitm.dto.FilmasDTO;

import java.sql.*;
import java.util.ArrayList;

public class FilmasDAO {
    private static final String URL = "jdbc:mysql://localhost/db";
    private static final String[] prisijungimas = new String[]{"root", ""};

    public static ArrayList<FilmasDTO> visiFilmai() throws SQLException {
        ArrayList<FilmasDTO> filmai = new ArrayList<>();
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM filmai JOIN kategorijos ON kategorijos_id = kategorijos.id");
        while (resultSet.next()) {
            filmai.add(new FilmasDTO(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getString(9)
                    ));
        }
        statement.close();
        connection.close();
        return filmai;
    }

    public static void delete(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM filmai WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void create(Filmas filmas) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO filmai (isbn, pavadinimas, santrauka, nuotrauka, puslapiu_skaicius, kategorijos_id) VALUES (?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, filmas.getIsbn());
        preparedStatement.setString(2, filmas.getPavadinimas());
        preparedStatement.setString(3, filmas.getSantrauka());
        preparedStatement.setString(4, filmas.getNuotrauka());
        preparedStatement.setInt(5, filmas.getPuslapiuSkaicius());
        preparedStatement.setInt(6, filmas.getKategorijosId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void update(Filmas filmas) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE filmai SET isbn = ?, pavadinimas = ?, santrauka = ?, nuotrauka = ?, puslapiu_skaicius = ?, kategorijos_id = ? WHERE id = ?");
        preparedStatement.setString(1, filmas.getIsbn());
        preparedStatement.setString(2, filmas.getPavadinimas());
        preparedStatement.setString(3, filmas.getSantrauka());
        preparedStatement.setString(4, filmas.getNuotrauka());
        preparedStatement.setInt(5, filmas.getPuslapiuSkaicius());
        preparedStatement.setInt(6, filmas.getKategorijosId());
        preparedStatement.setInt(7, filmas.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void pamegtiFilma(int vartotojoId, int knygosId) throws SQLException {
        //TODO: neleisti pamegti daugiau nei viena kartą
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pamegtos_knygos VALUES(?, ?)");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, knygosId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static ArrayList<FilmasDTO> gautiPamegtasKnygas(int vartotojoId) throws SQLException {
        ArrayList<FilmasDTO> filmai = new ArrayList<>();
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM filmai JOIN kategorijos ON kategorijos_id = kategorijos.id JOIN pamegtos_knygos ON filmai.id = pamegtos_knygos.knygos_id WHERE pamegtos_knygos.vartotojo_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            filmai.add(new FilmasDTO(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getString(9)
            ));
        }
        preparedStatement.close();
        connection.close();
        return filmai;
    }

    public static void rezervuotiFilma(int vartotojoId, int filmoId) throws SQLException {
        //TODO: neleisti rezervuoti daugiau nei viena kartą
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO rezervacijos VALUES(?, ?)");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmoId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static ArrayList<FilmasDTO> gautiRezervuotusFilmus(int vartotojoId) throws SQLException {
        ArrayList<FilmasDTO> filmai = new ArrayList<>();
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM filmai JOIN kategorijos ON kategorijos_id = kategorijos.id JOIN rezervacijos ON filmai.id = rezervacijos.knygos_id WHERE rezervacijos.vartotojo_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            filmai.add(new FilmasDTO(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getString(9)
            ));
        }
        preparedStatement.close();
        connection.close();
        return filmai;
    }

    public static boolean filmasRezervuotas(int filmoId) throws SQLException {
        boolean filmasUzimtas = false;
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rezervacijos WHERE knygos_id = ?");
        preparedStatement.setInt(1, filmoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        filmasUzimtas = resultSet.isBeforeFirst();
        preparedStatement.close();
        connection.close();
        return filmasUzimtas;
    }

    public static void nebemegtiFilmo(int vartotojoId, int filmoId) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM pamegtos_knygos WHERE vartotojo_id = ? AND knygos_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmoId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void atsauktiRezervacija(int vartotojoId, int filmoId) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM rezervacijos WHERE vartotojo_id = ? AND knygos_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmoId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static boolean arVartotojasPamegesFilma(int vartotojoId, int filmoId) throws SQLException {
        boolean filmasVartotojoPamegtas = false;
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM pamegtos_knygos WHERE vartotojo_id = ? AND knygos_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        filmasVartotojoPamegtas = resultSet.isBeforeFirst();
        preparedStatement.close();
        connection.close();
        return filmasVartotojoPamegtas;
    }
}
