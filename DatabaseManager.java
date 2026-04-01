import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:student_info.db";

    public static void setupDatabase() {
        String[] setupSQL = {
            "CREATE TABLE IF NOT EXISTS admins (username TEXT PRIMARY KEY, password TEXT NOT NULL);",
            "CREATE TABLE IF NOT EXISTS teachers (username TEXT PRIMARY KEY, password TEXT NOT NULL, name TEXT NOT NULL);",
            "CREATE TABLE IF NOT EXISTS students (studentId TEXT PRIMARY KEY, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, name TEXT, dob TEXT, address TEXT, contact TEXT, email TEXT, parentName TEXT, parentContact TEXT);",
            "CREATE TABLE IF NOT EXISTS marks (studentId TEXT, subject TEXT, score INTEGER, PRIMARY KEY(studentId, subject));",
            "CREATE TABLE IF NOT EXISTS attendance (studentId TEXT, subject TEXT, percentage INTEGER, PRIMARY KEY(studentId, subject));",
            "INSERT OR IGNORE INTO admins (username, password) VALUES ('admin', 'adminpass');",
            "INSERT OR IGNORE INTO teachers (username, password, name) VALUES ('teacher', 'teacherpass', 'Alice Jones');"
        };

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            for (String sql : setupSQL) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println("Database setup error: " + e.getMessage());
        }
    }
    
    //all other methods like addStudent, validateLogin, getAllStudents, etc. are UNCHANGED ...
    public static boolean addStudent(Student student) {
        String sql = "INSERT INTO students(studentId, username, password, name, dob, address, contact, email, parentName, parentContact) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.studentId);
            pstmt.setString(2, student.username);
            pstmt.setString(3, student.password);
            pstmt.setString(4, student.name);
            pstmt.setString(5, student.dob);
            pstmt.setString(6, student.address);
            pstmt.setString(7, student.contact);
            pstmt.setString(8, student.email);
            pstmt.setString(9, student.parentName);
            pstmt.setString(10, student.parentContact);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public static boolean validateLogin(String userType, String username, String password) {
        String tableName = userType.toLowerCase() + "s";
        String sql = "SELECT password FROM " + tableName + " WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getString("password").equals(password);
        } catch (SQLException e) {
            System.out.println("Login validation error: " + e.getMessage());
            return false;
        }
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                    rs.getString("studentId"), rs.getString("username"), rs.getString("password"),
                    rs.getString("name"), rs.getString("dob"), rs.getString("address"),
                    rs.getString("contact"), rs.getString("email"), rs.getString("parentName"),
                    rs.getString("parentContact")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all students: " + e.getMessage());
        }
        return students;
    }

    public static Student getStudent(String username) {
        String sql = "SELECT * FROM students WHERE username = ?";
        Student student = null;
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                student = new Student(
                    rs.getString("studentId"), rs.getString("username"), rs.getString("password"),
                    rs.getString("name"), rs.getString("dob"), rs.getString("address"),
                    rs.getString("contact"), rs.getString("email"), rs.getString("parentName"),
                    rs.getString("parentContact"));
                
                loadMarks(conn, student);
                loadAttendance(conn, student);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }
        return student;
    }
    
    private static void loadMarks(Connection conn, Student student) throws SQLException {
        String sql = "SELECT subject, score FROM marks WHERE studentId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                student.setMarks(rs.getString("subject"), rs.getInt("score"));
            }
        }
    }

    private static void loadAttendance(Connection conn, Student student) throws SQLException {
        String sql = "SELECT subject, percentage FROM attendance WHERE studentId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                student.setAttendance(rs.getString("subject"), rs.getInt("percentage"));
            }
        }
    }
    
    public static void saveMarksAndAttendance(String studentId, String subject, int score, int attendance) {
        String marksSql = "INSERT OR REPLACE INTO marks (studentId, subject, score) VALUES (?, ?, ?)";
        String attendanceSql = "INSERT OR REPLACE INTO attendance (studentId, subject, percentage) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (PreparedStatement pstmtMarks = conn.prepareStatement(marksSql)) {
                pstmtMarks.setString(1, studentId);
                pstmtMarks.setString(2, subject);
                pstmtMarks.setInt(3, score);
                pstmtMarks.executeUpdate();
            }
            try (PreparedStatement pstmtAttendance = conn.prepareStatement(attendanceSql)) {
                pstmtAttendance.setString(1, studentId);
                pstmtAttendance.setString(2, subject);
                pstmtAttendance.setInt(3, attendance);
                pstmtAttendance.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error saving marks/attendance: " + e.getMessage());
        }
    }

    // --- NEW METHOD 1 ---
    // Fetches all marks for a specific subject. Returns a Map of (studentId -> score).
    public static Map<String, Integer> getMarksForSubject(String subject) {
        Map<String, Integer> marksMap = new HashMap<>();
        String sql = "SELECT studentId, score FROM marks WHERE subject = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                marksMap.put(rs.getString("studentId"), rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching marks for subject: " + e.getMessage());
        }
        return marksMap;
    }

    // --- NEW METHOD 2 ---
    // Fetches all attendance for a specific subject. Returns a Map of (studentId -> percentage).
    public static Map<String, Integer> getAttendanceForSubject(String subject) {
        Map<String, Integer> attendanceMap = new HashMap<>();
        String sql = "SELECT studentId, percentage FROM attendance WHERE subject = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                attendanceMap.put(rs.getString("studentId"), rs.getInt("percentage"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching attendance for subject: " + e.getMessage());
        }
        return attendanceMap;
    }
}