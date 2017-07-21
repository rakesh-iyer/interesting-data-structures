import java.util.*;

class PerfectSkipList {
    int level;
    Node start = new Node("", new Data());
    Node end = new Node("LARGESTKEYEVER", new Data());


    static class DuplicateKeyException extends Exception {
        String exceptionString;

        DuplicateKeyException(String exceptionString) {
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

    Node search(String key) {
        Node node = start;
        int searchLevel = level;

        while (searchLevel >= 0) {
            if (node.getKey().equals(key)) {
                return node;
            } else if (node.getKey().compareTo(node.links.get(searchLevel).getKey()) >= 0) {
                node = node.links.get(searchLevel);
            } else {
                searchLevel--;
            }
        }

        return null;
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

    void insert(Node ins, Node curr, Node next, int level) {
        curr.links.set(level, ins);
        ins.links.set(level, next);
    }

    void insert(Node ins) throws DuplicateKeyException {
        // insert in lowest level, then simply create higher levels on 1/2 principle.
        Node curr = start;
        int length = 0;

        while (curr != end) {
            Node next = curr.links.get(0);

            length++;
            if (next.getKey().compareTo(ins.getKey()) > 0) {
                insert(ins, curr, next, 0);
                break;
            } else if (next.getKey().compareTo(ins.getKey()) < 0) {
                curr = next;
            } else {
                throw new DuplicateKeyException("Node " + ins + " has duplicate key");
            }
        }


        // build all the higher levels again.
        int maxLevel = log(length);
        for (int level = 1; level < maxLevel; level++) {
            Node prevLevelCurr = start;
            Node nextLevelCurr = start;

            while (prevLevelCurr != end) {
                Node next = curr.links.get(level);

                if (next == end) {
                    break;
                }

                insert(next, nextLevelCurr, end, level);
                nextLevelCurr = next;

                prevLevelCurr = next.links.get(level);
            }
        }
    }

    void delete(Node n) {
    }

    public static void main(String args[]) {
    }
}
