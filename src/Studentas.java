import com.sun.istack.internal.Nullable;

import java.sql.*;
import java.util.Scanner;

/**
 * Created by Lina on 2017.06.19.
 */
public class Studentas {
    // konstantos
    // prisijungimo
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/kcs";
    private static final String NAME = "root";
    private static final String PASSWORD = "";

    //informacines zinutes
    private static final String PRISIJUNGTA = "Prisijungta!";
    private static final String NEPRISIJUNGTA = "Neprisijungta???";
    private static final String DUOMENYS_NUSKAITYTI = "Duomenys nuskaityti!";
    private static final String SUKURTI_STATEMENT_NEPAVYKO = "Sukurti statement nepavyko???";
    private static final String DUOMENU_ATSPAUSDINTI_NEPAVYKO = "Duomenu atspausdinti nepavyko???";
    private static final String DUOMENYS_ATSPAUSDINTI = "Duomenys atspausdinti!";
    private static final String LENTELEJE_IRASU_NERA = "Lentelėje įrašų nėra!!!";
    private static final String SUKURIAMAS_NAUJAS_STUDENTAS = "Sukuriamas naujas studentas!";
    private static final String LENTELEJE_IRASU_YRA = "Lentelėje įrašų yra!";
    private static final String PATIKRINTI_AR_YRA_LENTELEJE_IRASU_NEPAVYKO = "Patikrinti ar yra lentelėje įrašų nepavyko!!!";
    private static final String IVESKITE_STUDENTO_VARDA = "IVESKITE STUDENTO VARDA";
    private static final String IVESKITE_STUDENTO_PAVARDE = "IVESKITE STUDENTO PAVARDE";
    private static final String IVESKITE_STUDENTO_TELEFONO_NUMERI = "IVESKITE STUDENTO TELEFONO NUMERI";
    private static final String IVESKITE_STUDENTO_EL_PASTA = "IVESKITE STUDENTO EL.PASTA";
    private static final String SUKURTAS_NAUJAS_STUDENTAS = "Sukurtas naujas studentas!";
    private static final String NEPAVYKO_IVESTI_NAUJO_STUDENTO = "Nepavyko ivesti naujo studento!!!";

    //uzklausa
    private static final String SELECT_FROM_STUDENTS_QUERY = "SELECT * FROM students";

    // kintamieji naudojami studentui sukurti
    private static String name;
    private static String surname;
    private static String phone;
    private static String email;

    // pagrindinis programos main metodas
    public static void main(String[] args) {
        // prisijungimas
        Connection connection = getConnection();
        // nuskaitomi duomenys
        ResultSet resultSet = getResultSet(connection, SELECT_FROM_STUDENTS_QUERY);
        // atspaudina duomenis i konsole
        print(resultSet);
        // patikrina ar yra irasu lenteleje ir sukuria nauja irasa jeigu nera ir atspausdina
        isTableHaveRecords(connection, resultSet, SELECT_FROM_STUDENTS_QUERY);
    }

    @Nullable // sis metodas gali grazinti null
    //metodas prisijungimui prie duomenu bazes,
    // patikrinama ar prisijungta
    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println(PRISIJUNGTA);
        } catch (SQLException e) {
            System.out.println(NEPRISIJUNGTA);
        }
        return connection;
    }

    @Nullable
    // metodas nuskaito duomenis
    private static ResultSet getResultSet(Connection connection, String query) {
        ResultSet resultSet = null;
        if (connection != null && query != null) {
            try {
                Statement st = connection.createStatement();
                resultSet = st.executeQuery(query);
                System.out.println(DUOMENYS_NUSKAITYTI);
            } catch (SQLException e) {
                System.out.println(SUKURTI_STATEMENT_NEPAVYKO);
            }
        }
        return resultSet;
    }

    //metodas atspausdina duomenis
    private static void print(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                while (resultSet.next()) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        sb.append(metaData.getColumnName(i)).append("=")
                                .append(resultSet.getObject(i)).append(" ");
                    }
                    System.out.println(sb.toString());
                }
            } catch (SQLException e) {
                System.out.println(DUOMENU_ATSPAUSDINTI_NEPAVYKO);
            }
            System.out.println(DUOMENYS_ATSPAUSDINTI);
        }
    }

    // metodas nustato ar yra irasu lenteleje, jei ne sukuria nauja irasa
    private static void isTableHaveRecords(Connection connection, ResultSet resultSet, String query) {
        resultSet = null;
        if (connection != null && query != null) {
            Statement st = null;
            try {
                st = connection.createStatement();
                resultSet = st.executeQuery(query);
                if (!resultSet.next()) {
                    System.out.println(LENTELEJE_IRASU_NERA);
                    System.out.println(SUKURIAMAS_NAUJAS_STUDENTAS);
                    update(connection); // nukreipiama i metoda, kad sukurtu nauja studenta
                } else {
                    System.out.println(LENTELEJE_IRASU_YRA);
                }
            } catch (SQLException e) {
                System.out.println(PATIKRINTI_AR_YRA_LENTELEJE_IRASU_NEPAVYKO);
            }
        }
    }

    // metodas - nuskaitomos reiksmes apie studenta - turi ivesti vartotojas
    private static void readStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(IVESKITE_STUDENTO_VARDA);
        name = scanner.next();
        System.out.println(IVESKITE_STUDENTO_PAVARDE);
        surname = scanner.next();
        System.out.println(IVESKITE_STUDENTO_TELEFONO_NUMERI);
        phone = scanner.next();
        System.out.println(IVESKITE_STUDENTO_EL_PASTA);
        email = scanner.next();
    }

    //metodas iraso nuskaitytus duomenis apie nauja studenta i lentele ir atspausdina
    private static void update(Connection connection) {
        try {
            readStudent(); // nukreipiama i studento
            String create_new_student_query = "INSERT INTO students(name, surname, phone, email)values('"
                    + name + "','" + surname + "','" + phone + "','" + email + "');";
            connection.createStatement().execute(create_new_student_query);
            System.out.println(SUKURTAS_NAUJAS_STUDENTAS);
            print(getResultSet(connection, SELECT_FROM_STUDENTS_QUERY));
        } catch (SQLException e) {
            System.out.println(NEPAVYKO_IVESTI_NAUJO_STUDENTO);
        }
    }
}



