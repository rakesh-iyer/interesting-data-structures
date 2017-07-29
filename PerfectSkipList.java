import java.util.*;

class PerfectSkipList {
    int level;
    SkipListNode start = new SkipListNode("", new Data());
    SkipListNode end = new SkipListNode("LARGESTKEYEVER", new Data());
    int size;

    PerfectSkipList() {
        start.addLink(end);
    }

    int getSize() {
        return size;
    }

    void incrementSize() {
        size++;
        if (log(size) > level) {
            level = log(size);
            start.addLink(end);
        }
    }

    int getLevel() {
        return level;
    }

    void decrementSize() {
        size--;
        if (log(size) > level) {
            start.removeLink(level);
            level = log(size);
        }
    }

    static int log(int length) {
        if (length == 0) {
            throw new ArithmeticException("Log of 0 is invalid");
        }

        int bit;

        // find max 1 bit position.
        for (bit = 0; length > 1; length >>= 1) {
            bit++;
        }

        return bit;
    }

    // either returns the node with the given key or the location after which to insert.
    private SkipListNode searchInternal(String key) {
        SkipListNode node = start;
        int searchLevel = level;

        while (searchLevel >= 0) {
            SkipListNode rightSkipListNode = node.getLink(searchLevel);

            if (node.getKey().equals(key)) {
                break;
            } else if (rightSkipListNode.getKey().compareTo(key) <= 0) {
                node = rightSkipListNode;
            } else {
                searchLevel--;
            }
        }

        return node;
    }

    SkipListNode search(String key) {
        SkipListNode node = searchInternal(key);

        if (node.getKey().equals(key)) {
            return node;
        } else {
            return null;
        }
    }

    void insertAfter(SkipListNode ins, SkipListNode curr, int level) {
        SkipListNode next = curr.getLink(level);

        curr.setLink(ins, level);
        ins.addLink(next);
    }

    void removeNext(SkipListNode node, int level) {
        SkipListNode del = node.getLink(level);
        node.setLink(del.getLink(level), level);
    }

    void insert(SkipListNode ins) throws DuplicateKeyException {
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

    void delete(String key) throws KeyNotFoundException {
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
    void rebuild() {
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

    void print() {
        System.out.println("In printing");
        for (int i = 0; i <= level; i++) {
            System.out.print("Level " + i + ": ");
            for (SkipListNode node = start.getLink(i); node != end; node = node.getLink(i)) {
                System.out.print(node.getKey() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) throws Exception {
        PerfectSkipList psl = new PerfectSkipList();

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
