package com.example.test1.res;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.test1.R;
import com.example.test1.generics.Beneficiary;

import java.util.List;


public class MyBeneficiariesAdapter extends ArrayAdapter<Beneficiary> {
    private int resourceLayout;
    private Context mContext;

    public MyBeneficiariesAdapter(Context context, int resource, List<Beneficiary> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Beneficiary p = getItem(position);

        if (p != null) {
            TextView name = v.findViewById(R.id.beneficiaryNameTextView);
            TextView account = v.findViewById(R.id.beneficiaryAccountNumberTextView);
            TextView bankName = v.findViewById(R.id.beneficiaryBankNameTextView);


            name.setText(p.getName());
            account.setText(Long.toString(p.getAccountNumber()));
            bankName.setText(p.getBankName());
        }
        return v;
    }

}