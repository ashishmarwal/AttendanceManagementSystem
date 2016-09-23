package edu.institution.attendance;

import edu.institution.clazz.AcademicClass;
import edu.institution.person.Person;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A keeper for class attendance records.
 */
public class AttendanceRegistrar {
    private static final Map<Person, List<AttendanceRecord>> attendanceRegister = new TreeMap<Person, List<AttendanceRecord>>();

    public List<AttendanceRecord> viewAllAttendanceRecords(final AcademicClass academicClass, final Person accessor) throws Exception {
        // Only a student of this class or a faculty of this class can access the attendance records of a class
        if (isClassFaculty(accessor, academicClass)) {
            final List<AttendanceRecord> combinedAttendanceRecords = new ArrayList<AttendanceRecord>();

            for (final List<AttendanceRecord> studentAttendanceRecords : attendanceRegister.values()) {
                combinedAttendanceRecords.addAll(studentAttendanceRecords);
            }

            System.out.println(">> Found " + combinedAttendanceRecords.size() + " attendance records for class: " + academicClass.getName());

            return combinedAttendanceRecords;
        } else {
            throw new UnsupportedOperationException("Only the class faculty can see all of its attendance records");
        }
    }

    public List<AttendanceRecord> viewAttendanceRecords(final Person person, final AcademicClass academicClass, final Person accessor) throws Exception {
        // Only a student of this class or a faculty of this class can access the attendance records of a class
        if ( (accessor.isFaculty() && academicClass.getFaculty().equals(accessor)) ||
                (person.equals(accessor) && academicClass.hasStudent(accessor))) {
            final List<AttendanceRecord> attendanceRecords = attendanceRegister.get(person);

            if (attendanceRecords == null || attendanceRecords.size() == 0) {
                throw new Exception("No attendance records found for: " + person);
            }

            System.out.println(">> Found " + attendanceRecords.size() + " attendance records for student: " + person.getName());

            return attendanceRecords;
        } else {
            throw new UnsupportedOperationException("Only the class faculty or the student himself can see all of a student's attendance records");
        }
    }

    public boolean createAttendanceRecord(final Person person, final Date date, final boolean present, final AcademicClass academicClass, final Person creator) throws Exception {

        if (!isClassFaculty(creator, academicClass)) {
            throw new UnsupportedOperationException("Only the faculty of a class is allowed to create an attendance record for it!");
        }

        if (creator.equals(person)) {
            throw new UnsupportedOperationException("One can't create his/her own attendance record!");
        }

        if (findExistingRecord(person, date) != null) {
            throw new IllegalArgumentException("Attendance records already exists for the given student & date. Please select the modify operation instead!");
        }

        List<AttendanceRecord> studentAttendanceRecords = attendanceRegister.get(person);

        if (studentAttendanceRecords == null) {
            studentAttendanceRecords = new ArrayList<AttendanceRecord>();
            attendanceRegister.put(person, studentAttendanceRecords);
        }

        final AttendanceRecord attendanceRecord = new AttendanceRecord(person, date, present);
        studentAttendanceRecords.add(attendanceRecord);

        System.out.println("Successfully created attendence record: " + attendanceRecord);

        return true;
    }

    public boolean modifyAttendanceRecord(final Person person, final Date date, final boolean present, final AcademicClass academicClass, final Person modifier) throws Exception {

        if (!isClassFaculty(modifier, academicClass)) {
            throw new UnsupportedOperationException("Only the faculty member of a class is allowed to update it's attendance records!");
        }

        if (modifier.equals(person)) {
            throw new UnsupportedOperationException("One can't modify his/her own attendance record!");
        }

        final AttendanceRecord existingAttendanceRecord = findExistingRecord(person, date);

        if (existingAttendanceRecord == null) {
            throw new IllegalArgumentException("No existing attendance record for the given student & date. Please select the create operation instead!");
        }

        existingAttendanceRecord.setPresent(present);

        System.out.println("Successfully updated attendence record to: " + existingAttendanceRecord);

        return true;
    }

    private boolean isClassFaculty(final Person modifier, final AcademicClass academicClass) {
        return modifier.isFaculty() || academicClass.getFaculty().equals(modifier);
    }

    private boolean parsePresentStatus(String present) {
        present = present != null ? present.trim() : null;

        if (present == null && !(present.equalsIgnoreCase("Yes") || present.equalsIgnoreCase("No"))) {
            throw new IllegalArgumentException("Invalid value for 'present' status. Please provide 'Yes' or 'No'!");
        }

        return Boolean.parseBoolean(present);
    }

    private AttendanceRecord findExistingRecord(final Person person, final Date date) throws ParseException {
        final List<AttendanceRecord> studentAttendanceRecords = attendanceRegister.get(person);

        if (studentAttendanceRecords == null) {
            return null;
        }

        for (final AttendanceRecord attendanceRecord : studentAttendanceRecords) {
            if (attendanceRecord.getDate().equals(date)) {
                return attendanceRecord;
            }
        }

        return  null;
    }
}
