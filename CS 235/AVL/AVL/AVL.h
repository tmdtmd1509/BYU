#pragma once

#include "AVLInterface.h"
#include <iostream>


class AVL : public AVLInterface {
public:
	AVL() {
		root = NULL;
	}
	~AVL() {
		clear();
	}

	NodeInterface * getRootNode() const;

	Node* itemAdd(Node* curr, int value);

	bool tree(int value);

	bool itemTree(Node* curr, int value);

	int size();

	Node* findLarge(Node* curr, Node* rNode);

	Node* itemRemove(Node* curr, int x);

	bool add(int data);

	bool remove(int data);

	void clear();

	void RR(Node* a, Node* b);
	void RL(Node* a, Node* b, Node* c);
	void LL(Node* a, Node* b);
	void LR(Node* a, Node* b, Node* c);

	Node* balance(Node* node);

	Node* root = NULL;
};
