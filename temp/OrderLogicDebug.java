public class OrderLogicDebug {
    //DESCENDING 33-->{33:281:621}-->{281:336:371}-->{336:651:747}-->{651:686:268}-->{686:833:914}-->{833:874:28}-->{874:0:339}-->end, same as ascending from current index

    private LinkedListDebug buyOrders = new LinkedListDebug();
    private LinkedListDebug sellOrders = new LinkedListDebug();

    public OrderLogicDebug(){
        sellOrders.setAsc(false);
    }

    public void placeSellOrder(int price, int amount){
        int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
        if (isInBuy != 2 && isInSell != 2)
			if (sellOrders.getCurrentIndex() == 0 && isInBuy != 1)
				sellOrders.push(price, amount);
			else {
				if (sellOrders.getCurrentIndex() == 0 && isInBuy == 1 || isInSell == 1 && isInBuy == 1)
					return;
				if (isInSell == 1 && isInBuy != 1)
					sellOrders.addValueToNode(price, amount);
				else {
					if (isInSell != 1 && isInBuy == 1)
						return;
					if (isInSell != 1 && isInBuy != 1)
						sellOrders.insert(price, amount);
				}
			}
    }

    public void purchaseSellOrder(int price, int amount){
        if (sellOrders.isIndexInChain(price) == 1 && buyOrders.isIndexInChain(price) != 1)
			sellOrders.subtractFromNode(price, amount);
    }

    public void placeBuyOrder(int price, int amount){
        int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
        if (isInBuy != 2 && isInSell != 2)
			if (buyOrders.getCurrentIndex() == 0 && isInSell != 1)
				buyOrders.push(price, amount);
			else {
				if (buyOrders.getCurrentIndex() == 0 && isInSell == 1 || isInBuy == 1 && isInSell == 1)
					return;
				if (isInBuy == 1 && isInSell != 1)
					buyOrders.addValueToNode(price, amount);
				else {
					if (isInBuy != 1 && isInSell == 1)
						return;
					if (isInBuy != 1 && isInSell != 1)
						buyOrders.insert(price, amount);
				}
			}
    }

    public void purchaseBuyOrder(int price, int amount){
        if (buyOrders.isIndexInChain(price) == 1 && sellOrders.isIndexInChain(price) != 1)
			sellOrders.subtractFromNode(price, amount);
    }

}
