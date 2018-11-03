package sample;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        BuddySystem buddySystem = new BuddySystem(2);
        List<MemBlock>[] free_area = buddySystem.getFree_area();
        int N = buddySystem.getN();
        List<MemBlock> usedMem = buddySystem.getUsedMem();
        for (int i = 0; i < free_area.length; i++) {
            free_area[i] = new LinkedList<>();
        }
        free_area[N].add(new MemBlock(2));
        for (List list : free_area) {
            System.out.print(list.size() + "  ");
        }
        Scanner s = new Scanner(System.in);

        //输入数字以分配内存
        while (true) {
            long t = s.nextLong();
            if (t == -1) break;
            int n = (int) Math.ceil(MyMath.log2(t));
            if(n > N) {
                System.err.println("超过最大内存!");
                continue;
            }
            buddySystem.f(n, n,t);

            for(int i = 0; i <= N; i++){
                System.out.print(i + "  ");
            }
            System.out.println();
            for (List list : free_area) {
                System.out.print(list.size() + "  ");
            }
            System.out.println("\n已分配的内存信息如下:");
            for (MemBlock block : usedMem){
                System.out.printf("%-4d",block.getIndex());
            }
            System.out.println();
            for (MemBlock block : usedMem){
                System.out.printf("%-4d",block.getNeedSize());
            }

        }

        //输入序号以释放内存
        while (true){
            int index = s.nextInt();
            if (index == -1) break;
            if(usedMem.size() == 0 || index > usedMem.size()) {
                System.err.println("没有这块内存！");
                continue;
            }
            buddySystem.g(usedMem.get(index));

            for (List list : free_area) {
                System.out.print(list.size() + "  ");

            }

        }
        s.close();
    }
}
