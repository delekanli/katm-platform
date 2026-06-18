package uz.katm.claim.mrz.etc1;

import uz.katm.claim.mrz.MrzParser;
import uz.katm.claim.mrz.MrzRange;
import uz.katm.claim.mrz.MrzRecord;
import uz.katm.claim.mrz.etc2.MrzFormat;

public class MRP extends MrzRecord {
    private static final long serialVersionUID = 1L;

    public MRP() {
        super(MrzFormat.PASSPORT);
    }

    public String personalNumber;

    @Override
    public void fromMrz(String mrz) {
        super.fromMrz(mrz);
        final MrzParser parser = new MrzParser(mrz);
        setName(parser.parseName(new MrzRange(5, 44, 0)));
        documentNumber = parser.parseString(new MrzRange(0, 9, 1));
        parser.checkDigit(9, 1, new MrzRange(0, 9, 1), "passport number");
        nationality = parser.parseString(new MrzRange(10, 13, 1));
        dateOfBirth = parser.parseDate(new MrzRange(13, 19, 1));
        parser.checkDigit(19, 1, new MrzRange(13, 19, 1), "date of birth");
        sex = parser.parseSex(20, 1);
        expirationDate = parser.parseDate(new MrzRange(21, 27, 1));
        parser.checkDigit(27, 1, new MrzRange(21, 27, 1), "expiration date");
        personalNumber = parser.parseString(new MrzRange(28, 42, 1));
        parser.checkDigit(42, 1, new MrzRange(28, 42, 1), "personal number");
        parser.checkDigit(43, 1, parser.rawValue(new MrzRange(0, 10, 1), new MrzRange(13, 20, 1), new MrzRange(21, 43, 1)), "mrz");
    }

    @Override
    public String toString() {
        return "MRP{" + super.toString() + ", personalNumber=" + personalNumber + '}';
    }

    @Override
    public String toMrz() {
        final StringBuilder sb = new StringBuilder();
        sb.append(code1);
        sb.append(code2);
        sb.append(MrzParser.toMrz(issuingCountry, 3));
        sb.append(MrzParser.nameToMrz(surname, givenNames, 39));
        sb.append('\n');
        final String docNum = MrzParser.toMrz(documentNumber, 9) + MrzParser.computeCheckDigitChar(MrzParser.toMrz(documentNumber, 9));
        sb.append(docNum);
        sb.append(MrzParser.toMrz(nationality, 3));
        final String dob = dateOfBirth.toMrz() + MrzParser.computeCheckDigitChar(dateOfBirth.toMrz());
        sb.append(dob);
        sb.append(sex.mrz);
        final String edpn = expirationDate.toMrz() + MrzParser.computeCheckDigitChar(expirationDate.toMrz()) + MrzParser.toMrz(personalNumber, 14) + MrzParser.computeCheckDigitChar(MrzParser.toMrz(personalNumber, 14));
        sb.append(edpn);
        sb.append(MrzParser.computeCheckDigitChar(docNum + dob + edpn));
        sb.append('\n');
        return sb.toString();
    }
}
