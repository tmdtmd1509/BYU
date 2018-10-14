//YOU MAY NOT MODIFY THIS DOCUMENT

#pragma once

#include "NodeInterface.h"

using namespace std;

class BSTInterface {
public:
	BSTInterface() {}
	virtual ~BSTInterface() {}

	//Please note that the class that implements this interface must be made
	//of objects which implement the NodeInterface

	/*
	* Returns the root node for this tree
	*
	* @return the root node for this tree.
	*/
	virtual NodeInterface * getRootNode() const = 0;

	/*
	* Attempts to add the given int to the BST tree
	*
	* @return true if added
	* @return false if unsuccessful (i.e. the int is already in tree)
	*/
	virtual bool add(int data) = 0;
	
	//in class
	//return insert(head, value);

	/*
	* Attempts to remove the given int from the BST tree
	*
	* @return true if successfully removed
	* @return false if remove is unsuccessful(i.e. the int is not in the tree)
	*/
	virtual bool remove(int data) = 0;

	/*
	bool remove(Node* &T, int val) {
		if (T == NULL) {
			return false;
		}
		if (T->value < val) {
			return remove(T->right, val);
		}
		else if (T->value > val) {
			return remove(T->left, val);
		}
		else {
			if (hasNoChildren(T)) {
				delete T;
				T = NUL;
				return true;
			}
			else if (hasTwoChildren(T)) {
				int n = getHighest(T->left);
				remove(T->left, n);
				T->value = n;
				return true;
			}
			else if (hasLeftChild(T)) {
				Node* curr = T;
				T = curr->left;
				delete curr;
				return true;
			}
			else if (hasRightChild(T)) {
				Node* curr = T;
				T = curr->right;
				delete curr;
				return true;
			}
		}
	}
	*/
	/*
	* Removes all nodes from the tree, resulting in an empty tree.
	*/
	virtual void clear() = 0;

	//In class
	/*
	bool insert(Node* &T, int val) {
		if (T == NULL) {
			T = new Node(val);
			return true;
		}
		if (T->value == val) {
			return false;
		}
		else if (T->value > val) {
			return insert(T->left, val);
		}
		else {
			return insert(T->right, val);
		}
	}
	*/
};