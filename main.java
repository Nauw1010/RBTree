import RBTree.RBTree;

import java.util.*;

public class main {
    public static void main(String[] args) {
        System.out.println("This is RBTree main method");

        Random random = new Random(633);
        List<Integer> a = new ArrayList<>();
        boolean[] flag= new boolean[31];


        RBTree tree = new RBTree();
        for(int i = 0; i < 10; i++) {
            int tmp;
            do {
                tmp = random.nextInt(30);
            }while (flag[tmp]);
            flag[tmp] = true;
            a.add(tmp);
            tree.insert(tmp);
        }

        for (int i = 0; i < 10; i++) {
            System.out.print(a.get(i) + " ");
        }

        Collections.shuffle(a);
        System.out.print('\n');

        for (int i = 0; i < 5; i++) {
            System.out.print(a.get(i) + " ");
            tree.remove(a.get(i));
        }

        System.out.println("\n---tree---");

        tree.inOrder();
    }
}
