package uz.katm.report.domain.fcbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.katm.report.util.ConverterUtils;
import java.util.Date;

public class HeaderBlock {
    @JsonProperty("credit_report_date")
    private String creditReportDate;

    @JsonProperty("external_id")
    private String externalId;

    public HeaderBlock(FicoResultEntity resultEntity) {
        this.creditReportDate = ConverterUtils.dateToString(new Date(), ConverterUtils.FORMAT_YYYY_MM_DD);
        this.externalId = resultEntity.getExternal_Id();
    }

    public String getCreditReportDate() {
        return creditReportDate;
    }

    public void setCreditReportDate(String creditReportDate) {
        this.creditReportDate = creditReportDate;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
