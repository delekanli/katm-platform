package uz.katm.claim.mrz.etc1;

import uz.katm.claim.mrz.MrzParser;
import uz.katm.claim.mrz.MrzRange;
import uz.katm.claim.mrz.MrzRecord;
import uz.katm.claim.mrz.etc2.MrzDocumentCode;
import uz.katm.claim.mrz.etc2.MrzFormat;

public class FrenchIdCard extends MrzRecord {

    private static final long serialVersionUID = 1L;

    public FrenchIdCard() {
        super(MrzFormat.FRENCH_ID);
        code = MrzDocumentCode.TypeI;
        code1 = 'I';
        code2 = 'D';
    }

    public String optional;

    @Override
    public void fromMrz(String mrz) {
        super.fromMrz(mrz);
        final MrzParser p = new MrzParser(mrz);
        String[] name = new String[]{"", ""};
        name[0] = p.parseString(new MrzRange(5, 30, 0));
        name[1] = p.parseString(new MrzRange(13, 27, 1));
        setName(name);
        nationality = p.parseString(new MrzRange(2, 5, 0));
        optional = p.parseString(new MrzRange(30, 36, 0));
        documentNumber = p.parseString(new MrzRange(0, 12, 1));
        p.checkDigit(12, 1, new MrzRange(0, 12, 1), "document number");
        dateOfBirth = p.parseDate(new MrzRange(27, 33, 1));
        p.checkDigit(33, 1, new MrzRange(27, 33, 1), "date of birth");
        sex = p.parseSex(34, 1);
        final String finalChecksum = mrz.toString().replace("\n", "").substring(0, 36 + 35);
        p.checkDigit(35, 1, finalChecksum, "final checksum");
    }

    @Override
    public String toString() {
        return "FrenchIdCard{" + super.toString() + ", optional=" + optional + '}';
    }

    @Override
    public String toMrz() {
        final StringBuilder sb = new StringBuilder("IDFRA");
        sb.append(MrzParser.toMrz(surname, 25));
        sb.append(MrzParser.toMrz(optional, 6));
        sb.append('\n');
        sb.append(MrzParser.toMrz(documentNumber, 12));
        sb.append(MrzParser.computeCheckDigitChar(MrzParser.toMrz(documentNumber, 12)));
        sb.append(MrzParser.toMrz(givenNames, 14));
        sb.append(dateOfBirth.toMrz());
        sb.append(MrzParser.computeCheckDigitChar(dateOfBirth.toMrz()));
        sb.append(sex.mrz);
        sb.append(MrzParser.computeCheckDigitChar(sb.toString().replace("\n", "")));
        sb.append('\n');
        return sb.toString();
    }
}
