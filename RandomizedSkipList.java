import java.util.*;

class RandomizedSkipList extends SkipList {
    // either returns the node with the given key or the location after which to insert.
    public void insert(SkipListNode ins) throws DuplicateKeyException {
        String key = ins.getKey();
        SkipListNode node = start;
        int searchLevel = getLevel();
        Random r = new Random();
        Deque<SkipListNode> insertStack = new ArrayDeque<>();

        while (searchLevel >= 0) {
            SkipListNode rightSkipListNode = node.getLink(searchLevel);

            if (node.getKey().equals(key)) {
                throw new DuplicateKeyException("SkipListNode " + ins + " has duplicate key");
            } else if (rightSkipListNode.getKey().compareTo(key) <= 0) {
                node = rightSkipListNode;
            } else {
                insertStack.push(node);
                searchLevel--;
            }
        }

        node = insertStack.pop();
        insertAfter(ins, node, 0);

        searchLevel = 1;
        while (r.nextInt(2) == 1) {
            if (searchLevel > getLevel()) {
                addLevel();
            }

            if (insertStack.size() > 0) {
                node = insertStack.pop();
            } else {
                node = start;
            }
            insertAfter(ins, node, searchLevel++);
        }
        incrementSize();
    }

    public void delete(String key) throws KeyNotFoundException {
        SkipListNode node = start;
        SkipListNode prev = null;
        int searchLevel = getLevel();
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
    }

    public static void main(String args[]) throws Exception {
        SkipList rsl = new RandomizedSkipList();

        for (int i = 0; i < 100; i++) {
            rsl.insert(new SkipListNode("Key" + i, null));
        }
        rsl.print();

        System.out.println(rsl.search("Key38"));

        rsl.delete("Key38");
        rsl.delete("Key58");
        rsl.delete("Key21");
        rsl.print();
    }
}
