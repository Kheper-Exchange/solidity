import java.util.Random;

public class TestLListDBG {



    private static Random rnd = new Random();
    private static  LinkedListDebug lld = new LinkedListDebug();

    public static void main(String[] args) {
            lld.setAsc(true);

        while (true) {
            int rint = rnd.nextInt(5) + 1;
            if (rint == 1)
                action1();
            if (rint == 2)
                action2();
            if (rint == 3)
                action3();
            if (rint == 4)
                action4();
            if (rint == 5)
                action5();

            String map = lld.printList();
            boolean validate = lld.validate(map);
            if (!validate) {
                System.out.println(rint);
                System.out.println(map);
                break;
            }


        }


    }


    public static  void action1() {
        System.out.println("---------------");
        int a = rnd.nextInt(1000) + 2, b = rnd.nextInt(1000) + 2;
        System.out.println("Pushing index:" + a + " value:" + b + " - " + lld.printList());
        lld.push(a, b);
        System.out.println("Result - " + lld.printList());
    }

    public static  void action2() {
        System.out.println("---------------");
        System.out.println("Popping - " + lld.printList());
        lld.pop();
        System.out.println("Result - " + lld.printList());
    }

    public static  void action3() {
        System.out.println("---------------");
        int a = rnd.nextInt(1000) + 2, b = rnd.nextInt(1000) + 2;
        System.out.println("Inserting index:" + a + " value:" + b + " - " + lld.printList());
        lld.insert(a, b);
        System.out.println("Result - " + lld.printList());
    }

    public static  void action4() {
        System.out.println("---------------");
        int a = randomChainIndex();
        System.out.println("Removing index:" + a + " - " + lld.printList());
        lld.remove(a);
        System.out.println("Result - " + lld.printList());
    }

    public static  void action5() {
        System.out.println("---------------");
        int a = randomChainIndex();
        System.out.println("Truncating index:" + a + " - " + lld.printList());
        lld.truncateAt(a);
        System.out.println("Result - " + lld.printList());
    }

    public static  int randomChainIndex() {
        if (lld.getCurrentIndex() != 0) {
            int atCurrent = lld.getCurrentIndex(), prev = lld.linkedList.get(atCurrent).previousIndex;
            while (prev != 0) {
                atCurrent = prev;
                prev = lld.linkedList.get(atCurrent).previousIndex;
                if (rnd.nextBoolean())
                    return !rnd.nextBoolean() ? prev : atCurrent;
            }
        }
        return rnd.nextInt(1000) + 2;
    }
}
