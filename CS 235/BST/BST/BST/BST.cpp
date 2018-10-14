#include "BST.h"
#include <iostream>

using namespace std;

int treeSize = 0;

NodeInterface *BST::getRootNode() const{
	return root;
}

int BST::size() {
	return treeSize;
}

bool BST::itemTree(Node* curr, int value) {
	bool tree = false;

	if (curr == NULL) {
		return false;
	}
	if (value == curr->data) {
		tree = true;
	}
	else if (value < curr->data) {
		tree = itemTree(curr->left, value);
	}
	else if (value > curr->data) {
		tree = itemTree(curr->right, value);
	}
	else {
		tree = false;
	}

	return tree;
}

bool BST::tree(int value) {
	if (root == NULL)
	{
		return false;
	}

	else {
		bool tree = BST::itemTree(root, value);
		return tree;
	}
}

Node* BST::itemAdd(Node* temp, int a) {
	if (temp == NULL) {
		return new Node(a);
	}

	else if (a < temp->data) {
		temp->left = itemAdd(temp->left, a);
		return temp;
	}

	else if (a > temp->data) {
		temp->right = itemAdd(temp->right, a);
		return temp;
	}
	return temp;
}

bool BST::add(int x) {

	// find
	if (!BST::tree(x)) {
		root = BST::itemAdd(root, x);
		return true;
	}
	else {
		return false;
	}
}

Node* BST::findLarge(Node* curr, Node* parent) {

	if (curr->right == NULL) {
		int tempData = parent->data;
		parent->data = curr->data;
		curr->data = tempData;

		Node* savedNode = curr->left;
		delete curr;
		return savedNode;
	}

	else {
		curr->right = BST::findLarge(curr->right, parent);
		return curr;
	}
}

Node* BST::itemRemove(Node* c, int x) {

	if (c->data > x) {
		c->left = itemRemove(c->left, x);
		return c;
	}

	else if (c->data < x) {
		c->right = itemRemove(c->right, x);
		return c;
	}

	else {

		if (c->left == NULL & c->right == NULL) {
			delete c;
			c = NULL;
			return c;
		}
		// if it has just a right child
		if (c->left == NULL) {
			Node* temp = c->right;
			delete c;
			return temp;
		}
		// if it has just a left child
		if (c->right == NULL) {
			Node* temp = c->left;
			delete c;
			return temp;
		}
		else {
			c->left = BST::findLarge(c->left, c);
			return c;
		}

	}


}

bool BST::remove(int data) {

	if (BST::tree(data)) {
		root = BST::itemRemove(root, data);
		return true;
	}
	else {
		return false;
	}
}

void BST::clear() {

	while (root != NULL) {
		remove(root->data);
	}
}

void Inorder(Node *root) {
	if (root == NULL) return;

	Inorder(root->left);       //Visit left subtree
	Inorder(root->right);      // Visit right subtree
}