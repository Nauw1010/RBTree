package RBTree;

public class RBTree {
    RBTreeNode root;
    private final boolean RED = false;
    private final boolean BLACK = true;

    public RBTreeNode query(int key) {
        RBTreeNode tmp = root;
        while(tmp != null) {
            if(key == tmp.getKey()) {
                return tmp;
            } else if (key < tmp.getKey()) {
                tmp = tmp.getLeft();
            } else {
                tmp = tmp.getRight();
            }
        }
        return null;
    }

    public void insert(int key) {
        RBTreeNode node = new RBTreeNode(key);
        //case 1
        if(root == null) {
            node.setColor(BLACK);
            root = node;
            return;
        }

        RBTreeNode parent = root;
        RBTreeNode son = (key < parent.getKey()) ? parent.getLeft() : parent.getRight(); //不支持重复值
        while (son != null) {
            parent = son;
            son = key < parent.getKey()? parent.getLeft() : parent.getRight();
        }
        node.setParent(parent);
        if(key < parent.getKey()) {
            parent.setLeft(node);
        } else {
            parent.setRight(node);
        }

        //update
        insertFix(node);
    }

    private void insertFix(RBTreeNode node) {
        RBTreeNode father = node.getParent(), grandFather, uncle;
        if(father == null) { //node == root
            setBlack(root);
        } else if(father.getColor() == RED) {
            grandFather = father.getParent();
            if(grandFather.getLeft() == father) {
                uncle = grandFather.getRight();
                if(uncle != null && uncle.getColor() == RED) {
                    setBlack(father);
                    setBlack(uncle);
                    setRed(grandFather);
                    insertFix(grandFather);
                } else {
                    if(node == father.getRight()) {
                        leftRotate(father);
                        RBTreeNode tmp = node;
                        node = father;
                        father = tmp;
                    }
                    setBlack(father);
                    setRed(grandFather);
                    rightRotate(grandFather);
                }
            } else {
                uncle = grandFather.getLeft();
                if(uncle != null && uncle.getColor() == RED) {
                    setBlack(father);
                    setBlack(uncle);
                    setRed(grandFather);
                    insertFix(grandFather);
                } else {
                    if(node == father.getLeft()) {
                        rightRotate(father);
                        RBTreeNode tmp = node;
                        node = father;
                        father = tmp;
                    }
                    setBlack(father);
                    setRed(grandFather);
                    leftRotate(grandFather);
                }
            }
        }
    }

    public void remove(int key) {
        remove(query(key));
    }

    private void remove(RBTreeNode node) {
        if(node != null) {
            if(node.getLeft() != null && node.getRight() != null) {
                RBTreeNode replace = node; //replace: node的前驱
                RBTreeNode tmp = node.getLeft();
                while (tmp != null) {
                    replace = tmp;
                    tmp = tmp.getRight();
                }
                node.setKey(replace.getKey());
                remove(replace);
            } else {
                RBTreeNode replace = node.getLeft() != null ? node.getLeft() : node.getRight();
                RBTreeNode parent = node.getParent();

                if(parent == null) {
                    root = replace;
                    if(replace != null)
                        replace.setParent(null);
                } else {
                    if(replace != null)
                        replace.setParent(parent);
                    if(parent.getLeft() == node) {
                        parent.setLeft(replace);
                    } else {
                        parent.setRight(replace);
                    }
                }

                if(isBlack(node))
                    removeFix(parent, replace);
            }
        }
    }

    private void removeFix(RBTreeNode father, RBTreeNode node) {
        if(isRed(node)) {
            node.setColor(BLACK);
        } else if(father != null) {
            RBTreeNode brother;
            //兄弟一定不为null
            if(father.getLeft() == null) { //左节点为被删节点
                brother = father.getRight();
                if(isRed(brother)) {
                    setRed(father);
                    setBlack(brother);
                    leftRotate(father);
                    brother = father.getRight();
                } //使兄弟变为黑色
                if ( isBlack(brother.getLeft()) && isBlack(brother.getRight()) ) {
                    setRed(brother);
                    if(isBlack(father)) {
                        removeFix(father.getParent(), father);
                    } else {
                        setBlack(father);
                    }
                } else {
                    if(isBlack(brother.getRight())) {
                        setBlack(brother.getLeft());
                        setRed(brother);
                        rightRotate(brother);
                        brother = brother.getParent();
                    } //将远侄节点变为红色

                    brother.setColor(father.getColor());
                    setBlack(father);
                    setBlack(brother.getRight());
                    leftRotate(father);
                }
            } else { //右节点为被删节点
                brother = father.getLeft();
                if(isRed(brother)) {
                    setRed(father);
                    setBlack(brother);
                    rightRotate(father);
                    brother = father.getLeft();
                } //使兄弟变为黑色
                if( isBlack(brother.getLeft()) && isBlack(brother.getRight()) ) {
                    setRed(brother);
                    if(isBlack(father)) {
                        removeFix(father.getParent(), father);
                    } else {
                        setBlack(father);
                    }
                } else {
                    if(isBlack(brother.getLeft())) {
                        setBlack(brother.getRight());
                        setRed(brother);
                        leftRotate(brother);
                        brother = brother.getParent();
                    } // 将远侄变为红色

                    brother.setColor(father.getColor());
                    setBlack(father);
                    setBlack(brother.getLeft());
                    rightRotate(father);
                }
            }
            if(isRed(root)) setBlack(root);
        }
    }

    //null 节点为黑
    private boolean isBlack(RBTreeNode node) {
        if (node == null)
            return true;
        return node.getColor() == BLACK;
    }

    private boolean isRed(RBTreeNode node) {
        if (node == null)
            return false;
        return node.getColor() == RED;
    }

    private void leftRotate(RBTreeNode node) {
        RBTreeNode right = node.getRight();
        RBTreeNode parent = node.getParent();
        if (parent == null) {
            root = right;
            right.setParent(null);
        } else {
            if (parent.getLeft() != null && parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
            right.setParent(parent);
        }
        node.setParent(right);
        node.setRight(right.getLeft());
        if (right.getLeft() != null) {
            right.getLeft().setParent(node);
        }
        right.setLeft(node);
    }

    private void rightRotate(RBTreeNode node) {
        RBTreeNode left = node.getLeft();
        RBTreeNode parent = node.getParent();
        if(parent == null) {
            root = left;
            left.setParent(null);
        } else {
            if(parent.getLeft() != null && parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
            left.setParent(parent);
        }
        node.setParent(left);
        node.setLeft(left.getRight());
        if(left.getRight() != null) {
            left.getRight().setParent(node);
        }
        left.setRight(node);
    }

    private void setBlack(RBTreeNode node) {
        node.setColor(BLACK);
    }

    private void setRed(RBTreeNode node) {
        node.setColor(RED);
    }

    public void inOrder() {
        inOrder(root, 0);
    }

    private void inOrder(RBTreeNode node, int deep) {
        if (node == null)
            return;
        inOrder(node.getLeft(), deep + 1);
        System.out.println(node + " deep : " + deep);
        inOrder(node.getRight(), deep + 1);
    }
}
