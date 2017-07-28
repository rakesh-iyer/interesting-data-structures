import java.util.*;

class Node {
    String key;
    Data data;
    List<Node> links = new ArrayList<>();

    Node(String key, Data data) {
        this.key = key;
        this.data = data;
    }

    void addLink(Node node) {
        links.add(node);
    }

    void removeLink(int level) {
        links.remove(level);
    }

    Node getLink(int level) {
        return links.get(level);
    }

    Node setLink(Node node, int level) {
        return links.set(level, node);
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }
}

