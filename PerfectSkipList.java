import java.util.*;

class PerfectSkipList extends SkipList {
    public void insert(SkipListNode ins) throws DuplicateKeyException {
        // insert in lowest level, then simply create higher levels on 1/2 principle.
        SkipListNode prev = searchInternal(ins.getKey());
        if (prev.getKey().equals(ins.getKey())) {
            throw new DuplicateKeyException("SkipListNode " + ins + " has duplicate key");
        }

        insertAfter(ins, prev, 0);
        incrementSize();

        // build all the higher levels again.
        rebuild();
    }

    public void delete(String key) throws KeyNotFoundException {
        SkipListNode node = start;
        SkipListNode prev = null;
        int searchLevel = level;
        boolean deleted = false;

        while (searchLevel >= 0) {
            SkipListNode rightSkipListNode = node.getLink(searchLevel);

            if (node.getKey().equals(key)) {
                deleted = true;
                removeNext(prev, searchLevel);
                node = prev;
                searchLevel--;
            } else if (rightSkipListNode.getKey().compareTo(key) <= 0) {
                prev = node;
                node = rightSkipListNode;
            } else {
                searchLevel--;
            }
        }

        if (!deleted) {
            throw new KeyNotFoundException("Cannot find node with key " + key);
        }

        // The rebuild is inefficient and renders the above code redundant, but can be replaced later.
        rebuild();
    }

    // ideally the algorithm should be
    // record 2 insert operations. if deletes are few then just mark the nodes deleted without change to data structure.
    // find the start and end indices of nodes at level 0 whose indices have modified as a result of inserts.
    // this is the range of nodes that need to be updated throughout the levels.
    // this maintains the invariant of 2 lgn search time.
    private void rebuild() {
        // basic implementation of perfect skip list.
        for (SkipListNode node = start; node != end; node = node.getLink(0)) {
            // need to clear out all higher level pointers for all nodes.
            node.keepLink(0);
        }

        int maxLevel = log(getSize());
        for (int level = 1; level <= maxLevel; level++) {
            start.addLink(end);

            SkipListNode prevLevelCurr = start;
            SkipListNode nextLevelCurr = start;

            while (prevLevelCurr != end) {
                SkipListNode next = prevLevelCurr.getLink(level-1);

                if (next == end || next.getLink(level-1) == end) {
                    break;
                }

                insertAfter(next, nextLevelCurr, level);
                nextLevelCurr = next;
                prevLevelCurr = next.getLink(level-1);
            }
        }
    }

    public static void main(String args[]) throws Exception {
        SkipList psl = new PerfectSkipList();

        for (int i = 0; i < 100; i++) {
            psl.insert(new SkipListNode("Key" + i, null));
        }
        psl.print();

        System.out.println(psl.search("Key38"));

        psl.delete("Key38");
        psl.delete("Key58");
        psl.delete("Key21");
        psl.print();
    }
}
