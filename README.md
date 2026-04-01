# 🎓 Student Information System

## 📌 Description

The **Student Information System** is a Java-based desktop application developed using **Java Swing** and **SQLite database**.
It allows Admin, Teacher, and Students to manage and access student-related information efficiently.

---

## 🚀 Features

### 👨‍💼 Admin

* Add new students
* View student list

### 👩‍🏫 Teacher

* Select subject
* Enter marks and attendance
* Save student records

### 👩‍🎓 Student

* View marks and grades
* View attendance

---

## 🛠️ Technologies Used

* Java (Swing GUI)
* SQLite Database
* JDBC (Java Database Connectivity)

---

## 🗄️ Database

* SQLite database (`student_info.db`)
* Tables:

  * Students
  * Admins
  * Teachers
  * Marks
  * Attendance

---

## ▶️ How to Run

### Step 1: Compile the program

javac *.java

### Step 2: Run the application

java -cp ".;sqlite-jdbc-3.45.1.0.jar" StudentInfoSystem

---

## 🔐 Default Login Credentials

### Admin

* Username: admin
* Password: adminpass

### Teacher

* Username: teacher
* Password: teacherpass

---

## 📁 Project Structure

* Student.java
* Teacher.java
* Admin.java
* DatabaseManager.java
* StudentInfoSystem.java
* AdminDashboard.java
* TeacherView.java
* StudentDashboard.java
* MarksView.java
* AttendanceView.java

---

## 🎯 Learning Outcomes

* Developed GUI using Java Swing
* Implemented database using SQLite
* Used JDBC for database connectivity
* Applied OOP concepts in Java

---

## 📌 Author

**Hemashree H S**

---

## ⭐ Conclusion

This project demonstrates a complete student management system with role-based access and real-time data handling using Java and SQLite.

---
