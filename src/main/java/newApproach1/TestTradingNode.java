package newApproach1;

import com.google.gson.Gson;
import newApproach1.TradingNode.TradingNode;

public class TestTradingNode {

    public static void main(String[] args){
        Gson gson = new Gson();
        //setup some assets
        TradingNode tn = new TradingNode();
        tn.pricePerNativeToken = 2000;
        tn.nonNativeAddress = "nn_addr";

        tn.bank.addAssets("user_krk", "native",1000000000);
        tn.bank.addAssets("user_krk", "nn_addr",1000000000);


        System.out.println(gson.toJson(tn.bank));

        tn.sellNative("user_krk",1000);
        System.out.println(gson.toJson(tn.bank));

        tn.cancelSellNativeOrder("user_krk");
        System.out.println(gson.toJson(tn.bank));




        tn.buyNative("user_krk",3000);
        System.out.println(gson.toJson(tn.bank));

        tn.cancelBuyNativeOrder("user_krk");
        System.out.println(gson.toJson(tn.bank));


//
//        tn.bank.addAssets("user_krk2", "native",1000000000);
//        tn.bank.addAssets("user_krk2", "nn_addr",1000000000);
//        tn.sellNative("user_krk2",2500);
//        System.out.println(gson.toJson(tn.bank));
//
//        tn.cancelSellOrder("user_krk2");
//        tn.cancelBuyOrder("user_krk2");
//        System.out.println(gson.toJson(tn.bank));


    }


}
