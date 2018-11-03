package sample;

import sample.MemBlock;
import sample.MyMath;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BuddySystem {
    private static final int N = 2;
    private static boolean[] map = new boolean[1 + 2 * (int)Math.pow(2,N)];
    private static List<MemBlock> usedMem = new LinkedList<>();
    private static List<MemBlock>[] free_area = new List[N + 1];

    public static void main(String[] args) {
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
            f(n, n,t);

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
            g(usedMem.get(index));

            for (List list : free_area) {
                System.out.print(list.size() + "  ");

            }

        }
        s.close();
    }

    // 分配算法
    public static boolean f(int i, int n,long needSize) {
        if(i > N) {
            System.err.println("内存不足！");
            return false;
        }
        if (free_area[i].size() == 0) {
            if(f(i + 1, n,needSize)) {
                //上一个调用返回true后 当前就会有合适的块了  再分配一次
                return f(i, n,needSize);
            }
        } else { // 当前有大小大于所需的内存块
            if (i > n) { // i大于n 说明当前的内存块还是大于预期大小 那就应该标记 分割 回退
                MemBlock superBlock = free_area[i].remove(0);
                map[superBlock.getIndex()] = true;
                free_area[i - 1].add(new MemBlock(superBlock.getIndex() * 2 - 1));
                free_area[i - 1].add(new MemBlock(superBlock.getIndex() * 2 ));
            } else if (i == n) {// i==n 说明当前i代表的内存块大小就是需要的大小，执行分配
                MemBlock block = free_area[i].remove(0);
                map[block.getIndex()] = true;
                block.setNeedSize(needSize);
                usedMem.add(block);

            }
            return true;
        }
        return false;
    }

    //释放算法
    public static void g(MemBlock block){
        usedMem.remove(block);
        int buddyIndex;
        int index = block.getIndex();
        if(index == 2){
            buddyIndex = 2;
        }else if(index % 2 == 0){ //偶数 说明他的伙伴是自身-1
            buddyIndex = index - 1;
        }else { // 奇数  说明他的伙伴是自身+1
            buddyIndex = index + 1;
        }
        if(map[buddyIndex]) { // 伙伴被分配了 那就把自身直接加入到对应的链表
            int n = N - (int) Math.ceil(MyMath.log2(index)) + 1;
            System.out.print("(" + n +")");
            block.setNeedSize(0);
            free_area[n].add(block);

        }else { // 伙伴没有被分配 因此进行合并 并且尝试释放合并后的块
            int n = N - (int) Math.ceil(MyMath.log2(buddyIndex)) + 1;
            if(n < N) {
                free_area[n].remove(new MemBlock(buddyIndex));
                int superIndex = (index + 1) / 2;
                g(new MemBlock(superIndex));
            }
        }
        map[index] = false;
    }

}
