package com.gelistirmen.finance;

import com.gelistirmen.finance.model.membership.BankAccount;
import com.gelistirmen.finance.model.membership.Company;
import com.gelistirmen.finance.model.membership.DocumentType;
import com.gelistirmen.finance.model.membership.LoginResponse;
import com.gelistirmen.finance.model.membership.RegisterResponse;
import com.gelistirmen.finance.model.membership.RenewTokenResponse;
import com.gelistirmen.finance.model.membership.UserProfile;
import com.gelistirmen.finance.model.operation.FactoringProcess;
import com.gelistirmen.finance.model.operation.Fingerprint;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.model.operation.Notification;
import com.gelistirmen.finance.model.operation.Quote;
import com.gelistirmen.finance.model.operation.QuoteStatusResponse;
import com.gelistirmen.finance.model.settings.City;
import com.gelistirmen.finance.model.settings.County;
import com.gelistirmen.finance.model.settings.SettingsModel;

import org.json.JSONArray;

import java.util.Date;

public abstract class MockProvider {

    public static LoginResponse loginResponse() {
        return new LoginResponse("123", "123", "123", false);
    }

    public static RegisterResponse registerResponse() {
        return new RegisterResponse("123", true);
    }

    public static RenewTokenResponse renewTokenResponse() {
        return new RenewTokenResponse("123", "123");
    }

    public static City city() {
        return new City("34", "Istanbul");
    }

    public static City.List cities() {
        City.List list = new City.List(new JSONArray());
        list.add(city());
        list.add(city());
        return list;
    }

    public static County county() {
        return new County("34722", "Kadikoy", city());
    }

    public static County.List counties() {
        County.List list = new County.List(new JSONArray());
        list.add(county());
        list.add(county());
        return list;
    }

    public static SettingsModel settingsModel() {
        return new SettingsModel(false, Constants.LanguageCode.Turkish);
    }

    public static BankAccount bankAccount() {
        return new BankAccount("Isengard", "1234 5678", "1234567", "TR12 3456 7890 1234 5678 90");
    }

    public static BankAccount.List bankAccounts() {
        BankAccount.List list = new BankAccount.List(new JSONArray());
        list.add(bankAccount());
        list.add(bankAccount());
        return list;
    }

    public static Company company() {
        return new Company("123", "Gelistirmen", "2121234567", "info@gelistirmen.com", "Bag End Shire", "34000", county(), "1234567", "Shire T.O.", "www.gelistirmen.com", bankAccounts());
    }

    public static UserProfile userProfile() {
        return new UserProfile("123", "Frodo Baggins", "02121234567", company());
    }

    public static FactoringProcess factoringProcess() {
        return new FactoringProcess("123", "123", new Date(), 34.50, 1, Constants.Currency.TL);
    }

    public static FactoringProcess.List factoringProcesses() {
        FactoringProcess.List list = new FactoringProcess.List(new JSONArray());
        list.add(factoringProcess());
        list.add(factoringProcess());
        return list;
    }

    public static Fingerprint fingerprint() {
        return new Fingerprint("1234567", 30);
    }

    public static Invoice invoice() {
        return new Invoice("123", new Date(), new Date(), 101.02, Constants.Currency.TL, false, 65.04, 1, "1234567890", "12", new Date(), "123321", 1, new Date(), "1234567", "1234567", null);
    }

    public static Invoice.List invoices() {
        Invoice.List list = new Invoice.List(new JSONArray());
        list.add(invoice());
        list.add(invoice());
        return list;
    }

    public static Notification notification() {
        return new Notification("123", null, "Some notification message", new Date(), false);
    }

    public static Notification.List notifications() {
        Notification.List list = new Notification.List(new JSONArray());
        list.add(notification());
        list.add(notification());
        return list;
    }

    public static Quote quote() {
        return new Quote("123", Constants.QuoteStatus.Completed, new Date(), new Date(), "Rivendell", 13.25, new Date(), 15.45, 30, invoices(), "Lord Elrond", "1234567", "Gandalf the Grey", 10, 105.33, 46.33, Constants.Currency.TL);
    }

    public static Quote.List quotes() {
        Quote.List list = new Quote.List(new JSONArray());
        list.add(quote());
        list.add(quote());
        return list;
    }

    public static QuoteStatusResponse quoteStatusResponse() {
        return new QuoteStatusResponse(Constants.QuoteStatus.Completed, false, false);
    }

    public static DocumentType documentType() {
        return new DocumentType("123", "Doc Type 1", false, 100);
    }

    public static DocumentType.List documentTypes() {
        DocumentType.List list = new DocumentType.List(new JSONArray());
        list.add(documentType());
        list.add(documentType());
        return list;
    }


}
