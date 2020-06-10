package com.gelistirmen.finance.model.operation;

import java.util.Date;

public class ChangeQuoteRequest {
    public String quoteId;
    public String note;
    public Date requestedMaturityDate;

    public ChangeQuoteRequest(String quoteId, String note, Date requestedMaturityDate) {
        this.quoteId = quoteId;
        this.note = note;
        this.requestedMaturityDate = requestedMaturityDate;
    }
}
