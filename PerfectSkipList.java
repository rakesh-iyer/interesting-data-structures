import java.util.*;

class PerfectSkipList {
    int level;
    Node start = new Node("", new Data());
    Node end = new Node("LARGESTKEYEVER", new Data());
    int size;

    int getSize() {
        return size;
    }

    void incrementSize() {
        size++;
    }

    void decrementSize() {
        size--;
    }

    static class DuplicateKeyException extends Exception {
        String exceptionString;

        DuplicateKeyException(String exceptionString) {
            this.exceptionString = exceptionString;
        }

        public String toString() {
            return exceptionString;
        }
    }

    static class KeyNotFoundException extends Exception {
        String exceptionString;

        KeyNotFoundException(String exceptionString) {
            this.exceptionString = exceptionString;
        }

        public String toString() {
            return exceptionString;
        }
    }

    void PerfectSkipList() {
        start.links.add(end);
        level = 0;
    }

    static class Data {
        String data;

        String getData() {
            return data;
        }

        void setData(String data) {
            this.data = data;
        }
    }

    static class Node {
        String key;
        List<Node> links = new ArrayList<>();
        Data data;

        Node(String key, Data data) {
            this.key = key;
            this.data = data;
        }

        void addLink(Node node) {
            links.add(node);
        }

        String getKey() {
            return key;
        }

        void setKey(String key) {
            this.key = key;
        }
    }

    // either returns the node with the given key or the location after which to insert.
    private Node searchInternal(String key) {
        Node node = start;
        int searchLevel = level;

        while (searchLevel >= 0) {
            Node rightNode = node.links.get(searchLevel);

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

    int log(int length) {
        if (length == 0) {
            throw new ArithmeticException("Log of 0 is invalid");
        }

        int bit;

        // find max 1 bit position.
        for (bit = 0; length > 1; length >>= 2) {
            bit++;
        }

        return bit;
    }

    void insert(Node ins, Node curr, int level) {
        Node next = curr.links.get(level);

        curr.links.set(level, ins);
        ins.links.set(level, next);
    }

    void rebuild() {
        int maxLevel = log(getSize());
        for (int level = 1; level < maxLevel; level++) {
            Node prevLevelCurr = start;
            Node nextLevelCurr = start;

            while (prevLevelCurr != end) {
                Node next = prevLevelCurr.links.get(level-1);

                if (next == end) {
                    break;
                }

                insert(next, nextLevelCurr, level);
                nextLevelCurr = next;
                prevLevelCurr = next.links.get(level-1);
            }
        }
    }

    void insert(Node ins) throws DuplicateKeyException {
        // insert in lowest level, then simply create higher levels on 1/2 principle.
        Node prev = searchInternal(ins.getKey());
        if (prev.getKey().equals(ins.getKey())) {
            throw new DuplicateKeyException("Node " + ins + " has duplicate key");
        }

        insert(ins, prev, 0);
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

    public static void main(String args[]) {
    }
}
