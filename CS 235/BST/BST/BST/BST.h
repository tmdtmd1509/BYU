#pragma once

#include "BSTInterface.h"
#include "Node.h"


class BST : public BSTInterface {
private:
	Node* root;
public:
	BST() {
		root = NULL;
	}
	~BST() {
		clear();
	}

	//Please note that the class that implements this interface must be made
	//of objects which implement the NodeInterface

	/*
	* Returns the root node for this tree
	*
	* @return the root node for this tree.
	*/
	NodeInterface * getRootNode() const;

	Node* itemAdd(Node* curr, int value);

	bool tree(int value);

	bool itemTree(Node* curr, int value);

	int size();
	
	Node* findLarge(Node* curr, Node* rNode);

	Node* itemRemove(Node* curr, int x);
	
	/*
	* Attempts to add the given int to the BST tree
	*
	* @return true if added
	* @return false if unsuccessful (i.e. the int is already in tree)
	*/
	bool add(int data);

	/*
	* Attempts to remove the given int from the BST tree
	*
	* @return true if successfully removed
	* @return false if remove is unsuccessful(i.e. the int is not in the tree)
	*/
	bool remove(int data);

	/*
	* Removes all nodes from the tree, resulting in an empty tree.
	*/
	void clear();
};
