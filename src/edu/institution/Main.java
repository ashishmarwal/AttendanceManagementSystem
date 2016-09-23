package edu.institution;

import edu.institution.attendance.AttendanceRecord;
import edu.institution.attendance.AttendanceRegistrar;
import edu.institution.clazz.AcademicClass;
import edu.institution.person.Faculty;
import edu.institution.person.Person;
import edu.institution.person.Student;
import edu.institution.person.StudentFaculty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String dateFormatString = "dd-MM-yyyy";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

    // create a scanner so we can read the command-line input
    final static Scanner scanner = new Scanner(System.in);

    final static AttendanceRegistrar attendanceRegistrar = new AttendanceRegistrar();

    final static List<AcademicClass> academicClasses = new ArrayList<AcademicClass>();
    final static Map<Integer, Person> personsMap = new HashMap<Integer, Person>();
    private static final String QUIT = "quit";

    public static void main(final String[] args) {
        buildTestData();
        printWelcomeMessage();

        if (scanner == null) {
            System.err.println("No scanner. Can't proceed any further!");
            System.exit(1);
        }

        final Person loggedOnUser = readPersonIdUntilPersonRetrieved();
        final AcademicClass selectedClass = selectClass(loggedOnUser);

        executeOperations(loggedOnUser, selectedClass);
    }

    private static void executeOperations(final Person loggedOnUser, final AcademicClass selectedClass) {
        do {
            displayOperationsMenu(loggedOnUser);
            final int selectedOption = readNumericUserInput();

            if ((!loggedOnUser.isFaculty() && selectedOption != 1) || (loggedOnUser.isFaculty() && selectedOption > 4)) {
                System.err.println("Invalid option selected. Please select a valid option");
            } else {
                try {
                    switch (selectedOption) {
                        case 1: {
                            final List<AttendanceRecord> attendanceRecords;

                            if (loggedOnUser.isFaculty()) {
                                attendanceRecords = attendanceRegistrar.viewAllAttendanceRecords(selectedClass, loggedOnUser);
                            } else {
                                // print my records for a non facult user
                                attendanceRecords = attendanceRegistrar.viewAttendanceRecords(loggedOnUser, selectedClass, loggedOnUser);
                            }

                            printAttendanceRecords(attendanceRecords);
                            break;
                        }
                        case 2: {
                            final Person student = readPersonIdUntilPersonRetrieved();
                            final List<AttendanceRecord> attendanceRecords = attendanceRegistrar.viewAttendanceRecords(student, selectedClass, loggedOnUser);

                            printAttendanceRecords(attendanceRecords);
                            break;
                        }
                        case 3: {
                            final Person student = readPersonIdUntilPersonRetrieved();

                            System.out.println("Please enter the date (format: " + dateFormatString + "): ");
                            final Date date = readDateUserInput();

                            System.out.println("Student was/is present on the day specified (Yes/No)?: ");
                            final boolean present = readBooleanUserInput();

                            attendanceRegistrar.createAttendanceRecord(student, date, present, selectedClass, loggedOnUser);
                            break;
                        }
                        case 4: {
                            System.err.println("Sorry, this feature is not yet implemented!");
                        }
                        default:
                            break;
                    }
                } catch (Exception e) {
                    // log the error and continue
                    System.err.println("Error: " + e.getMessage());
                }
            }

        } while(true); // Inifinitely execute this code unless the user ends the program by typig quit
    }

    private static void printAttendanceRecords(final List<AttendanceRecord> attendanceRecords) {
        if (attendanceRecords != null && attendanceRecords.size() > 0) {
            for (final AttendanceRecord attendanceRecord : attendanceRecords) {
                System.out.println(attendanceRecord);
            }
        }
    }

    private static void displayOperationsMenu(final Person loggedOnPerson) {
        System.out.println("\n\nPlease select an operation:");
        System.out.println("\n\nID\tOperation description.\n");

        if (loggedOnPerson.isFaculty()) {
            System.out.println("1\tList all attendance records.");
            System.out.println("2\tList a specific student's  attendance records.");
            System.out.println("3\tAdd an attendance record");
            System.out.println("4\tModify an attendance record");
        } else {
            System.out.println("1\tList my attendance records.");
        }

        System.out.println("Quit\tExit from the system.\n\n\n");
    }

    private static AcademicClass selectClass(final Person loggedOnUser) {
        AcademicClass selectedClass = null;

        do {
            System.out.print("Please select a class by entering its id. Available classes:");
            listClasses();

            selectedClass = verifyClass(loggedOnUser);
        } while (selectedClass == null);

        System.out.println("\n***** Welcome to '" + selectedClass.getName() + "' *******\n\n");

        return selectedClass;
    }

    private static void listClasses() {
        for (final AcademicClass academicClass : academicClasses) {
            System.out.println("ID: " + academicClass.getId() + "\tName: " + academicClass.getName());
        }
    }

    private static Person readPersonIdUntilPersonRetrieved() {
        Person loggedOnUser = null;

        do {
            System.out.println("Enter person id: ");
            loggedOnUser = readPersonIdToRetrievePerson();
        } while (loggedOnUser == null);

        return loggedOnUser;
    }


    static Person readPersonIdToRetrievePerson() {
        final int loginIdInt = readNumericUserInput();

        for (final Integer personId : personsMap.keySet()) {
            if (personId.intValue() == loginIdInt) {
                return personsMap.get(personId);
            }
        }

        System.err.println("Couldn't find a person with the provided id. Please try again!");
        return null;
    }

    private static AcademicClass verifyClass(final Person loggedOnPerson) {

        final int classIdInt = readNumericUserInput();

        for (final AcademicClass academicClass : academicClasses) {
            if (academicClass.getId() == classIdInt && personBelongsToClassInSomeWay(academicClass, loggedOnPerson)) {
                return academicClass;
            }
        }

        System.err.println("Either a class with the supplied ID doesn't exist or the logged on user is not related to the class. " +
                "A person is said to be related to class if he/she is the faculty of the class or if he/she is one of the " +
                "students of the class. Please try again!");

        return null;
    }

    private static boolean personBelongsToClassInSomeWay(final AcademicClass academicClass, final Person loggedOnPerson) {
        return loggedOnPerson.equals(academicClass.getFaculty()) || academicClass.getStudents().contains(loggedOnPerson);
    }

    private static void printWelcomeMessage() {
        System.out.println("=================================================================");
        System.out.println("------------ WELCOME TO ATTENDANCE MANAGEMENT SYSTEM ------------");
        System.out.println("=================================================================");
        System.out.println("Type quit to exit the system anytime.");
        System.out.println("Please login...");
    }

    public static Date readDateUserInput() {
        final String userInputString = readUserInput();
        Date date = null;

        do {

            try {
                date = dateFormat.parse(userInputString);
            } catch (ParseException pe) {
                throw new IllegalArgumentException("Supplied date not in valid format. Plase provide in " + dateFormatString + " format. e.g. 23-09-2016");
            }
        } while (date == null);

        return date;
    }

    public static int readNumericUserInput() {
        final String userInputString = readUserInput();
        int userInputInt = 0;

        do {
            try {
                userInputInt = Integer.parseInt(userInputString);
            } catch (NumberFormatException ne) {
                System.err.println("You must enter a numeric value. Please try again!");
            }
        } while (userInputInt == 0);

        return userInputInt;
    }

    public static boolean readBooleanUserInput() {
        final String userInputString = readUserInput();
        String yesNoString = null;

        do {
            // Only accept yes or no strings
            yesNoString = (userInputString.equalsIgnoreCase("Yes") || userInputString.equalsIgnoreCase("No")) ? userInputString : null;

            if (yesNoString == null) {
                System.err.println("You must enter either 'Yes' or 'No'. Please try again!");
            }
        } while (yesNoString == null);

        return yesNoString.equalsIgnoreCase("Yes") ? true : false;
    }

    private static String readUserInput() {
        String userInput = scanner.next();
        userInput = userInput != null ? userInput.trim() : "";

        if (userInput.trim().equalsIgnoreCase(QUIT)) {
            System.out.println("Exiting the system! Good Bye!!!");
            System.exit(0);
        }

        return userInput;
    }

    private static Date parseDate(final String dateStr) {
        final Date date;

        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Supplied date not in valid format. Plase provide in " + dateFormatString + " format. e.g. 23-09-2016");
        }
        return date;
    }

    private static void buildTestData() {
        // Create a few faculties
        final Person student1 = new Student(1, "S1-FName", "S1-LName");
        final Person student2 = new Student(2, "S2-FName", "S2-LName");
        final Person student3 = new Student(3, "S3-FName", "S3-LName");
        final Person student4 = new Student(4, "S4-FName", "S4-LName");

        final Person faculty1 = new Faculty(5, "F1-FName", "F1-LName");
        final Person faculty2 = new Faculty(6, "F2-FName", "F2-LName");

        final Person studentFaculty1 = new StudentFaculty(7, "SF1-FName", "SF1-LName");

        personsMap.put(1, student1);
        personsMap.put(2, student2);
        personsMap.put(3, student3);
        personsMap.put(4, student4);
        personsMap.put(5, faculty1);
        personsMap.put(6, faculty2);
        personsMap.put(7, studentFaculty1);

        // Create a few classes
        final AcademicClass academicClass1 = new AcademicClass(1, "Class 1", faculty1);
        final AcademicClass academicClass2 = new AcademicClass(2, "Class 2", faculty2);
        final AcademicClass academicClass3 = new AcademicClass(3, "Class StudentFaculty", studentFaculty1);

        academicClasses.add(academicClass1);
        academicClasses.add(academicClass2);
        academicClasses.add(academicClass3);

        academicClass1.addStudent(student1);
        academicClass1.addStudent(student2);
        academicClass1.addStudent(student3);

        academicClass2.addStudent(student4);
        academicClass2.addStudent(studentFaculty1);

        academicClass3.addStudent(student1);
    }
}
