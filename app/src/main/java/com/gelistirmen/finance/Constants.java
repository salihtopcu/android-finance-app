package com.gelistirmen.finance;

public final class Constants {
    public static final String webServiceBaseUrl = "https://api.finance.gelistirmen.com/";
    public static final String webSiteUrl = "https://finance.gelistirmen.com/";

    public static final String senderId = "senderId";
    public static final String hubName = "hubName";
    public static final String hubListenUrl = "hubListenUrl";

    public static final String webServiceDateFormat = "yyyy-MM-dd";
    public static final String webServiceDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String interfaceDateFormat = "dd.MM.yyyy";
    public static final String interfaceDateTimeFormat = "dd/MM/yyyy HH:mm:ss";

    public static final int securityCodeDialogTimerInterval = 180000; // millisecond

    public static class FileChoosingMethod {
        public static final int TakePhoto = 1;
        public static final int ChooseFromDevice = 2;
    }

    public static class ApiMethod {
        public static String ActiveFactoringProcesses = "factorings/active";
        public static String Customers = "finance/customers";
        public static String ChangePassword = "auth/changePassword";
        public static String Login = "auth/login";
        public static String Me = "auth/me";
        public static String RenewToken = "auth/renewToken";
        public static String RegisterConfirm = "auth/registerConfirm";
        public static String ResendVerification = "auth/resendVerification";
        public static String ResetPassword = "auth/resetPassword";
        public static String BankAccounts = "finance/bankAccounts";
        public static String Companies = "finance/companies";
        public static String Invoices = "finance/invoices";
        public static String InvoiceVision = "finance/invoices/vision";
        public static String Notifications = "finance/notifications";
        public static String Quotes = "finance/quotes";
        public static String ApproveQuoteByDebtor = "finance/quotes/approveByDebtor";
        public static String ApproveQuoteByPayee = "finance/quotes/approveByPayee";
        public static String ApproveQuoteDebtor = "finance/quotes/debtor/approve";
        public static String CompleteQuote = "finance/quotes/complete";
        public static String ChangeQuote = "finance/quotes/changeQuoteRequest";
        public static String CompleteQuoteByPayee = "finance/quotes/completeByPayee";
        public static String DeclineQuote = "finance/quotes/decline";
        public static String GeneratePayeeContract = "finance/quotes/generatePayeeContract";
        public static String PayeeContract = "finance/quotes/{id}/payeeContract";
        public static String DebtorContract = "finance/quotes/{id}/debtorContract";
        public static String DebtorFingerPrint = "finance/quotes/debtor/fingerPrint/{quoteId}";
        public static String PayeeFingerPrint = "finance/quotes/payee/fingerPrint/{quoteId}";
        public static String Settings = "settings";
        public static String Cities = "settings/cities";
        public static String Counties = "settings/counties";
        public static String Documents = "documents";
        public static String DocumentTypes = "documentTypes";
        public static String QuotesWaitingForApproval = "quotes/debtorsapproval";
        public static String QuoteStatus = "finance/quotes/status/{id}";
    }

    public static class LanguageCode {
        public static String English = "en";
        public static String Turkish = "tr";
    }

    public static class Currency {
        public static int TL = 1;
        public static int USD = 2;
        public static int EURO = 3;

        public static String getSymbolFor(int currency) {
            switch (currency) {
                case 1:
                    return "₺";
                case 2:
                    return "$";
                case 3:
                    return "€";
                default:
                    return "";
            }
        }
    }

    public static class FactoringStatus {
        public static int InvoicesUploaded = 1;
        public static int InvoicesChecked = 2;
        public static int QuotePrepared = 3;
        public static int QuoteAccepted = 4;
        public static int DebtorApproved = 5;
        public static int ApprovedQuote = 6;
        public static int Completed = 7;
        public static int Declined = 15;

        public static String getDescription(int status) {
            switch (status) {
                case 1:
                    return MyApplication.instance.getResources().getString(R.string.demand_status_uploaded_description);
                case 2:
                    return MyApplication.instance.getResources().getString(R.string.demand_status_checked_description);
                case 3:
                    return MyApplication.instance.getResources().getString(R.string.demand_status_quote_prepared_description);
                case 4:
                    return MyApplication.instance.getResources().getString(R.string.demand_status_quote_accepted_description);
                case 5:
                    return MyApplication.instance.getResources().getString(R.string.demand_status_debtor_approved_description);
                case 6:
                    return MyApplication.instance.getResources().getString(R.string.demand_status_approved_quote_description);
                default:
                    return "";
            }
        }
    }

    public static class QuoteStatus {
        public static int PendingByPayee = 1;
        public static int PendingByDebtor = 2;
        public static int Rejected = 3;
        public static int ChangeRequest = 4;
        public static int PendingApproval = 5;
        public static int Completed = 6;
        public static int Revised = 7;
        public static int PendingDebtorSignature = 8;
        public static int PendingPayeeSignature = 9;
    }

    public static class Role {
        public static int Payee = 1;
        public static int Debtor = 2;
    }
}
