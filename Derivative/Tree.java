import lombok.*;
import java.util.*;

class Tree implements Cloneable {
    @Setter @Getter Info info;
    @Setter @Getter Tree left;
    @Setter @Getter Tree right;
    @Setter @Getter boolean leftTag;
    @Setter @Getter boolean rightTag;
    @Setter @Getter static Tree listHead;

    Tree() {
    }

    Tree(Info.Type type, String value) {
        Info info = new Info();
        info.setType(type);
        info.setValue(value);

        setInfo(info);
    }

    Tree(Info.Type type, Tree left, Tree right) {
        this(type, null);
        Tree cloneLeft = left.clone();
        setLeft(cloneLeft);
        if (right != null) {
            Tree cloneRight = right.clone();
            getLeft().setRight(cloneRight);
            cloneRight.findLastInOrder().setRightAsTag(this);
        } else {
            cloneLeft.findLastInOrder().setRightAsTag(this);
        }
    }

    void addLeft(Tree left) {
        setLeft(left);
        left.setRight(this);
        left.setRightTag(true);
    }

    void addRight(Tree right) {
        Tree inOrderSucc = getRight();
        setRight(right);
        setRightTag(false);
        right.setRight(inOrderSucc);
        right.setRightTag(true);
    }

    public Tree clone() {
        Tree c = new Tree(getInfo().getType(), getInfo().getValue());

        cloneChild(c);

        return c;
    }

    // build clone for threaded tree top-down
    void cloneChild(Tree c) {
        if (getLeft() != null) {
            Tree left = new Tree(getLeft().getInfo().getType(), getLeft().getInfo().getValue());
            c.addLeft(left);
            getLeft().cloneChild(left);
        }

        if (getRight() != null && !isRightTag()) {
            Tree right = new Tree(getRight().getInfo().getType(), getRight().getInfo().getValue());
            c.addRight(right);
            getRight().cloneChild(right);
        }
    }

    Tree findFirstInOrder() {
        Tree t = this;

        while (t.getLeft() != null) {
            t = t.getLeft();
        }

        return t;
    }

    Tree findLastInOrder() {
        Tree t = findFirstInOrder();

        while (inOrderSuccessor(t) != null) {
            t = inOrderSuccessor(t);
        }

        return t;
    }

    static Tree inOrderSuccessor(Tree t) {
        if (t.isRightTag() || t.getRight() == null) {
            return t.getRight();
        }

        t = t.getRight();
        while (t.getLeft() != null) {
            t = t.getLeft();
        }

        return t;
    }

    static void processInfo(Info i) {
        if (i == null) {
            return;
        }
        if (i.getType() == Info.Type.CONSTANT || i.getType() == Info.Type.VARIABLE) {
            System.out.print(i.getValue() + " ");
        } else {
            System.out.print(i.getType().getOperator() + " ");
        }
    }

    void inOrder() {
        Tree t = findFirstInOrder();

        processInfo(t.getInfo());
        while ((t = inOrderSuccessor(t)) != getListHead()) {
            processInfo(t.getInfo());
        }
    }

    static Tree findDerivative(Tree p, Tree param1, Tree param2, Tree derivp1, Tree derivp2) {
        switch (p.getInfo().getType()) {
            case CONSTANT:
                            return new Tree(Info.Type.CONSTANT, "0");

            case VARIABLE:
                            if ("x".equals(p.getInfo().getValue())) {
                                return new Tree(Info.Type.CONSTANT, "1");
                            } else {
                                return new Tree(Info.Type.CONSTANT, "0");
                            }

            case ADD:
                            return new Tree(Info.Type.ADD, derivp1, derivp2);

            case SUBTRACT:
                            return new Tree(Info.Type.SUBTRACT, derivp1, derivp2);

            case MULTIPLY:
                            return new Tree(Info.Type.ADD, new Tree(Info.Type.MULTIPLY, param1, derivp2), new Tree(Info.Type.MULTIPLY, param2, derivp1));

            case DIVIDE: {
                            Tree rightTree = new Tree(Info.Type.DIVIDE, new Tree(Info.Type.MULTIPLY, param1, derivp2), new Tree(Info.Type.POWER, param2, new Tree(Info.Type.CONSTANT, "2")));
                            return new Tree(Info.Type.SUBTRACT, new Tree(Info.Type.DIVIDE, derivp1, param2), rightTree);
                         }

            case POWER:
                        {
                            Tree leftTree = new Tree(Info.Type.MULTIPLY, derivp1, new Tree(Info.Type.MULTIPLY, param2, new Tree(Info.Type.POWER, param1, new Tree(Info.Type.SUBTRACT, param2, new Tree (Info.Type.CONSTANT, "1")))));
                            Tree rightTree = new Tree(Info.Type.MULTIPLY, new Tree(Info.Type.MULTIPLY, new Tree(Info.Type.LOG, param1, null), derivp2), new Tree(Info.Type.POWER, param1, param2));

                            return new Tree(Info.Type.ADD, leftTree, rightTree);
                        }
            case LOG:
                            return new Tree(Info.Type.DIVIDE, derivp2, param1);

            default:
                            return null;
        }

    }

    static boolean isBinaryOperator(Tree t) {
        Info.Type type = t.getInfo().getType();
        if (type == Info.Type.SUBTRACT || type == Info.Type.ADD || type == Info.Type.DIVIDE || type == Info.Type.MULTIPLY || type == Info.Type.POWER) {
            return true;
        } else {
            return false;
        }
    }

    Tree findDerivative(Tree p) {
        Tree q = null, p1, p2 = null, q1 = null;
        do {
            p1 = p.getLeft();
            q1 = (p1 != null) ? p1.getRight() : q1;

            if (isBinaryOperator(p)) {
                Tree right = p1.getRight();
                p1.setRight(null);
            }
            q = findDerivative(p, p1, p2, q1, q);
            if (isBinaryOperator(p)) {
                p1.setRight(p2);
            }

            p2 = p;
            p = inOrderSuccessor(p);
            if (!p2.isRightTag()) {
                // replace right pointer with q to temporarily store it.
                p2.setRight(q);
            }
        } while (p != this);

        return q;
    }

    void nonThreadedInOrder() {
        Tree tree = this;
        Deque<Tree> stack = new ArrayDeque<>();
        int cond = 1;
        System.out.println("nonThreadedInOrder");

        while (tree != null) {
            switch (cond) {
                case 1:
                        while (tree.left != null) {
                            stack.push(tree);
                            tree = tree.left;
                        }
                case 2:
                        processInfo(tree.getInfo());
                        if (tree.right != null && !tree.rightTag) {
                            tree = tree.right;
                            cond = 1;
                            break;
                        }
                case 3:
                        try {
                            tree = stack.pop();
                            cond = 2;
                            break;
                        } catch (NoSuchElementException e) {
                            System.out.println("nonThreadedInOrder done");
                            return;
                        }
            }
        }
    }

    void setRightAsTag(Tree right) {
        setRight(right);
        setRightTag(true);
    }

}

class Info {
    enum Type {
        CONSTANT(""),
        VARIABLE(""),
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        LOG("ln"),
        POWER("^");

        @Getter @Setter String operator;
        Type(String operator) {
            setOperator(operator);
        }
    }

    @Setter @Getter Type type;
    @Setter @Getter String value; // if type is constant you do Long.valueOf
}
