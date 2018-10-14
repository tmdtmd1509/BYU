#pragma once
#include "NodeInterface.h"
#include <iostream>


class Node : public NodeInterface {

public:
	Node* left;
	Node* right;
	int data;
	int height;



	Node(int data, Node* left = NULL, Node* right = NULL, int height = 0) {
		this->data = data;
		this->left = left;
		this->right = right;
		this->height = height;
	}

	/*
	* Returns the data stored in this node
	*
	* @return the data stored in this node.
	*/
	virtual int getData() const {
		return data;
	}

	/*
	* Returns the left child of this node or null if empty left child.
	*
	* @return the left child of this node or null if empty left child.
	*/
	virtual NodeInterface * getLeftChild() const {
		return left;
	}

	/*
	* Returns the right child of this node or null if empty right child.
	*
	* @return the right child of this node or null if empty right child.
	*/
	virtual NodeInterface * getRightChild() const {
		return right;
	}

	/*
	* Returns the height of this node. The height is the number of nodes
	* along the longest path from this node to a leaf.  While a conventional
	* interface only gives information on the functionality of a class and does
	* not comment on how a class should be implemented, this function has been
	* provided to point you in the right direction for your solution.  For an
	* example on height, see page 448 of the text book.
	*
	* @return the height of this tree with this node as the local root.
	*/
	virtual int getHeight() {
		return height;
	}

	void setHeight(int value) {
		this->height = value;
	}

	void newHeight() {
		int LH;
		if (left == NULL)LH = -1;
		else LH = left->height;

		int RH;
		if (right == NULL) RH = -1;
		else RH = right->height;

		height = max(LH, RH) + 1;
	}

	int getBalance() {
		int LH = (left == NULL) ? -1 : left->height;
		int RH = (right == NULL) ? -1 : right->height;

		return RH - LH;
	}
};
