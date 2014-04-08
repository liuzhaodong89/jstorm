package storm.trident.tuple;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;


public class ComboList extends AbstractList<Object> {    
    public static class Factory implements Serializable {
        Pointer[] index;
        
        public Factory(int... sizes) {
            int total = 0;
            for(int size: sizes) {
                total+=size;
            }
            index = new Pointer[total];
            int i=0;
            int j=0;
            for(int size: sizes) {
                for(int z=0; z<size; z++) {
                    index[j] = new Pointer(i, z);
                    j++;
                }
                i++;
            }
        }
        
        public ComboList create(List[] delegates) {
            return new ComboList(delegates, index);
        }
    }
    
    private static class Pointer implements Serializable {
        int listIndex;
        int subIndex;
        
        public Pointer(int listIndex, int subIndex) {
            this.listIndex = listIndex;
            this.subIndex = subIndex;
        }
        
    }
    
    Pointer[] _index;
    List[] _delegates;
    
    public ComboList(List[] delegates, Pointer[] index) {
        _index = index;
        _delegates = delegates;
    }
            
    @Override
    public Object get(int i) {
        Pointer ptr = _index[i];
        return _delegates[ptr.listIndex].get(ptr.subIndex);
    }

    @Override
    public int size() {
        return _index.length;
    }
}
