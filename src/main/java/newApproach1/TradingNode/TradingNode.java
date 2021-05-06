package newApproach1.TradingNode;

import newApproach1.Bank.Bank;

//if I place native and ask for non-native, I am selling native and buying non-native
//if I place non-native and ask for native, I am buying native and selling non-native
//I can only buy or sell native


public class TradingNode {


    public int pricePerNativeToken; //how many tokens does 1 native token costs. The zeros included! if price=2000..., then 1000...n = 2000...nn
    public int nativeToken = 1000; //lets assume native token has 3 zeros
    public String nativeAddress = "native"; //is contract and token address!?
    public String nonNativeAddress;
    public Bank bank = new Bank(); //FOR INTERNAL PURPOSES
    public QA buy = new QA();
    public QA sell = new QA();


    public void setPrice(int num, String nonNativeAddress){
        this.nonNativeAddress = nonNativeAddress;
        this.pricePerNativeToken = num;
    }

    public boolean buyNative(String usrAddress, int nonNativeAmount){
        int nativeAmount = toNative(nonNativeAmount);
        if(nativeAmount==0) return false;
        placeBuy(usrAddress, nativeAmount, nonNativeAmount);
        //send to contract
        sendTo(usrAddress, nativeAddress, nonNativeAddress, nonNativeAmount);
        return true;
    }

    public boolean sellNative(String usrAddress, int nativeAmount){
        int nonNativeAmount = toNonNative(nativeAmount);
        if(nonNativeAmount==0) return false;
        placeSell(usrAddress, nativeAmount, nonNativeAmount);
        //send to contract
        sendTo(usrAddress, nativeAddress, nativeAddress, nativeAmount);
        return true;
    }

    private int toNative(int nonNativeAmount){
        int nativeAmount = (nonNativeAmount*nativeToken)/pricePerNativeToken;
        return nativeAmount;
    }
    private int toNonNative(int nativeAmount){
        int nonNativeAmount = (nativeAmount*pricePerNativeToken)/nativeToken;
        return nonNativeAmount;
    }

    //if buy, and then sell exists, answer sell then ask buy with a remainder
    //if buy, and then sell does not exist, ask buy
    private boolean placeBuy(String userAddress, int nativeAmount, int nonNativeAmount){
        int amt = sell.getTotalAmount();
        if(amt!=0){
            if(amt >= nativeAmount){
                sell.answer(nativeAmount);
                sendTo(nativeAddress, userAddress, nativeAddress, nativeAmount);
            }
            else{
                int answer = amt;
                if(!sell.answer(answer)) return false; //require
                if(!buy.ask(userAddress,nativeAmount)) return false; //require
                sendTo(nativeAddress, userAddress, nativeAddress, answer);
            }
        }
        else{
            buy.ask(userAddress,nativeAmount);
        }
        return true;
    }

    //if sell and then buy exists, answer buy and then ask sell with a remainder
    //if sell and then buy does not exist, ask sell
    private boolean placeSell(String userAddress, int nativeAmount, int nonNativeAmount){
        int amt = buy.getTotalAmount();
        if(amt!=0){
            if(amt >= nativeAmount){
                buy.answer(nativeAmount);
                sendTo(nativeAddress, userAddress, nonNativeAddress, nonNativeAmount);
            }
            else{
                int answer = amt;
                int nn = toNonNative(answer);
                if(!buy.answer(answer)){return false;}
                if(!sell.ask(userAddress,nativeAmount)){return false;};
                sendTo(nativeAddress, userAddress, nonNativeAddress, nn);

            }
        }
        else{
            sell.ask(userAddress,nativeAmount);
        }
        return true;
    }


    public boolean cancelBuyNativeOrder(String userAddress){
        int total = buy.getTotalAmount();
        buy.answer(total);
        int nn = toNonNative(total);
        sendTo(nativeAddress, userAddress, nonNativeAddress, nn);
        return true;
    }

    public boolean cancelSellNativeOrder(String userAddress){
        int total = sell.getTotalAmount();
        sell.answer(total);
        sendTo(nativeAddress, userAddress, nativeAddress, total);
        return true;
    }


    private void sendTo(String fromAddress, String toAddress, String tokenAddress, int amount) {
        int fromAssets = bank.getAssets(fromAddress,tokenAddress);
        if(fromAssets>=amount) {
            bank.addAssets(toAddress, tokenAddress, amount);
            bank.subtractAssets(fromAddress, tokenAddress, amount);
        }
    }



//TODO cancel order!
//this can be the getTotal for address, and then simply answer the total, then withdrawAll
    

   //---------------------------------------



}
