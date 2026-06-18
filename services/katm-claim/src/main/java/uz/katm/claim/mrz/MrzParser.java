package uz.katm.claim.mrz;

import uz.katm.claim.mrz.etc2.MrzDate;
import uz.katm.claim.mrz.etc2.MrzFormat;
import uz.katm.claim.mrz.etc2.MrzSex;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class MrzParser {

    public final String mrz;

    public final String[] rows;

    public final MrzFormat format;

    public MrzParser(String mrz) {
        this.mrz = mrz;
        this.rows = mrz.split("\n");
        this.format = MrzFormat.get(mrz);
    }

    public String[] parseName(MrzRange range) {
        checkValidCharacters(range);
        String str = rawValue(range);
        while (str.endsWith("<")) {
            str = str.substring(0, str.length() - 1);
        }
        final String[] names = str.split("<<");
        final String surname = parseString(new MrzRange(range.column, range.column + names[0].length(), range.row));
        final String givenNames = parseString(new MrzRange(range.column + names[0].length() + 2, range.column + str.length(), range.row));
        return new String[]{surname, givenNames};
    }

    public String rawValue(MrzRange... range) {
        final StringBuilder sb = new StringBuilder();
        for (MrzRange r : range) {
            sb.append(rows[r.row].substring(r.column, r.columnTo));
        }
        return sb.toString();
    }

    public void checkValidCharacters(MrzRange range) {
        final String str = rawValue(range);
        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);
            if (c != FILLER && (c < '0' || c > '9') && (c < 'A' || c > 'Z')) {
                throw new MrzParseException("Invalid character in MRZ record: " + c, mrz, new MrzRange(range.column + i, range.column + i + 1, range.row), format);
            }
        }
    }

    public String parseString(MrzRange range) {
        checkValidCharacters(range);
        String str = rawValue(range);
        while (str.endsWith("<")) {
            str = str.substring(0, str.length() - 1);
        }
        return str.replace("" + FILLER + FILLER, ", ").replace(FILLER, ' ');
    }

    public void checkDigit(int col, int row, MrzRange strRange, String fieldName) {
        checkDigit(col, row, rawValue(strRange), fieldName);
    }

    public void checkDigit(int col, int row, String str, String fieldName) {
        final char digit = (char) (computeCheckDigit(str) + '0');
        final char checkDigit = rows[row].charAt(col);
        if (digit != checkDigit || (checkDigit != FILLER && checkDigit != '0' && digit == '0')) {
//            throw new MrzParseException("Check digit verification failed for " + fieldName + ": expected " + digit + " but got " + checkDigit, mrz, new MrzRange(col, col + 1, row), format);
            //ignore
        }
    }

    public MrzDate parseDate(MrzRange range) {
        if (range.length() != 6) {
            throw new IllegalArgumentException("Parameter range: invalid value " + range + ": must be 6 characters long");
        }
        MrzRange r = null;
        try {
            r = new MrzRange(range.column, range.column + 2, range.row);
            final int year = Integer.parseInt(rawValue(r));
            if (year < 0 || year > 99) {
                throw new MrzParseException("Failed to parse MRZ date: invalid year value " + year + ": must be 0..99", mrz, r, format);
            }
            r = new MrzRange(range.column + 2, range.column + 4, range.row);
            final int month = Integer.parseInt(rawValue(r));
            if (month < 1 || month > 12) {
                throw new MrzParseException("Failed to parse MRZ date: invalid month value " + month + ": must be 1..12", mrz, r, format);
            }
            r = new MrzRange(range.column + 4, range.column + 6, range.row);
            final int day = Integer.parseInt(rawValue(r));
            if (day < 1 || day > 31) {
                throw new MrzParseException("Failed to parse MRZ date: invalid day value " + day + ": must be 1..31", mrz, r, format);
            }
            return new MrzDate(year, month, day);
        } catch (NumberFormatException ex) {
            throw new MrzParseException("Failed to parse MRZ date " + rawValue(range) + ": " + ex, mrz, r, format);
        }
    }

    public MrzSex parseSex(int col, int row) {
        return MrzSex.fromMrz(rows[row].charAt(col));
    }

    private static final int[] MRZ_WEIGHTS = new int[]{7, 3, 1};

    private static boolean isValid(char c) {
        return ((c == FILLER) || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z'));
    }

    private static int getCharacterValue(char c) {
        if (c == FILLER) {
            return 0;
        }
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        throw new RuntimeException("Invalid character in MRZ record: " + c);
    }

    public static int computeCheckDigit(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += getCharacterValue(str.charAt(i)) * MRZ_WEIGHTS[i % MRZ_WEIGHTS.length];
        }
        return result % 10;
    }

    public static char computeCheckDigitChar(String str) {
        return (char) ('0' + computeCheckDigit(str));
    }

    public static MrzRecord parse(String mrz) {
        final MrzRecord result = MrzFormat.get(mrz).newRecord();
        result.fromMrz(mrz);
        return result;
    }

    private static final Map<String, String> EXPAND_CHARACTERS = new HashMap<String, String>();

    static {
        EXPAND_CHARACTERS.put("\u00C4", "AE"); // Ä
        EXPAND_CHARACTERS.put("\u00E4", "AE"); // ä
        EXPAND_CHARACTERS.put("\u00C5", "AA"); // Å
        EXPAND_CHARACTERS.put("\u00E5", "AA"); // å
        EXPAND_CHARACTERS.put("\u00C6", "AE"); // Æ
        EXPAND_CHARACTERS.put("\u00E6", "AE"); // æ
        EXPAND_CHARACTERS.put("\u0132", "IJ"); // Ĳ
        EXPAND_CHARACTERS.put("\u0133", "IJ"); // ĳ
        EXPAND_CHARACTERS.put("\u00D6", "OE"); // Ö
        EXPAND_CHARACTERS.put("\u00F6", "OE"); // ö
        EXPAND_CHARACTERS.put("\u00D8", "OE"); // Ø
        EXPAND_CHARACTERS.put("\u00F8", "OE"); // ø
        EXPAND_CHARACTERS.put("\u00DC", "UE"); // Ü
        EXPAND_CHARACTERS.put("\u00FC", "UE"); // ü
        EXPAND_CHARACTERS.put("\u00DF", "SS"); // ß
    }

    public static String toMrz(String string, int length) {
        if (string == null) {
            string = "";
        }
        for (final Map.Entry<String, String> e : EXPAND_CHARACTERS.entrySet()) {
            string = string.replace(e.getKey(), e.getValue());
        }
        string = string.replace("’", "");
        string = string.replace("'", "");
        string = deaccent(string).toUpperCase();
        if (length >= 0 && string.length() > length) {
            string = string.substring(0, length);
        }
        final StringBuilder sb = new StringBuilder(string);
        for (int i = 0; i < sb.length(); i++) {
            if (!isValid(sb.charAt(i))) {
                sb.setCharAt(i, FILLER);
            }
        }
        while (sb.length() < length) {
            sb.append(FILLER);
        }
        return sb.toString();
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String nameToMrz(String surname, String givenNames, int length) {
        if (isBlank(surname)) {
            throw new IllegalArgumentException("Parameter surname: invalid value " + surname + ": blank");
        }
        if (isBlank(givenNames)) {
            throw new IllegalArgumentException("Parameter givenNames: invalid value " + givenNames + ": blank");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Parameter length: invalid value " + length + ": not positive");
        }
        surname = surname.replace(", ", " ");
        givenNames = givenNames.replace(", ", " ");
        final String[] surnames = surname.trim().split("[ \n\t\f\r]+");
        final String[] given = givenNames.trim().split("[ \n\t\f\r]+");
        for (int i = 0; i < surnames.length; i++) {
            surnames[i] = toMrz(surnames[i], -1);
        }
        for (int i = 0; i < given.length; i++) {
            given[i] = toMrz(given[i], -1);
        }
        int nameSize = getNameSize(surnames, given);
        String[] currentlyTruncating = given;
        int currentlyTruncatingIndex = given.length - 1;
        while (nameSize > length) {
            final String ct = currentlyTruncating[currentlyTruncatingIndex];
            final int ctsize = ct.length();
            if (nameSize - ctsize + 1 <= length) {
                currentlyTruncating[currentlyTruncatingIndex] = ct.substring(0, ctsize - (nameSize - length));
            } else {
                currentlyTruncating[currentlyTruncatingIndex] = ct.substring(0, 1);
                currentlyTruncatingIndex--;
                if (currentlyTruncatingIndex < 0) {
                    if (currentlyTruncating == surnames) {
                        throw new IllegalArgumentException("Cannot truncate name " + surname + " " + givenNames + ": length too small: " + length + "; truncated to " + toName(surnames, given));
                    }
                    currentlyTruncating = surnames;
                    currentlyTruncatingIndex = currentlyTruncating.length - 1;
                }
            }
            nameSize = getNameSize(surnames, given);
        }
        return toMrz(toName(surnames, given), length);
    }

    public static final char FILLER = '<';

    private static String toName(String[] surnames, String[] given) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : surnames) {
            if (first) {
                first = false;
            } else {
                sb.append(FILLER);
            }
            sb.append(s);
        }
        sb.append(FILLER);
        for (String s : given) {
            sb.append(FILLER);
            sb.append(s);
        }
        return sb.toString();
    }

    private static int getNameSize(final String[] surnames, final String[] given) {
        int result = 0;
        for (String s : surnames) {
            result += s.length() + 1;
        }
        for (String s : given) {
            result += s.length() + 1;
        }
        return result;
    }

    private static String deaccent(String str) {
        String n = Normalizer.normalize(str, Normalizer.Form.NFD);
        return n.replaceAll("[^\\p{ASCII}]", "").toLowerCase();
    }
}
