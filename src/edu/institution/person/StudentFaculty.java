package edu.institution.person;

/**
 * Represents a student who is also a faculty.
 */
public class StudentFaculty extends Person {

    /**
     * Constructor.
     *
     * @param id        The id of this student
     * @param firstName The first name of this student
     * @param lastName  The last name of this student
     */
    public StudentFaculty(final int id, final String firstName, final String lastName) {
        super(id, firstName, lastName, PersonType.STUDENT_FACULTY);
    }
}
