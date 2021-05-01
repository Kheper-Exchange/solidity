import java.util.HashMap;
import java.util.Map;





public class TradingNodeDebug {

    private static int lowestId;
    private static int currentId;

    private static Map<Integer, Bid> statusMap = new HashMap<>();
    private static Map<String, Integer> withdrawnAmount = new HashMap<>();

    public static boolean placeAsk(String usrAddress, int amount){
        Bid bid = new Bid();
        bid.address = usrAddress;
        bid.amount = amount;
        statusMap.put(currentId,bid);
        ++currentId;
        return true;
    }

    public static boolean answer(int amount){
        if(!(amount>0)) return false; //require
        int totalamt = getTotalAmount();

        if (!(amount <= totalamt)) return false; //require
        if(!((totalamt-amount)>(totalamt/100))) return false; //require

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

    public static int getTotalAmount(){
        int ret = 0;
        for(int y = lowestId; y< currentId; ++y) {
            if(y==10000) return 0;
            ret += statusMap.get(y).amount;
        }
        return ret;
    }



    private static int findId(int amount){
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

    private static int findAmount(int amount){
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



    private static int findAllowedAmount(String address){
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

    private static boolean withdrawAll(String usrAddress){
        int amt = findAllowedAmount(usrAddress);
        if(amt!=0) {
            withdrawnAmount.put(usrAddress, withdrawnAmount.get(usrAddress)+amt);
        }
        return true;
    }

//-----------------------------------------------------------
    public static String asString(){
        String ret = "";
        int t=lowestId;
        while (t< currentId) {
            ret += "[" + t + ":" + statusMap.get(t).address + ":" + statusMap.get(t).amount + "]";
            ++t;
        }
        return ret+"*";
    }


    public static void main(String[] args){
        String addressA = "x00000A", addressB = "x00000B", addressC = "x00000C", addressD = "x00000D";
        placeAsk(addressA, 213);
        placeAsk(addressB, 2);
        placeAsk(addressC, 5);

        answer(220);

        System.out.println(asString());
        placeAsk(addressA, 213);
        placeAsk(addressB, 2);
        placeAsk(addressC, 5);
        System.out.println(asString());

        answer(221);
        System.out.println(asString());
        System.out.println(findAllowedAmount(addressA));
        withdrawAll(addressA);
        System.out.println(findAllowedAmount(addressA));

    }

}

class Bid{
    String address;
    Integer amount;
}

