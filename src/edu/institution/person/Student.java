package edu.institution.person;

/**
 * Represents a student.
 */
public class Student extends Person {
    
    /**
     * Constructor.
     *
     * @param id        The id of this student
     * @param firstName The first name of this student
     * @param lastName  The last name of this student
     */
    public Student(final int id, final String firstName, final String lastName) {
        super(id, firstName, lastName, PersonType.STUDENT);
    }
}
