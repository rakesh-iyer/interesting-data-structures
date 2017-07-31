abstract class SkipList {
    SkipListNode start = new SkipListNode("", new Data());
    SkipListNode end = new SkipListNode("LARGESTKEYEVER", new Data());
    int size;

    SkipList() {
        start.addLink(end);
    }

    int getSize() {
        return size;
    }

    void incrementSize() {
        size++;
    }

    void decrementSize() {
        size--;
    }

    void addLevel() {
        start.addLink(end);
    }

    int getLevel() {
        return start.getLinkCount() - 1;
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

    protected void insertAfter(SkipListNode ins, SkipListNode curr, int level) {
        SkipListNode next = curr.getLink(level);

        curr.setLink(ins, level);
        ins.addLink(next);
    }

    protected void removeNext(SkipListNode node, int level) {
        SkipListNode del = node.getLink(level);
        node.setLink(del.getLink(level), level);
    }

    // either returns the node with the given key or the location after which to insert.
    protected SkipListNode searchInternal(String key) {
        SkipListNode node = start;
        int searchLevel = getLevel();

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

    public SkipListNode search(String key) {
        SkipListNode node = searchInternal(key);

        if (node.getKey().equals(key)) {
            return node;
        } else {
            return null;
        }
    }

    void print() {
        System.out.println("In printing");
        for (int i = 0; i <= getLevel(); i++) {
            System.out.print("Level " + i + ": ");
            for (SkipListNode node = start.getLink(i); node != end; node = node.getLink(i)) {
                System.out.print(node.getKey() + " ");
            }
            System.out.println();
        }
    }

    abstract void insert(SkipListNode node) throws DuplicateKeyException;
    abstract void delete(String key) throws KeyNotFoundException;
}
