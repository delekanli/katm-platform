package uz.katm.claim.mrz.etc2;

import java.io.Serializable;

public class MrzDate implements Serializable, Comparable<MrzDate> {
    private static final long serialVersionUID = 1L;

    public final int year;

    public final int month;

    public final int day;

    public MrzDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        check();
    }

    @Override
    public String toString() {
        return "{" + day + "/" + month + "/" + year + '}';
    }

    public String toMrz() {
        return String.format("%02d%02d%02d", year, month, day);
    }

    private void check() {
        if (year < 0 || year > 99) {
            throw new IllegalArgumentException("Parameter year: invalid value " + year + ": must be 0..99");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Parameter month: invalid value " + month + ": must be 1..12");
        }
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Parameter day: invalid value " + day + ": must be 1..31");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MrzDate other = (MrzDate) obj;
        if (this.year != other.year) {
            return false;
        }
        if (this.month != other.month) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.year;
        hash = 11 * hash + this.month;
        hash = 11 * hash + this.day;
        return hash;
    }

    public int compareTo(MrzDate o) {
        return Integer.valueOf(year * 10000 + month * 100 + day).compareTo(o.year * 10000 + o.month * 100 + o.day);
    }
}
