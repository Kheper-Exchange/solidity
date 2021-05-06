import java.util.HashMap;
import java.util.Map;

public class LinkedStackDebug {

    //1 is reserved for no-result!
	//2 is reserved for not found

    final Map<Integer, StackNode> linkedStack = new HashMap<>(); //public for testing only!
    private int currentIndex;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void addValueToNode(String usrAddress, int index, int num){
        TradingNodeDebug tn = linkedStack.get(index).tradeNode; //clone?
        tn.placeAsk(usrAddress,num);
        putNode(index, linkedStack.get(index).previousIndex, tn);
    }

    public void subtractFromNode(int index, int num){
        TradingNodeDebug tn = linkedStack.get(index).tradeNode; //clone?
        boolean answered = tn.answer(num);
        if(!answered) return;
        if(tn.getTotalAmount()==0){
            pop();
        }
        else{
            putNode(index, linkedStack.get(index).previousIndex, tn);
        }
    }


    public void putNode(int index, int previousIndex, TradingNodeDebug tn) {
        StackNode node = new StackNode();
        node.previousIndex = previousIndex;
        node.tradeNode = tn;
        linkedStack.put(index, node);
    }

    public void push(String usrAddress, int index, int num) {
        if (index <= currentIndex || currentIndex != 0) {
            return;
        }
        TradingNodeDebug tn = new TradingNodeDebug();
        tn.placeAsk(usrAddress,num);
        putNode(index, currentIndex, tn);
        currentIndex = index;
    }

    public void pop() {
        if (currentIndex != 0) {
            currentIndex = linkedStack.get(currentIndex).previousIndex;
        }
    }



    public void remove(int index) {
        if (index == 0 || currentIndex == 0 || isIndexInChain(index)!=1)
            return;
        if (currentIndex == index) {
            pop();
            return;
        }
        int pointerIndex = findPointerIndex(index);
        if (pointerIndex == 1) return;

        if (pointerIndex != 0 && linkedStack.get(pointerIndex).previousIndex != 0)
            linkedStack.get(pointerIndex).previousIndex = linkedStack.get(index).previousIndex;

        if (linkedStack.get(pointerIndex).previousIndex == 0)
            return;

        pointerIndex = findPointerIndex(pointerIndex);
        if (pointerIndex != 1)
            linkedStack.get(pointerIndex).previousIndex = 0;
    }

    private int findPointerIndex(int index) {
        int atCurrent = currentIndex, prev = linkedStack.get(atCurrent).previousIndex, count = 0;
        while (prev != index && prev != 0) {
            if (count >= 10000)
                return 1;
            atCurrent = prev;
            prev = linkedStack.get(atCurrent).previousIndex;
            ++count;
        }
        return atCurrent;
    }



    public int isIndexInChain(int index) {
        if (currentIndex == 0 && index != currentIndex) return 0;
        if (currentIndex == index) return 1;
        int atCurrent = currentIndex, prev = linkedStack.get(atCurrent).previousIndex, count = 0;
        while (prev != 0) {
            if (count >= 10000)
                return 2;
            atCurrent = prev;
            prev = linkedStack.get(atCurrent).previousIndex;
            if (prev == index || atCurrent == index)
                return 1;
            count++;
        }
        return 0;
    }





    //#############################################################################################################################################
    public String printList() {
        String out = currentIndex + "";

        for (int iterator = currentIndex; iterator > 0; ) {
            StackNode node = linkedStack.get(iterator);
            int previousIndex = node.previousIndex;
            out += "-->{" + iterator + ":" + previousIndex + ":" + node.tradeNode.getTotalAmount() + "}";
            iterator = previousIndex;
        }
        return out += "-->end";
    }

}

class StackNode {
    public Integer previousIndex;
    public TradingNodeDebug tradeNode;
}
