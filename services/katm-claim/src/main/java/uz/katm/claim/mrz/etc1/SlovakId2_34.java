package uz.katm.claim.mrz.etc1;

import uz.katm.claim.mrz.MrzParser;
import uz.katm.claim.mrz.MrzRange;
import uz.katm.claim.mrz.MrzRecord;
import uz.katm.claim.mrz.etc2.MrzFormat;

public class SlovakId2_34 extends MrzRecord {
    private static final long serialVersionUID = 1L;

    public SlovakId2_34() {
        super(MrzFormat.SLOVAK_ID_234);
    }

    public String optional;

    @Override
    public void fromMrz(String mrz) {
        super.fromMrz(mrz);
        final MrzParser p = new MrzParser(mrz);
        setName(p.parseName(new MrzRange(5, 34, 0)));
        documentNumber = p.parseString(new MrzRange(0, 9, 1));
        p.checkDigit(9, 1, new MrzRange(0, 9, 1), "document number");
        nationality = p.parseString(new MrzRange(10, 13, 1));
        dateOfBirth = p.parseDate(new MrzRange(13, 19, 1));
        p.checkDigit(19, 1, new MrzRange(13, 19, 1), "date of birth");
        sex = p.parseSex(20, 1);
        expirationDate = p.parseDate(new MrzRange(21, 27, 1));
        p.checkDigit(27, 1, new MrzRange(21, 27, 1), "expiration date");
        optional = p.parseString(new MrzRange(28, 34, 1));
    }

    @Override
    public String toString() {
        return "SlovakId2x34{" + super.toString() + ", optional=" + optional + '}';
    }

    @Override
    public String toMrz() {
        final StringBuilder sb = new StringBuilder();
        sb.append(code1);
        sb.append(code2);
        sb.append(MrzParser.toMrz(issuingCountry, 3));
        sb.append(MrzParser.nameToMrz(surname, givenNames, 29));
        sb.append('\n');
        sb.append(MrzParser.toMrz(documentNumber, 9));
        sb.append(MrzParser.computeCheckDigitChar(MrzParser.toMrz(documentNumber, 9)));
        sb.append(MrzParser.toMrz(nationality, 3));
        sb.append(dateOfBirth.toMrz());
        sb.append(MrzParser.computeCheckDigitChar(dateOfBirth.toMrz()));
        sb.append(sex.mrz);
        sb.append(expirationDate.toMrz());
        sb.append(MrzParser.computeCheckDigitChar(expirationDate.toMrz()));
        sb.append(MrzParser.toMrz(optional, 6));
        sb.append('\n');
        return sb.toString();
    }
}
