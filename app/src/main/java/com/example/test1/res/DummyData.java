package com.example.test1.res;

import com.example.test1.generics.Beneficiary;
import com.example.test1.generics.FundsRequest;

import java.util.ArrayList;


public class DummyData {
    ArrayList<FundsRequest> fundsRequests;
    ArrayList<Beneficiary> myBeneficiaries;

    public DummyData() {}

    public void initFundsRequests() {
        fundsRequests = new ArrayList<>();
        FundsRequest req1 = new FundsRequest(1, "Sujay", 11001678321L, 100, 8010761583L, "Lunch");
        FundsRequest req2 = new FundsRequest(2, "Aman", 33892011180L, 5000, 9654741594L, "Drinks");
        FundsRequest req3 = new FundsRequest(3, "Aman", 33892011180L, 15500, 9654741594L, "House Rent");

        fundsRequests.add(req1);
        fundsRequests.add(req2);
        fundsRequests.add(req3);
    }

    public void initMyBeneficiaries() {
        myBeneficiaries = new ArrayList<>();
        Beneficiary b1 = new Beneficiary("John Doe", 1234567890L, "State Bank of India");
        Beneficiary b2 = new Beneficiary("Jane Roe", 33421357312L, "Indian Bank");

        myBeneficiaries.add(b1);
        myBeneficiaries.add(b2);
    }

    public ArrayList<FundsRequest> getFundsRequests() {
        return this.fundsRequests;
    }
    public ArrayList<Beneficiary> getMyBeneficiaries() {
        return this.myBeneficiaries;
    }
}
