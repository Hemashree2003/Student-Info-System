
import java.util.HashMap;
import java.util.Map;

public class Student {
    String studentId, username, password;
    String name, dob, address, contact, email, parentName, parentContact;
    Map<String, Integer> attendance;
    Map<String, Integer> marks;

    public Student(String studentId, String username, String password, String name, String dob, String address, String contact, String email, String parentName, String parentContact) {
        this.studentId = studentId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.parentName = parentName;
        this.parentContact = parentContact;
        this.attendance = new HashMap<>();
        this.marks = new HashMap<>();
    }

    public void setAttendance(String subject, int percent) {
        attendance.put(subject, percent);
    }

    public void setMarks(String subject, int score) {
        marks.put(subject, score);
    }
}