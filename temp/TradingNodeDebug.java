import java.util.HashMap;
import java.util.Map;

public class TradingNodeDebug {


    private static int lowestId;
    private static int currentid;

    private static Map<Integer, Bid> statusMap = new HashMap<>();
    private static Map<String, Integer> withdrawnAmount = new HashMap<>();

    public static void ask(String address, int amount){
        Bid bid = new Bid();
        bid.address = address;
        bid.amount = amount;
        statusMap.put(currentid,bid);
        ++currentid;
    }

    public static int getTotalAmount(){
        int ret = 0;
        for(int y=lowestId;y<currentid;++y)
			ret += statusMap.get(y).amount;
        return ret;
    }

    public static void answer(String address, int amount){
        int totalamt = getTotalAmount();
        if (amount <= totalamt)
			if (amount == totalamt)
				lowestId = currentid;
			else {
				int[] res = findId(amount);
				if(res[0]==0 && res[1]==1) return;
				lowestId = res[0];
				statusMap.get(res[0]).amount -= res[1];
			}
    }

    private static int[] findId(int amount){
        int t=lowestId;
        while (t<currentid) {
			int amtAt = statusMap.get(t).amount;
			if (amtAt == amount)
				return new int[] { t++, amount };
			if (amtAt > amount)
				return new int[] { t, amount };
			amount -= amtAt;
            ++t;
        }
        return new int[]{0,0}; //not found
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

    private static void withdrawAll(String address){
        int amt = findAllowedAmount(address);
        withdrawnAmount.put(address, !withdrawnAmount.containsKey(address) ? amt : withdrawnAmount.get(address) + amt);
    }


    public static String asString(){
        String ret = "";
        int t=lowestId;
        while (t<currentid) {
            ret += "[" + t + ":" + statusMap.get(t).address + ":" + statusMap.get(t).amount + "]";
            ++t;
        }
        return ret+"*";
    }



    public static void main(String[] args){
        String addressA = "x00000A", addressB = "x00000B", addressC = "x00000C", addressD = "x00000D";
        ask(addressA, 213);
        ask(addressB, 2);
        ask(addressC, 5);

        answer(addressD,220);

        System.out.println(asString());
        ask(addressA, 213);
        ask(addressB, 2);
        ask(addressC, 5);
        System.out.println(asString());

        answer(addressD,221);
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
