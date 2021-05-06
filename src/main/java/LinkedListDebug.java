import java.util.HashMap;
import java.util.Map;

public class LinkedListDebug {

    //1 is reserved for no-result!
	//2 is reserved for not found

    Map<Integer, Node> linkedList = new HashMap<>(); //public for testing only!
    LinkedStackDebug lsb = new LinkedStackDebug();
    private int currentIndex;
    private boolean asc = true; //false for descending

    //===============ADDED CLASSES START HERE===================
    public int getCurrentIndex() {
        return currentIndex;
    }

    public void addValueToNode(String usrAddress, int index, int num){
        TradingNodeDebug tn = linkedList.get(index).tradeNode; //clone?
        tn.placeAsk(usrAddress,num);
        putNode(index, linkedList.get(index).previousIndex, tn);
    }

    public void subtractFromNode(int index, int num){
        TradingNodeDebug tn = linkedList.get(index).tradeNode; //clone?
        boolean answered = tn.answer(num);
        if(!answered) return;
        if(tn.getTotalAmount()==0){
            pop();
        }
        else{
            putNode(index, linkedList.get(index).previousIndex, tn);
        }
    }
    //===============ADDED CLASSES END  HERE===================

    public void setAsc(boolean isAscending) {
        asc = isAscending;
    }

    public void putNode(int index, int previousIndex, TradingNodeDebug tn) {
        Node node = new Node();
        node.previousIndex = previousIndex;
        node.tradeNode = tn;
        linkedList.put(index, node);
    }

    public void push(String usrAddress, int index, int num) {
        if ((asc && index <= currentIndex) || (!asc && index >= currentIndex && currentIndex != 0)) {
            return;
        }
        TradingNodeDebug tn = new TradingNodeDebug();
        tn.placeAsk(usrAddress,num);
        putNode(index, currentIndex, tn);
        currentIndex = index;
    }

    public void pop() {
        if (currentIndex != 0) {
            currentIndex = linkedList.get(currentIndex).previousIndex;
        }
    }

    public void insert(String usrAddress, int index, int num) {
        if (isIndexInChain(index)!=1 && currentIndex != index) {
            if (currentIndex == 0) {
                push(usrAddress, index, num);
            } else if (asc) {
                insertAtAsc(usrAddress, index, num);
            } else {
                insertAtDesc(usrAddress, index, num);
            }
        }
    }

    //is internal
    public void insertAtDesc(String usrAddress, int index, int num) {
        if (asc) return;
        if (currentIndex > index) {
            push(usrAddress, index, num);
            return;
        }

        int closestIndex = findClosestIndexDesc(index);
        if (closestIndex == 1) return;
        if (closestIndex == 0) {
            closestIndex = findPointerIndex(closestIndex);
        }
        if (closestIndex == 1) return;
        TradingNodeDebug tn = new TradingNodeDebug();
        tn.placeAsk(usrAddress,num);
        putNode(index, linkedList.get(closestIndex).previousIndex, tn);
        linkedList.get(closestIndex).previousIndex = index;
    }

    //is internal
    public void insertAtAsc(String usrAddress, int index, int num) {
        if (!asc) return;
        if (currentIndex < index) {
            push(usrAddress, index, num);
            return;
        }

        int closestIndex = findClosestIndexAsc(index);
        if (closestIndex == 1) return;
        TradingNodeDebug tn = new TradingNodeDebug();
        tn.placeAsk(usrAddress,num);
        putNode(index, linkedList.get(closestIndex).previousIndex, tn);
        linkedList.get(closestIndex).previousIndex = index;
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

        if (pointerIndex != 0 && linkedList.get(pointerIndex).previousIndex != 0)
            linkedList.get(pointerIndex).previousIndex = linkedList.get(index).previousIndex;

        if (linkedList.get(pointerIndex).previousIndex == 0)
            return;

        pointerIndex = findPointerIndex(pointerIndex);
        if (pointerIndex != 1)
            linkedList.get(pointerIndex).previousIndex = 0;
    }

    private int findPointerIndex(int index) {
        int atCurrent = currentIndex, prev = linkedList.get(atCurrent).previousIndex, count = 0;
        while (prev != index && prev != 0) {
            if (count >= 10000)
                return 1;
            atCurrent = prev;
            prev = linkedList.get(atCurrent).previousIndex;
            ++count;
        }
        return atCurrent;
    }

