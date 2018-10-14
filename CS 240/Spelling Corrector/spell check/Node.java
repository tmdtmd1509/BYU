package spell;


import java.lang.*;
import java.lang.reflect.Array;

public class Node implements ITrie.INode {
    public int frequency;
    public Node[] trieNode = new Node[26];

    public Node() {
        trieNode = new Node[26];
        frequency = 0;
    }

    public int getValue() {
        return frequency;
    }


}
