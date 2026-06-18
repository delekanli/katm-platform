package uz.katm.claim.mrz;

import uz.katm.claim.mrz.etc2.MrzDate;
import uz.katm.claim.mrz.etc2.MrzDocumentCode;
import uz.katm.claim.mrz.etc2.MrzFormat;
import uz.katm.claim.mrz.etc2.MrzSex;
import java.io.Serializable;

public abstract class MrzRecord implements Serializable {

    public MrzDocumentCode code;

    public char code1;

    public char code2;

    public String issuingCountry;

    public String documentNumber;

    public String surname;

    public String givenNames;

    public MrzDate dateOfBirth;

    public MrzSex sex;

    public MrzDate expirationDate;

    public String nationality;

    public final MrzFormat format;

    protected MrzRecord(MrzFormat format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "MrzRecord{" + "code=" + code + "[" + code1 + code2 + "], issuingCountry=" + issuingCountry + ", documentNumber=" + documentNumber + ", surname=" + surname + ", givenNames=" + givenNames + ", dateOfBirth=" + dateOfBirth + ", sex=" + sex + ", expirationDate=" + expirationDate + ", nationality=" + nationality + '}';
    }

    public void fromMrz(String mrz) throws MrzParseException {
        if (format != MrzFormat.get(mrz)) {
            throw new MrzParseException("invalid format: " + MrzFormat.get(mrz), mrz, new MrzRange(0, 0, 0), format);
        }
        code = MrzDocumentCode.parse(mrz);
        code1 = mrz.charAt(0);
        code2 = mrz.charAt(1);
        issuingCountry = new MrzParser(mrz).parseString(new MrzRange(2, 5, 0));
    }

    protected final void setName(String[] name) {
        surname = name[0];
        givenNames = name[1];
    }

    public abstract String toMrz();
}
