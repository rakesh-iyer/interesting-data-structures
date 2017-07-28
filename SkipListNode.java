import java.util.*;

class SkipListNode {
    String key;
    Data data;
    List<SkipListNode> links = new ArrayList<>();

    SkipListNode(String key, Data data) {
        this.key = key;
        this.data = data;
    }

    void addLink(SkipListNode node) {
        links.add(node);
    }

    void removeLink(int level) {
        links.remove(level);
    }

    void keepLink(int level) {
        links = links.subList(0, level+1);
    }

    SkipListNode getLink(int level) {
        return links.get(level);
    }

    SkipListNode setLink(SkipListNode node, int level) {
        return links.set(level, node);
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }
}

