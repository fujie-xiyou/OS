package sample;

import java.util.Objects;

public class MemBlock {
    private int index;
    private long needSize;
    public  MemBlock(int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
    public long getNeedSize(){
        return needSize;
    }
    public void setNeedSize(long needSize){
        this.needSize = needSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemBlock)) return false;
        MemBlock block = (MemBlock) o;
        return index == block.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
