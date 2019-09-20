package t18.gradhack.com.res;

import java.util.ArrayList;
import java.util.List;

import t18.gradhack.com.generics.FundsRequest;

public class FakeData {
    ArrayList<FundsRequest> fundsRequests;

    public void initFundsRequests() {
        FundsRequest req1 = new FundsRequest(1, "Sujay", 11001678321L, 100, 8010761583L, "Lunch");
        FundsRequest req2 = new FundsRequest(2, "Aman", 33892011180L, 5000, 9654741594L, "Drinks");
        FundsRequest req3 = new FundsRequest(3, "Aman", 33892011180L, 15500, 9654741594L, "House Rent");

        fundsRequests = new ArrayList<>();
        fundsRequests.add(req1);
        fundsRequests.add(req2);
        fundsRequests.add(req3);
    }

    public ArrayList<FundsRequest> getFundsRequests() {
        return this.fundsRequests;
    }
}
