package sample;

import java.util.LinkedList;
import java.util.List;

public class BuddySystem {
    private int N;
    private boolean[] map;
    private List<MemBlock> usedMem;
    private List<MemBlock>[] free_area;
    public BuddySystem(int N){
        this.N = N;
        map = new boolean[1 + 2 * (int)Math.pow(2,N)];
        usedMem = new LinkedList<>();
        free_area = new List[N + 1];
    }
    public int getN() {
        return N;
    }

    public boolean[] getMap() {
        return map;
    }

    public List<MemBlock> getUsedMem() {
        return usedMem;
    }

    public List<MemBlock>[] getFree_area() {
        return free_area;
    }

    // 分配算法
    public boolean f(int i, int n,long needSize) {
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
    public void g(MemBlock block){
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
