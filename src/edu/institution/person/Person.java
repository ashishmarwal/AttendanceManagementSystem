package edu.institution.person;

/**
 * Represents a person.
 */
public abstract class Person implements Comparable<Person> {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final PersonType type;

    /**
     * Constructor.
     *
     * @param id The id of this person
     * @param firstName The first name of this person
     * @param lastName The last name of this person
     * @param type The type of this person
     */
    public Person(final int id, final String firstName, final String lastName, final PersonType type) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }

    /**
     * Retrieves the ID of this person.
     *
     * @return the int value of the id of this person.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this person.
     *
     * @return a string
     */
    public String getName() {
        String name = "";

        if (firstName != null && firstName.trim().length() > 0) {
            name += firstName.trim();
        }

        if (lastName != null && lastName.trim().length() > 0) {
            name += " ";
            name += lastName.trim();
        }

        return name;
    }

    /**
     * Checks whether this person is a faculty member.
     * @return true if this person is a faculty member, false otherwise.
     */
    public boolean isFaculty() {
        return type.equals(PersonType.FACULTY) || type.equals(PersonType.STUDENT_FACULTY);
    }

    /**
     * Checks whether this person is a student.
     * @return true if this person is a student, false otherwise.
     */
    public boolean isStudent() {
        return type.equals(PersonType.STUDENT);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Person person = (Person) o;

        return id == person.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(final Person otherPerson) {
        if (otherPerson == null) {
            return -1;
        }

        return new Integer(this.getId()).compareTo(new Integer(otherPerson.getId()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id);
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
