package com.gelistirmen.finance.data.remote.operation;

import com.android.volley.Request;
import com.gelistirmen.finance.Constants;
import com.gelistirmen.finance.data.remote.base.FMDao;
import com.gelistirmen.finance.model.membership.RegisterResponse;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.util.Method;

import org.json.JSONObject;

public class CreateInvoiceDao extends FMDao {

    public CreateInvoiceDao(VolleyDaoListener listener, Invoice.List invoices) {
        super(listener, Constants.ApiMethod.Invoices, Request.Method.POST, true);
        super.setRequestType(RequestType.MultipartRequest);
        super.timeout = 60000;
        Invoice invoice;
        for (int i = 0; i < invoices.size(); i++) {
            invoice = invoices.get(i);
            String index = Integer.toString(i);
            super.setMultipartData(invoice.pictureData, "[" + index + "].invoicePicture", "invoice_test_" + index + ".jpeg", "image/jpeg");
            super.addMultipartParameter("[" + index + "].invoiceAmount", Method.formatDoubleAsString(invoice.amount, 2));
            super.addMultipartParameter("[" + index + "].currency", Integer.toString(invoice.currency));
            super.addMultipartParameter("[" + index + "].isWholeCost", invoice.isWholeCost ? "true" : "false");
            super.addMultipartParameter("[" + index + "].paymentCost", Method.formatDoubleAsString(invoice.paymentCost, 2));
            super.addMultipartParameter("[" + index + "].interestResponsible", Integer.toString(invoice.interestResponsible));
            super.addMultipartParameter("[" + index + "].serialNo", invoice.serialNo);
            super.addMultipartParameter("[" + index + "].invoiceNo", invoice.no);
            if (invoice.date != null)
                super.addMultipartParameter("[" + index + "].invoiceDate", Method.getFormatedDate(invoice.date, Constants.webServiceDateTimeFormat));
            else
                super.addMultipartParameter("[" + index + "].invoiceDate", "");
            super.addMultipartParameter("[" + index + "].einvoiceHash", invoice.eInvoiceHash);
            super.addMultipartParameter("[" + index + "].invoiceType", Integer.toString(invoice.type));
            if (invoice.estimatedMaturityDate != null)
                super.addMultipartParameter("[" + index + "].estimatedMaturityDate", Method.getFormatedDate(invoice.estimatedMaturityDate, Constants.webServiceDateTimeFormat));
            else
                super.addMultipartParameter("[" + index + "].estimatedMaturityDate", "");
            super.addMultipartParameter("[" + index + "].payeeTaxNo", invoice.payeeTaxNo);
            super.addMultipartParameter("[" + index + "].debtorTaxNo", invoice.debtorTaxNo);
        }
    }

    @Override
    protected void onAfterSuccessRequest(Object data) {
        super.onAfterSuccessRequest(new RegisterResponse((JSONObject) data));
    }
}
