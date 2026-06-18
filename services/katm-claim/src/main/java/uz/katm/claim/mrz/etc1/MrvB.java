package uz.katm.claim.mrz.etc1;

import uz.katm.claim.mrz.MrzParser;
import uz.katm.claim.mrz.MrzRange;
import uz.katm.claim.mrz.MrzRecord;
import uz.katm.claim.mrz.etc2.MrzDocumentCode;
import uz.katm.claim.mrz.etc2.MrzFormat;

public class MrvB extends MrzRecord {

    private static final long serialVersionUID = 1L;

    public MrvB() {
        super(MrzFormat.MRV_VISA_B);
        code1 = 'V';
        code2 = '<';
        code = MrzDocumentCode.TypeV;
    }

    public String optional;

    @Override
    public void fromMrz(String mrz) {
        super.fromMrz(mrz);
        final MrzParser parser = new MrzParser(mrz);
        setName(parser.parseName(new MrzRange(5, 36, 0)));
        documentNumber = parser.parseString(new MrzRange(0, 9, 1));
        parser.checkDigit(9, 1, new MrzRange(0, 9, 1), "passport number");
        nationality = parser.parseString(new MrzRange(10, 13, 1));
        dateOfBirth = parser.parseDate(new MrzRange(13, 19, 1));
        parser.checkDigit(19, 1, new MrzRange(13, 19, 1), "date of birth");
        sex = parser.parseSex(20, 1);
        expirationDate = parser.parseDate(new MrzRange(21, 27, 1));
        parser.checkDigit(27, 1, new MrzRange(21, 27, 1), "expiration date");
        optional = parser.parseString(new MrzRange(28, 36, 1));
    }

    @Override
    public String toString() {
        return "MRV-B{" + super.toString() + ", optional=" + optional + '}';
    }

    @Override
    public String toMrz() {
        final StringBuilder sb = new StringBuilder("V<");
        sb.append(MrzParser.toMrz(issuingCountry, 3));
        sb.append(MrzParser.nameToMrz(surname, givenNames, 31));
        sb.append('\n');
        sb.append(MrzParser.toMrz(documentNumber, 9));
        sb.append(MrzParser.computeCheckDigitChar(MrzParser.toMrz(documentNumber, 9)));
        sb.append(MrzParser.toMrz(nationality, 3));
        sb.append(dateOfBirth.toMrz());
        sb.append(MrzParser.computeCheckDigitChar(dateOfBirth.toMrz()));
        sb.append(sex.mrz);
        sb.append(expirationDate.toMrz());
        sb.append(MrzParser.computeCheckDigitChar(expirationDate.toMrz()));
        sb.append(MrzParser.toMrz(optional, 8));
        sb.append('\n');
        return sb.toString();
    }
}
