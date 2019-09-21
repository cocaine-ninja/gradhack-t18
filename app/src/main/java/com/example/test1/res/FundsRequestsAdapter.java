package com.example.test1.res;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.test1.R;
import com.example.test1.generics.FundsRequest;

import java.util.List;


public class FundsRequestsAdapter extends ArrayAdapter<FundsRequest> {

    private int resourceLayout;
    private Context mContext;

    public FundsRequestsAdapter(Context context, int resource, List<FundsRequest> items) {
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

        FundsRequest p = getItem(position);

        if (p != null) {
            TextView name = v.findViewById(R.id.requestPerson);
            TextView amount = v.findViewById(R.id.requestAmount);
            TextView purpose = v.findViewById(R.id.requestPurpose);

            name.setText(p.getPersonName());
            amount.setText(Integer.toString(p.getAmount()));
            purpose.setText(p.getPurpose());
        }
        return v;
    }

}