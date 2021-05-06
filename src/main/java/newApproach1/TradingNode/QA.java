package newApproach1.TradingNode;

import java.util.HashMap;
import java.util.Map;

public class QA {

    private Map<Integer, Bid> statusMap = new HashMap<>();
    private Map<String, Integer> withdrawnAmount = new HashMap<>();
    private int lowestId;
    private int currentId;

    public boolean ask(String usrAddress, int amount){
        Bid bid = new Bid();
        bid.address = usrAddress;
        bid.amount = amount;
        statusMap.put(currentId,bid);
        ++currentId;
        return true;
    }

    public boolean answer(int amount){
        if(!(amount>0)) return false; //require
        int totalamt = getTotalAmount();

        if (!(amount <= totalamt)) return false; //require
        if(!((totalamt-amount)>(totalamt/100)) && (totalamt-amount!=0)) return false; //require

        if (amount == totalamt) {
            lowestId = currentId;
        } else {
            int res0 = findId(amount);
            int res1 = findAmount(amount);
            if(res0==0 && res1==0) return false; //require
            lowestId = res0;
            statusMap.get(res0).amount -= res1;
        }
        return true;
    }

    //-----------------------------------------------------------

    public int getTotalAmount(){
        int ret = 0;
        for(int y = lowestId; y< currentId; ++y) {
            if(y==10000) return 0;
            ret += statusMap.get(y).amount;
        }
        return ret;
    }



    private int findId(int amount){
        int t=lowestId;
        while (t< currentId) {
            if(t==10000) return 0; //not found
            int amtAt = statusMap.get(t).amount;
            if (amtAt == amount)
                return t++;
            if (amtAt > amount)
                return t;
            amount -= amtAt;
            ++t;
        }
        return 0; //not found
    }

    private int findAmount(int amount){
        int t=lowestId;
        while (t< currentId) {
            if(t==10000) return 0; //not found
            int amtAt = statusMap.get(t).amount;
            if (amtAt == amount)
                return amount;
            if (amtAt > amount)
                return amount;
            amount -= amtAt;
            ++t;
        }
        return 0; //not found
    }



    private int findAllowedAmount(String address){
        int ret = 0;
        int t=0;
        while (t<lowestId) {
            if (address.equals(statusMap.get(t).address))
                ret += statusMap.get(t).amount;
            ++t;
        }
        if(withdrawnAmount.containsKey(address))
            ret -= withdrawnAmount.get(address);
        return ret;
    }

    private boolean withdrawAll(String usrAddress){
        int amt = findAllowedAmount(usrAddress);
        if(amt!=0) {
            withdrawnAmount.put(usrAddress, withdrawnAmount.get(usrAddress)+amt);
        }
        return true;
    }

}
