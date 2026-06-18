package uz.katm.claim.mrz.etc2;

import uz.katm.claim.mrz.MrzParseException;
import uz.katm.claim.mrz.MrzRange;
import uz.katm.claim.mrz.MrzRecord;
import uz.katm.claim.mrz.etc1.FrenchIdCard;
import uz.katm.claim.mrz.etc1.MRP;
import uz.katm.claim.mrz.etc1.MrtdTd1;
import uz.katm.claim.mrz.etc1.MrtdTd2;
import uz.katm.claim.mrz.etc1.MrvA;
import uz.katm.claim.mrz.etc1.MrvB;
import uz.katm.claim.mrz.etc1.SlovakId2_34;

public enum MrzFormat {

    MRTD_TD1(3, 30, MrtdTd1.class),

    FRENCH_ID(2, 36, FrenchIdCard.class) {
        public boolean isFormatOf(String[] mrzRows) {
            if (!super.isFormatOf(mrzRows)) {
                return false;
            }
            return mrzRows[0].substring(0, 5).equals("IDFRA");
        }
    },

    MRV_VISA_B(2, 36, MrvB.class) {
        public boolean isFormatOf(String[] mrzRows) {
            if (!super.isFormatOf(mrzRows)) {
                return false;
            }
            return mrzRows[0].substring(0, 1).equals("V");
        }
    },

    MRTD_TD2(2, 36, MrtdTd2.class),

    MRV_VISA_A(2, 44, MrvA.class) {
        public boolean isFormatOf(String[] mrzRows) {
            if (!super.isFormatOf(mrzRows)) {
                return false;
            }
            return mrzRows[0].substring(0, 1).equals("V");
        }
    },

    PASSPORT(2, 44, MRP.class),

    SLOVAK_ID_234(2, 34, SlovakId2_34.class);
    public final int rows;
    public final int columns;
    private final Class<? extends MrzRecord> recordClass;

    private MrzFormat(int rows, int columns, Class<? extends MrzRecord> recordClass) {
        this.rows = rows;
        this.columns = columns;
        this.recordClass = recordClass;
    }

    public boolean isFormatOf(String[] mrzRows) {
        return rows == mrzRows.length && columns == mrzRows[0].length();
    }

    public static final MrzFormat get(String mrz) {
        final String[] rows = mrz.split("\n");
        final int cols = rows[0].length();
        for (int i = 1; i < rows.length; i++) {
            if (rows[i].length() != cols) {
                throw new MrzParseException("Different row lengths: 0: " + cols + " and " + i + ": " + rows[i].length(), mrz, new MrzRange(0, 0, 0), null);
            }
        }
        for (final MrzFormat f : values()) {
            if (f.isFormatOf(rows)) {
                return f;
            }
        }
        throw new MrzParseException("Unknown format / unsupported number of cols/rows: " + cols + "/" + rows.length, mrz, new MrzRange(0, 0, 0), null);
    }

    public final MrzRecord newRecord() {
        try {
            return recordClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
