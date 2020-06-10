package com.gelistirmen.finance.model.operation;

public class DeclineQuoteRequest {
    public String quoteId;
    public String note;

    public DeclineQuoteRequest(String quoteId, String note) {
        this.quoteId = quoteId;
        this.note = note;
    }
}
