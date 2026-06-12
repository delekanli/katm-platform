package uz.katm.report.domain.fcbk;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FicoScoreParams {
    @JsonProperty("header")
    private HeaderBlock header;

    @JsonProperty("individual")
    private IndividualBlock individual;

    @JsonProperty("accounts")
    private List<AccountsBlock> accounts;

    public FicoScoreParams(HeaderBlock header, IndividualBlock individual, List<AccountsBlock> accounts) {
        this.header = header;
        this.individual = individual;
        this.accounts = accounts;
    }

    public HeaderBlock getHeader() {
        return header;
    }

    public void setHeader(HeaderBlock header) {
        this.header = header;
    }

    public IndividualBlock getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualBlock individual) {
        this.individual = individual;
    }

    public List<AccountsBlock> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountsBlock> accounts) {
        this.accounts = accounts;
    }
}
