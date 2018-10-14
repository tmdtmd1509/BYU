package spell;

public class Trie implements ITrie{
    Node root;
    private int wordCount;
    private int nodeCount;


    public char[] alphabet = new char[26];

    public Trie() {
        root = new Node();
        nodeCount = 1; //add one node
        wordCount = 0;
        int i = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet[i] = c;
            i++;
        }
    }

    public void add(String word) {
        if(word.equals(null)) {
            return;
        }
        Node curNode = root;//start from root
        for(int i = 0; i < word.length(); i++) {
            if(curNode.trieNode[word.charAt(i)-'a'] == null) {//if there is no node maden
                curNode.trieNode[word.charAt(i)-'a'] = new Node();//make new node
                nodeCount++;
            }
            curNode = curNode.trieNode[word.charAt(i) - 'a'];//next char
            if(i == word.length() - 1) {
                if(curNode.getValue() == 0) {//if this is new word increase num of word
                    wordCount++;
                }
                curNode.frequency++;//if there was the word, frequency up
            }
        }

    }
/*
    public INode find(String word) {
        Node curNode = root;
        for(int i = 0; i < word.length(); i++) {
            if(curNode.trieNode[word.charAt(i) - 'a'] == null) {
                return null;
            }
            else {
                if(i == word.length() - 1) {
                    if(curNode.trieNode[word.charAt(i) - 'a'].frequency > 0){
                        return curNode.trieNode[word.charAt(i) - 'a'];
                    }
                    else
                        return null;
                }
                curNode = curNode.trieNode[word.charAt(i) - 'a'];
            }
        }
        return null;
    }
*/

    public INode find(String word) {
        //return findReflexive(root, word, 0,word.length());
        Node currentNode = root;
        //System.out.println(word);
        if(word==null)
            return null;
        for(int i=0;i<word.length();i++){
            if(currentNode.trieNode[word.charAt(i)-'a']==null)
                return null;
            else
                currentNode = currentNode.trieNode[word.charAt(i)-'a'];
        }
        if(currentNode != null && currentNode.getValue()>0)
            return currentNode;
        else return null;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        StringBuilder curWord = new StringBuilder();
        toString_helper(root, curWord, output);

        return output.toString();
    }

    private void toString_helper(Node n, StringBuilder curWord, StringBuilder output) {
        if(n == null) {
            return;
        }
        if(n.getValue() > 0) {
            output.append(curWord.toString() + "\n");
        }
        for(int i=0;i<26;i++) {
            curWord.append(alphabet[i]);
            toString_helper(n.trieNode[i], curWord, output);
            curWord.setLength(curWord.length() - 1);
        }
    }

    @Override
    public int hashCode() {
        return wordCount * 52+nodeCount*7;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }/*
        if(o == this) {
            return false;
        }*/
        if(this.getClass() != o.getClass()) {
            return false;
        }
        else {
            Trie curNode = (Trie) o;//change o to Trie class
            if (this.wordCount != curNode.getWordCount() || this.nodeCount != curNode.getNodeCount()) {
                return false;
            }
            else if (!this.toStringForVal().equals(curNode.toStringForVal())) {
                return false;
            }
            else return equals_helper(this.root, curNode.root);
        }
    }

    private boolean equals_helper(Node n1, Node n2) {
        for(int i = 0; i < 26; i++) {
            if(n1.trieNode[i] != null && n2.trieNode[i] != null) {
                if(n1.trieNode[i].getValue() != n2.trieNode[i].getValue()) {
                    return false;
                }
                else equals_helper(n1.trieNode[i], n2.trieNode[i]);
            }
            else if(n1.trieNode[i] == null && n2.trieNode[i] != null)
                return false;
            else if(n1.trieNode[i] != null && n2.trieNode[i] == null)
                return false;
        }
        return true;
    }

    public String toStringForVal() {
        StringBuilder output = new StringBuilder();
        StringBuilder curWord = new StringBuilder();
        toStringForVal_helper(root, curWord, output);

        return output.toString();
    }

    private void toStringForVal_helper(Node n, StringBuilder curWord, StringBuilder output) {
        if(n == null) {
            return;
        }
        if(n.getValue() > 0) {
            output.append("\t"+ curWord.toString() + " ");
            //Node no = find(curWord.toString());
            output.append(this.findFrequency(curWord.toString()) + "\n");
        }
        for(int i=0;i<26;i++) {
            curWord.append(alphabet[i]);
            toStringForVal_helper(n.trieNode[i], curWord, output);
            curWord.setLength(curWord.length() - 1);
        }
    }

    public int findFrequency(String word){
        Node curNode = root;
        for(int i=0; i<word.length(); i++) {
            if(curNode.trieNode[word.charAt(i) - 'a'] == null) {
                return 0;
            }
            else {
                curNode = curNode.trieNode[word.charAt(i) - 'a'];
            }
        }
        if(curNode != null && curNode.getValue() > 0) {
            return  curNode.getValue();
        }
        else return 0;
    }
}
