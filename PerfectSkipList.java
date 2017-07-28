import java.util.*;

class PerfectSkipList {
    int level;
    Node start = new Node("", new Data());
    Node end = new Node("LARGESTKEYEVER", new Data());
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
    private Node searchInternal(String key) {
        Node node = start;
        int searchLevel = level;

        while (searchLevel >= 0) {
            Node rightNode = node.getLink(searchLevel);

            if (node.getKey().equals(key)) {
                return node;
            } else if (rightNode.getKey().compareTo(key) <= 0) {
                node = rightNode;
            } else {
                searchLevel--;
            }
        }

        return node;
    }

    Node search(String key) {
        Node node = searchInternal(key);

        if (node.getKey().equals(key)) {
            return node;
        } else {
            return null;
        }
    }

    void insertAfter(Node ins, Node curr, int level) {
        Node next = curr.getLink(level);

        curr.setLink(ins, level);
        ins.addLink(next);
    }

    void insert(Node ins) throws DuplicateKeyException {
        // insert in lowest level, then simply create higher levels on 1/2 principle.
        Node prev = searchInternal(ins.getKey());
        if (prev.getKey().equals(ins.getKey())) {
            throw new DuplicateKeyException("Node " + ins + " has duplicate key");
        }

        insertAfter(ins, prev, 0);
        incrementSize();

        // build all the higher levels again.
        rebuild();
    }

    void delete(String key) throws KeyNotFoundException {
        Node del = search(key);

        if (del == null) {
            throw new KeyNotFoundException("Cannot find node with key " + key);
        }

        // you will find the node at the highest level, so just follow lower link and delete from each level.
        // need a doubly linked list for log n time deletion.
    }

    void rebuild() {
        int maxLevel = log(getSize());
        for (int level = 1; level < maxLevel; level++) {
            Node prevLevelCurr = start;
            Node nextLevelCurr = start;

            while (prevLevelCurr != end) {
                Node next = prevLevelCurr.getLink(level-1);

                if (next == end) {
                    break;
                }

                insertAfter(next, nextLevelCurr, level);
                nextLevelCurr = next;
                prevLevelCurr = next.getLink(level-1);
            }
        }
    }

    void print() {
        for (int i = 0; i <= level; i++) {
            System.out.print("Level " + i + ": ");
            for (Node node = start.getLink(i); node != end; node = node.getLink(i)) {
                System.out.print(node.getKey() + " ");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) throws Exception {
        PerfectSkipList psl = new PerfectSkipList();

        for (int i = 0; i < 100; i++) {
            psl.insert(new Node("Key" + i, null));
        }
        psl.print();
    }
}
