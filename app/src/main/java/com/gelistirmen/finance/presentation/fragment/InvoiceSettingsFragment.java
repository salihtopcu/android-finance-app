package com.gelistirmen.finance.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gelistirmen.finance.R;
import com.gelistirmen.finance.model.operation.Invoice;
import com.gelistirmen.finance.presentation.activity.BaseActivity;
import com.gelistirmen.finance.presentation.view.FMButton;
import com.gelistirmen.finance.presentation.view.InvoiceSettingsViewHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InvoiceSettingsFragment.Listener} interface
 * to handle interaction events.
 * Use the {@link InvoiceSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceSettingsFragment extends Fragment {

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.listContainerLinearLayout) LinearLayout listContainerLinearLayout;

    private Invoice.List invoices;

    private Listener listener;

    public InvoiceSettingsFragment() {
        // Required empty public constructor
    }

    public static InvoiceSettingsFragment newInstance(Listener listener, Invoice.List invoices) {
        InvoiceSettingsFragment fragment = new InvoiceSettingsFragment();
        fragment.listener = listener;
        fragment.invoices = invoices;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setMainScrollContent(this.scrollView);
        this.reloadInvoicesList();
    }

    public void setInvoices(Invoice.List invoices) {
        this.invoices = invoices;
    }

    private void reloadInvoicesList() {
        this.listContainerLinearLayout.removeAllViews();
        for (Invoice invoice: this.invoices) {
            InvoiceSettingsViewHandler invoiceSettingsViewHandler = new InvoiceSettingsViewHandler((BaseActivity) getActivity(), invoice);
            invoiceSettingsViewHandler.setListener(new InvoiceSettingsViewHandler.Listener() {
                @Override
                public void onDeleteInvoiceButtonClick(InvoiceSettingsViewHandler viewHandler, Invoice invoice) {
                    invoices.remove(invoice);
                    if (invoices.size() > 0)
                        reloadInvoicesList();
                    else {
                        if (listener != null)
                            listener.onAddInvoiceButtonClick();
                        else
                            getActivity().finish();
                    }
                }
            });
            this.listContainerLinearLayout.addView(invoiceSettingsViewHandler.getView());
        }
    }

    @OnClick(R.id.addInvoiceButton)
    public void addInvoiceButtonAction(FMButton button) {
        if (this.listener != null)
            this.listener.onAddInvoiceButtonClick();
    }

    @OnClick(R.id.saveButton)
    public void saveButtonAction(FMButton button) {
        if (this.listener != null)
            this.listener.onSaveButtonClick(this.invoices);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        void onAddInvoiceButtonClick();
        void onSaveButtonClick(Invoice.List invoices);
    }
}
