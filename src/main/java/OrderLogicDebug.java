public class OrderLogicDebug {
//    //DESCENDING 33-->{33:281:621}-->{281:336:371}-->{336:651:747}-->{651:686:268}-->{686:833:914}-->{833:874:28}-->{874:0:339}-->end, same as ascending from current index
//    //ASCENDING 587-->{587:432:427}-->{432:348:582}-->{348:235:665}-->{235:0:473}-->end
//
//      final LinkedListDebug buyOrders = new LinkedListDebug();
//      final LinkedListDebug sellOrders = new LinkedListDebug();
//
//    public OrderLogicDebug(){
//        sellOrders.setAsc(false);
//    }
//
//
//    public void buy(int price, int amount){
//		int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
//		if (isInBuy != 2 && isInSell != 2 && (isInBuy != 1 || isInSell != 1))
//			if (isInBuy == 1)
//				placeBuyOrder(price, amount);
//			else if (isInSell == 1)
//				purchaseSellOrder(price, amount);
//	}
//
//    public void sell(int price, int amount){
//		int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
//		if (isInSell != 2 && isInBuy != 2 && (isInSell != 1 || isInBuy != 1))
//			if (isInSell == 1)
//				placeSellOrder(price, amount);
//			else if (isInBuy == 1)
//				purchaseBuyOrder(price, amount);
//	}
//
//
//    private void placeSellOrder(int price, int amount){
//        int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
//        if (isInBuy != 2 && isInSell != 2)
//			if (sellOrders.getCurrentIndex() == 0 && isInBuy != 1)
//				sellOrders.push(price, amount);
//			else {
//				if (sellOrders.getCurrentIndex() == 0 && isInBuy == 1 || isInSell == 1 && isInBuy == 1)
//					return;
//				if (isInSell == 1)
//					sellOrders.addValueToNode(price, amount);
//				else {
//					if (isInBuy == 1)
//						return;
//					sellOrders.insert(price, amount);
//				}
//			}
//    }
//
//
//
//    private void placeBuyOrder(int price, int amount){
//        int isInSell = sellOrders.isIndexInChain(price), isInBuy = buyOrders.isIndexInChain(price);
//        if (isInBuy != 2 && isInSell != 2)
//			if (buyOrders.getCurrentIndex() == 0 && isInSell != 1)
//				buyOrders.push(price, amount);
//			else {
//				if (buyOrders.getCurrentIndex() == 0 && isInSell == 1 || isInBuy == 1 && isInSell == 1)
//					return;
//				if (isInBuy == 1)
//					buyOrders.addValueToNode(price, amount);
//				else {
//					if (isInSell == 1)
//						return;
//					buyOrders.insert(price, amount);
//				}
//			}
//    }
//
//	private void purchaseSellOrder(int price, int amount){
//		if (sellOrders.isIndexInChain(price) == 1 && buyOrders.isIndexInChain(price) != 1)
//			sellOrders.subtractFromNode(price, amount);
//	}
//
//    private void purchaseBuyOrder(int price, int amount){
//        if (buyOrders.isIndexInChain(price) == 1 && sellOrders.isIndexInChain(price) != 1)
//			buyOrders.subtractFromNode(price, amount);
//    }
//
//    //###############################################33
//	public String getBuyOrdersStr(){
//		return buyOrders.printList();
//	}
//
//	public String getSellOrdersStr(){
//		return buyOrders.printList();
//	}

}
