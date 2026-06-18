package uz.katm.claim.mrz.etc2;

import uz.katm.claim.mrz.MrzParseException;
import uz.katm.claim.mrz.MrzRange;

public enum MrzDocumentCode {

    Passport,

    TypeI,

    TypeA,

    CrewMember,

    TypeC,

    TypeV;

    public static MrzDocumentCode parse(String mrz) {
        final String code = mrz.substring(0, 2);
        if (code.equals("IV")) {
            throw new MrzParseException("IV document code is not allowed", mrz, new MrzRange(0, 2, 0), null);
        }
        if (code.charAt(0) == 'P' || code.equals("IP")) {
            return Passport;
        }
        if (code.equals("AC")) {
            return CrewMember;
        }
        if (code.charAt(0) == 'I') {
            return TypeI;
        }
        if (code.charAt(0) == 'A') {
            return TypeA;
        }
        if (code.charAt(0) == 'C') {
            return TypeC;
        }
        if (code.charAt(0) == 'V') {
            return TypeV;
        }
        throw new MrzParseException("Invalid document code: " + code, mrz, new MrzRange(0, 2, 0), null);
    }
}
