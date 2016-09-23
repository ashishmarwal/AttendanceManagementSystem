package edu.institution.attendance;

import edu.institution.person.Person;

import java.util.Date;

/**
 * Represents an individual attendance record. This is really a data holder. Operations on it are controlled by {@link AttendanceRegistrar}.
 */
public class AttendanceRecord {
    /** properties set to be final can only be initialized once */
    private final int id;
    private final Person person;
    private final Date date;

    private boolean present;

    /**
     * Constructor.
     *
     * @param person The person this record is for.
     * @param date The day this attendance record is for.
     * @param present Whether the person is present.
     */
    public AttendanceRecord(final Person person, final Date date, final boolean present) {
        this.id = AttendanceRecordSequence.nextVal();

        this.date = date;
        this.person = person;
        this.present = present;
    }

    public Date getDate() {
        return date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(final boolean present) {
        this.present = present;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AttendanceRecord{");
        sb.append("person=").append(person.getName());
        sb.append("\tdate=").append(date);
        sb.append("\tpresent=").append(present ? "Yes" : "No");
        sb.append('}');
        return sb.toString();
    }
}
