package com.example.Learner.Management.System.Config;

import com.example.Learner.Management.System.Entities.Student;
import com.example.Learner.Management.System.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleRunner  implements CommandLineRunner {
    private final StudentService studentService;
    private final Scanner scanner;

    public ConsoleRunner(StudentService service){
        this.studentService = service;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void run(String... args){
        int select = -1;

        do {
            System.out.println("\n--- Students Management (Grade 10) ---");
            System.out.println("1. Display All Students");
            System.out.println("2. Add Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Average Score");
            System.out.println("6. Search Student by Name");
            System.out.println("0. Exit");
            System.out.print("Select a number: ");

            String userInput = scanner.nextLine();

            if (!userInput.matches("\\d+")) {
                System.out.println("Please enter a valid number (positive only).");
                continue;
            }

            select = Integer.parseInt(userInput);

            switch (select) {
                case 1 -> displayAllStudent();
                case 2 -> addStudent();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> calculateAverage();
                case 6 -> searchStudent();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (select != 0);
    }

    private String capitalizeName(String name) {
        name = name.trim().toLowerCase();
        if (name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
    private void displayAllStudent() {
        System.out.println("\n--- All Students ---");

        List<Student> studentList = studentService.getAllStudents();

        if(studentList.isEmpty()) {
            System.out.println("------------------------------------------------");
            System.out.printf("|\t%-43s|\n", "No students available");
            System.out.println("------------------------------------------------");
        } else {
            System.out.println("-------------------------------------------------------------");
            System.out.printf("| %-10s | %-20s | %-10s | %-8s |\n", "ID", "NAME", "MARKS", "GRADE");
            System.out.println("-------------------------------------------------------------");

            studentList.forEach(student ->
                    System.out.printf("| %-10d | %-20s | %-10d | %-8s |\n",
                            student.getId(),
                            capitalizeName(student.getName()),
                            student.getScore(),
                            student.getGrade())
            );

            System.out.println("-------------------------------------------------------------");
        }
    }

    private  void addStudent(){
        while (true) {
            String name;
            int score;

            // Validate name input
            while (true) {
                System.out.print("Enter name (or 0 to cancel): ");
                String nameInput = scanner.nextLine().trim();

                if (nameInput.equals("0")) {
                    System.out.println("Cancelled. Returning to main menu.");
                    return;
                }

                if (nameInput.isEmpty() || !nameInput.matches("[a-zA-Z ]+")) {
                    System.out.println("Invalid name. Use letters only.");
                } else if (nameInput.length() < 2) {
                    System.out.println("Name too short.");
                } else if (nameInput.length() > 30) {
                    System.out.println("Name too long. Please keep it under 30 characters.");
                } else if (nameInput.contains("  ")) { // Beginner-friendly check
                    System.out.println("Remove extra spaces between names.");
                } else if (nameInput.startsWith(" ") || nameInput.endsWith(" ")) {
                    System.out.println("Name cannot start or end with a space.");
                } else {
                    final String validatedName = nameInput;
                    boolean exists = studentService.getAllStudents().stream()
                            .anyMatch(s -> s.getName().equalsIgnoreCase(validatedName));
                    {
                        name = validatedName;
                        if (exists) {
                            System.out.println("Student " + validatedName + " already exists.");
                            continue;
                        }
                        break;
                    }
                }
            }

            // Capitalize the name properly before saving
            name = capitalizeName(name);

            // Validate score input
            while (true) {
                System.out.print("Enter score (0–100): ");
                String scoreInput = scanner.nextLine();
                try {
                    score = Integer.parseInt(scoreInput);
                    if (score < 0 || score > 100) {
                        System.out.println("Score must be between 0 and 100.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
            // Add student to DB
            // Student student = new Student(, name, score, 10);
            studentService.saveStudent(new Student(name, score));

            // Ask if user wants to add another
            int again;
            while (true) {
                System.out.println("Do you want to add another student?");
                System.out.println("1. Yes");
                System.out.println("2. No (Return to main menu)");
                System.out.print("Choose (1 or 2): ");
                String inputChoice = scanner.nextLine();
                try {
                    again = Integer.parseInt(inputChoice);
                    if (again == 1 || again == 2) break;
                    else System.out.println("Please enter 1 or 2.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            if (again == 2) {
                System.out.println("Return to main menu.");
                break;
            }
        }
    }

    private void updateStudent() {
        System.out.println("\n--- Update Student ---");


        String newName ;
        int newScore ;
        boolean repeat;

        do {
            displayAllStudent();

            // Get student ID to update
            Long id = null;
            while (id == null) {
                System.out.print("Enter student ID to update (or 0 to cancel): ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equals("0")) {
                    System.out.println("Update cancelled.");
                    return;
                }

                try {
                    id = Long.parseLong(idInput);
                    if (studentService.getStudentById(id) == null) {
                        System.out.println("------------------------------------------------");
                        System.out.printf("|\t%-43s|\n", "Student not found.");
                        System.out.println("------------------------------------------------");
                        id = null; // Reset to continue loop
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid ID number.");
                }
            }


            Student student1 = studentService.getStudentById(id);

            if (student1 == null) {
                System.out.println("Error: Student not found!");
                return;
            }

            // Choose what to update
            int choice;
            while (true) {
                System.out.print("What do you want to update?");
                System.out.println("\n1. Name");
                System.out.println("2. Score");
                System.out.println("3. Both Name and Score");
                System.out.println("4. Cancel");
                System.out.print("Choose (1-4): ");
                String inputChoice = scanner.nextLine().trim();

                try {
                    choice = Integer.parseInt(inputChoice);
                    if (choice >= 1 && choice <= 4) break;
                    System.out.println("Please choose between 1 and 4.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            if (choice == 4) {
                System.out.println("Update cancelled.");
                return;
            }

            // Handle name update
            if (choice == 1 || choice == 3) {

                while (true) {
                    System.out.print("Current name: " + student1.getName());
                    System.out.print("\nEnter new name (or leave blank to cancel): ");
                    newName = scanner.nextLine().trim();

                    if (newName.isEmpty()) {
                        System.out.println("Name update cancelled.");
                        break;
                    }

                    if (!newName.matches("[a-zA-Z ]+")) {
                        System.out.println("Invalid name. Use only letters and spaces.");
                    } else {
                        newName = capitalizeName(newName);
                        if (student1.getName().equalsIgnoreCase(newName)) {
                            System.out.println("No changes made. Name is already: " + newName);
                        } else {
                            String oldName = student1.getName();
                            student1.setName(newName);
                            System.out.println("Name updated successfully: " + oldName + " → " + newName);
                        }
                        break;
                    }
                }
            }

            // Handle score update
            if (choice == 2 || choice == 3) {
                while (true) {
                    System.out.print("Current score: " + student1.getScore());
                    System.out.print("\nEnter new score (0-100, or leave blank to cancel): ");
                    String scoreInput = scanner.nextLine().trim();

                    if (scoreInput.isEmpty()) {
                        System.out.println("Score update cancelled.");
                        break;
                    }

                    try {
                        newScore = Integer.parseInt(scoreInput);
                        if (newScore >= 0 && newScore <= 100) {
                            if (student1.getScore() == newScore) {
                                System.out.println("No changes made. Score is already: " + newScore);
                            } else {
                                int oldScore = student1.getScore();

                                student1.setScore(newScore);
                                System.out.println("------------------------------------------");
                                System.out.println("\tScore updated successfully.");
                                System.out.println("------------------------------------------");
                                System.out.printf("| Old: %-5d  %2s  New: %-15d |\n", oldScore, "→", newScore);
                                System.out.println("------------------------------------------");
                                // System.out.println("Score updated successfully: " + oldScore + " → " + newScore);
                            }
                            break;
                        } else {
                            System.out.println("Score must be between 0 and 100.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
            }

            // Save the updated student
            System.out.println("About to save updated student: " + student1.getName());
            // Student student1 = new Student(id, newName, newScore, 10);
            studentService.updateStudent(student1);

            // Display updated list
            displayAllStudent();

            // Ask if user wants to update another student
            System.out.print("Update another student? (Y/N): ");
            String againInput = scanner.nextLine().trim().toLowerCase();
            repeat = againInput.equals("y") || againInput.equals("yes");

        } while (repeat);
    }

    private void deleteStudent()  {
        boolean repeat;
        do {
            displayAllStudent();

            long id;
            while (true) {
                System.out.print("Enter student ID to delete (or 0 to cancel): ");
                String idInput = scanner.nextLine();
                try {
                    id = Integer.parseInt(idInput);
                    if (id == 0) {
                        System.out.println("Cancelled. Returning to main menu.");
                        return;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            int confirmDelete;
            while (true) {
                System.out.println("Are you sure you want to delete student ID " + id + "?");
                System.out.println("1. Yes, delete");
                System.out.println("2. No, cancel and return to main menu");
                System.out.print("Choose (1-2): ");
                String confirmInput = scanner.nextLine();
                try {
                    confirmDelete = Integer.parseInt(confirmInput);
                    if (confirmDelete == 1 || confirmDelete == 2) break;
                    else System.out.println("Please choose 1 or 2.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            if (confirmDelete == 2) {
                System.out.println("Cancelled. Returning to main menu.");
                return;
            }


            if (!studentService.deleteStudent(id)){
                System.out.println("-------------------------------------------------");
                System.out.printf("|\t%-5s  %-17s|\n", "No student found with ID:", id);
                System.out.println("-------------------------------------------------");

            }


            int again;
            while (true) {
                System.out.println("Do you want to delete another student?");
                System.out.println("1. Yes");
                System.out.println("2. No (Return to Main Menu)");
                System.out.print("Choose (1-2): ");
                String againInput = scanner.nextLine();

                try {
                    again = Integer.parseInt(againInput);
                    if (again == 1){
                        break;
                    }
                    else if(again == 2){
                        System.out.println("Returning to main menu.");
                        break;
                    }
                    else{
                        System.out.println("Please choose 1 or 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }

            repeat = (again == 1);
        } while (repeat);
    }

    private void calculateAverage() {
        List<Student> students = studentService.getAllStudents();
        double average = studentService.calculateAvg();
        // System.out.printf("\nAverage score of all students: %.2f%n", average);
        System.out.println("----------------------------------");
        System.out.println("| AVERAGE SCORE | NO OF STUDENTS |");
        System.out.println("----------------------------------");
        System.out.printf("| %.2f%%    \t| %-15d|\n", average, students.size());
        System.out.println("----------------------------------");
    }


    private void searchStudent(){
        boolean searchAgain = true;

        do {
            System.out.println("\n\t--- Search Student ---");
            System.out.print("Enter student name to search: ");
            String searchTerm = scanner.nextLine().toLowerCase().trim();

            if (searchTerm.isEmpty()) {
                System.out.println("Search term cannot be empty.");
                continue;
            }


            List<Student> students = studentService.getStudentsByName(searchTerm);

            if (students.isEmpty()) {
                System.out.println("----------------------------------------------");
                System.out.println("\tNo matching students found.");
                System.out.println("----------------------------------------------");
                System.out.println("| ID  | NAME           | GRADE | SCORE/MARKS |");
                System.out.println("----------------------------------------------");
                System.out.printf("| %-3d | %-14s | %-5d | \t%-8d |%n", 0, capitalizeName(searchTerm), 0, 0);
                System.out.println("----------------------------------------------");
            } else {
                System.out.println("----------------------------------------------");
                System.out.println("| ID  | NAME           | GRADE | SCORE/MARKS |");
                System.out.println("----------------------------------------------");
                for (Student student : students) {
                    System.out.printf("| %-3d | %-14s | %-5d | \t%-8d |%n",
                            student.getId(),
                            capitalizeName(student.getName()),
                            student.getGrade(),
                            student.getScore());
                }
                System.out.println("----------------------------------------------");
            }

            while (true) {
                System.out.print("Do you want to search again? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();

                if (choice.equals("y")) {
                    break;
                } else if (choice.equals("n")) {
                    System.out.println("Returning to main menu.");
                    searchAgain = false;
                    break;
                } else {
                    System.out.println("Please enter (y/n).");
                }
            }

        } while (searchAgain);
    }
}

