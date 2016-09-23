package edu.institution.person;

/**
 * Represents a faculty member.
 */
public class Faculty extends Person {

    /**
     * Constructor.
     *
     * @param id        The id of this faculty
     * @param firstName The first name of this faculty
     * @param lastName  The last name of this faculty
     */
    public Faculty(final int id, final String firstName, final String lastName) {
        super(id, firstName, lastName, PersonType.FACULTY);
    }
}
