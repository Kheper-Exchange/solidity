public class OrderLogicDebug {
    //DESCENDING 33-->{33:281:621}-->{281:336:371}-->{336:651:747}-->{651:686:268}-->{686:833:914}-->{833:874:28}-->{874:0:339}-->end, same as ascending from current index

    private LinkedListDebug buyOrders = new LinkedListDebug();
    private LinkedListDebug sellOrders = new LinkedListDebug();

    public OrderLogicDebug(){
        sellOrders.setAsc(false);
    }

    public int getPriceGap(){
        int sellIndex = sellOrders.getCurrentIndex(), buyIndex = buyOrders.getCurrentIndex();
        return sellIndex < buyIndex ? buyIndex - sellIndex : sellIndex - buyIndex;
    }

    public void placeSellOrder(int price, int amount){
        int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
        if(isInBuy==2 || isInSell==2) return; //unreachable


        if (sellOrders.getCurrentIndex() == 0 && isInBuy != 1) {
			sellOrders.insert(price, amount);
		} else if (isInSell == 1 && isInBuy != 1) {
			sellOrders.addValue(price, amount);
		} else if (isInSell != 1 && isInBuy == 1) {
			System.out.println("process inBuy, get difference, truncate or subtract, place inSell");
		} else if (isInSell != 1 && isInBuy != 1) {
			sellOrders.insert(price, amount);
		}
    }

    public void placeBuyOrder(int price, int amount){
        int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
        if(isInBuy==2 || isInSell==2) return; //unreachable


        if (buyOrders.getCurrentIndex() == 0 && isInSell != 1) {
			buyOrders.insert(price, amount);
		} else if (isInBuy == 1 && isInSell != 1) {
			buyOrders.addValue(price, amount);
		} else if (isInBuy != 1 && isInSell == 1) {
			System.out.println("process inBuy, get difference, truncate or subtract, place inSell");
		} else if (isInBuy != 1 && isInSell != 1) {
			buyOrders.insert(price, amount);
		}
    }

}
