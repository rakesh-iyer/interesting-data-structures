class Derivative {
    public static void main(String args[]) {
        Tree listHead = new Tree();
        Tree minus = new Tree(Info.Type.SUBTRACT, null);
        Tree mult = new Tree(Info.Type.MULTIPLY, null);
        Tree three = new Tree(Info.Type.CONSTANT, "3");
        Tree ln = new Tree(Info.Type.LOG, null);
        Tree plus = new Tree(Info.Type.ADD, null);
        Tree x = new Tree(Info.Type.VARIABLE, "x");
        Tree one = new Tree(Info.Type.CONSTANT, "1");
        Tree divide = new Tree(Info.Type.DIVIDE, null);
        Tree a = new Tree(Info.Type.VARIABLE, "a");
        Tree pow = new Tree(Info.Type.POWER, null);
        Tree x2 = new Tree(Info.Type.VARIABLE, "x");
        Tree two = new Tree(Info.Type.CONSTANT, "2");

        listHead.setRightAsTag(listHead);
        listHead.setLeft(minus);
        minus.setLeft(mult);
        minus.setRightAsTag(listHead);
        mult.setLeft(three);
        mult.setRight(divide);
        three.setRight(ln);
        ln.setLeft(plus);
        ln.setRightAsTag(mult);
        plus.setLeft(x);
        plus.setRightAsTag(ln);
        x.setRight(one);
        one.setRightAsTag(plus);
        divide.setLeft(a);
        divide.setRightAsTag(minus);
        a.setRight(pow);
        pow.setLeft(x2);
        pow.setRightAsTag(divide);
        x2.setRight(two);
        two.setRightAsTag(pow);

        Tree.setListHead(listHead);

        Tree derivative = listHead.findDerivative(listHead.findFirstInOrder());
        derivative.nonThreadedInOrder();
    }
}