    public void truncateAt(int index) {
        if (index != 0 && index != currentIndex && isIndexInChain(index)==1)
            currentIndex = index;
    }

    public int isIndexInChain(int index) {
        if (currentIndex == 0 && index != currentIndex) return 0;
        if (currentIndex == index) return 1;
        int atCurrent = currentIndex, prev = linkedList.get(atCurrent).previousIndex, count = 0;
        while (prev != 0) {
            if (count >= 10000)
                return 2;
            atCurrent = prev;
            prev = linkedList.get(atCurrent).previousIndex;
            if (prev == index || atCurrent == index)
                return 1;
            count++;
        }
        return 0;
    }

    //is internal!
    public int findClosestIndexAsc(int searchIndex) {
        int atCurrent = currentIndex, prev = linkedList.get(atCurrent).previousIndex, count = 0;
        while (prev > searchIndex) {
            if (count >= 10000)
                return 1;
            atCurrent = prev;
            prev = linkedList.get(atCurrent).previousIndex;
            ++count;
        }
        return atCurrent;
    }

    //is internal!
    public int findClosestIndexDesc(int searchIndex) {
        int atCurrent = currentIndex, prev = linkedList.get(atCurrent).previousIndex, count = 0;
        while (prev < searchIndex) {
            if (count >= 10000)
                return 1;
            atCurrent = prev;
            if (atCurrent == 0)
                return 0;
            prev = linkedList.get(atCurrent).previousIndex;
            ++count;
        }
        return atCurrent;
    }


    //#############################################################################################################################################
    public String printList() {
        String out = currentIndex + "";

        for (int iterator = currentIndex; iterator > 0; ) {
            Node node = linkedList.get(iterator);
            int previousIndex = node.previousIndex;
            out += "-->{" + iterator + ":" + previousIndex + ":" + node.tradeNode.getTotalAmount() + "}";
            iterator = previousIndex;
        }
        return out += "-->end";
    }
//
//    //TODO use JSON instead!
//    public void stringToList(String map, boolean asc){
//        linkedList = new HashMap<>(); //public for testing only!
//        this.asc = asc; //false for descending
//        map = map.replaceAll("-", "");
//        String[] sp = map.split(">");
//        currentIndex = Integer.parseInt(sp[0]);
//
//        for(int t=1;t<sp.length-1;t++){
//            String s = sp[t].substring(1,sp[t].length()-1);
//            if(s.startsWith("{")) s=s.substring(1);
//            if(s.endsWith("}")) s = s.substring(s.length()-1);
//            String[] ss = s.split(":");
//            int current = Integer.parseInt(ss[0]);
//            int previous = Integer.parseInt(ss[1]);
//            int value = Integer.parseInt(ss[2]);
//            Node node = new Node();
//            node.previousIndex = previous;
//            TradingNodeDebug tn = new TradingNodeDebug();
//            tn.placeAsk("todo",value);
//            node.tradeNode = tn;
//            linkedList.put(current,node);
//        }
//    }
//
//    public boolean validate(String map, boolean asc) {
//        map = map.replaceAll("-", "");
//        String[] sp = map.split(">");
//
//        if (!"end".equals(sp[sp.length - 1])) {
//            return false;
//        }
//
//        int current = Integer.parseInt(sp[0]);
//        if(current==0){
//            if(!map.equals("0>end")){
//                return false;
//            }
//        }
//
//        for(int t=1;t<sp.length-2;t++){
//            String s = sp[t].substring(1,sp[t].length()-1);
//            String[] ss = s.split(":");
//            int confirm = Integer.parseInt(ss[0]);
//            if(confirm!=current){
//                return false;}
//            int nnext = Integer.parseInt(ss[1]);
//            if(asc){
//                if(!(nnext!=0 && nnext<current)) {
//                    return false;
//                }
//            }
//            else if(!asc){
//                if(!(nnext!=0 && nnext>current)) {
//                    return false;
//                }
//            }
//            current = nnext;
//        }
//
//        return true;
//    }
}

class Node {
    public Integer previousIndex;
    public TradingNodeDebug tradeNode;
}
