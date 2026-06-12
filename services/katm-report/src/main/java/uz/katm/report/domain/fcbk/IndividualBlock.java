package uz.katm.report.domain.fcbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.katm.report.util.ConverterUtils;

public class IndividualBlock {
    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("negative_status")
    private String negativeStatus;

    public IndividualBlock(FicoResultEntity resultEntity) {
        this.dateOfBirth = ConverterUtils.dateToString(resultEntity.getDateOfBirth(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.negativeStatus = resultEntity.getNegativeStatus();
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNegativeStatus() {
        return negativeStatus;
    }

    public void setNegativeStatus(String negativeStatus) {
        this.negativeStatus = negativeStatus;
    }
}
