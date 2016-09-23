package edu.institution.clazz;

import edu.institution.person.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an academic class at an institution.
 */
public class AcademicClass {

    final int id;

    private final String name;

    private final Person faculty;

    // A faculty member could be a student too
    private final List<Person> students;

    public AcademicClass(final int id, final String name, final Person person) {
        if (!person.isFaculty()) {
            throw new IllegalArgumentException("The supplied person is not a person!: " + person);
        }

        this.id = id;
        this.name = name;
        this.faculty = person;
        this.students = new ArrayList<Person>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Person getFaculty() {
        return faculty;
    }

    public List<Person> getStudents() {
        return students;
    }

    public void addStudent(final Person person) {
        this.students.add(person);
    }

    /**
     * Checks whether the supplied person is a student in this class.
     *
     * @param person The person to check.
     * @return true if the person is a student of this class, false otherwise.
     */
    public boolean hasStudent(final Person person) {
        for (final Person student : students) {
            if (student.equals(person)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AcademicClass{");
        sb.append("name='").append(name).append('\'');
        sb.append(", faculty=").append(faculty);
        sb.append('}');
        return sb.toString();
    }
}
