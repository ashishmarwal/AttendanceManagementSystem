package edu.institution.attendance;

/**
 * A utility to keep attendance record sequences.
 */
public final class AttendanceRecordSequence {
    private static int val = 0;

    /**
     * Constructor: declared private to prevent instantiation.
     */
    private AttendanceRecordSequence () {}

    public static final int nextVal() {
        return val++;
    }
}
