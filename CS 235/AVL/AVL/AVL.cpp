#include "AVL.h"

int treeSize = 0;

NodeInterface *AVL::getRootNode() const {
	return root;
}

int AVL::size() {
	return treeSize;
}

bool AVL::itemTree(Node* curr, int value) {
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

bool AVL::tree(int value) {
	if (root == NULL)
	{
		return false;
	}

	else {
		bool tree = AVL::itemTree(root, value);
		return tree;
	}
}

Node* AVL::itemAdd(Node* temp, int a) {
	if (temp == NULL) {
		return new Node(a);
	}

	else if (a < temp->data) {
		temp->left = itemAdd(temp->left, a);
		return balance(temp);
	}

	else if (a > temp->data) {
		temp->right = itemAdd(temp->right, a);
		return balance(temp);
	}
}

bool AVL::add(int a) {
	if (!AVL::tree(a)) {
		root = AVL::itemAdd(root, a);
		return true;
	}
	else {
		return false;
	}
}

Node* AVL::findLarge(Node* curr, Node* parent) {

	if (curr->right == NULL) {
		int tempData = parent->data;
		parent->data = curr->data;
		curr->data = tempData;

		Node* savedNode = curr->left;
		delete curr;
		return balance(savedNode);
	}

	else {
		curr->right = AVL::findLarge(curr->right, parent);
		return balance(curr);
	}
}

Node* AVL::itemRemove(Node* curr, int x) {

	if (curr->data > x) {
		curr->left = itemRemove(curr->left, x);
		return balance(curr);
	}

	else if (curr->data < x) {
		curr->right = itemRemove(curr->right, x);
		return balance(curr);
	}

	else {

		if (curr->left == NULL & curr->right == NULL) {
			delete curr;
			curr = NULL;
			return balance(curr);
		}
		// if it has just a right child
		if (curr->left == NULL) {
			Node* temp = curr->right;
			delete curr;
			return balance(temp);
		}
		// if it has just a left child
		if (curr->right == NULL) {
			Node* temp = curr->left;
			delete curr;
			return balance(temp);
		}
		else {
			curr->left = AVL::findLarge(curr->left, curr);
			return balance(curr);
		}
	}
}


bool AVL::remove(int data) {
	if (AVL::tree(data)) {
		root = AVL::itemRemove(root, data);
		return true;
	}
	else {
		return false;
	}
}

void AVL::clear() {
	while (root != NULL) {
		remove(root->data);
	}
}

void AVL::RR(Node* a, Node* b) {
	a->right = b->left;
	b->left = a;

	a->newHeight();
	b->newHeight();
}
void AVL::RL(Node* a, Node* b, Node* c) {
	a->right = c;
	b->left = c->right;
	c->right = b;

	AVL::RR(a, c);

	a->newHeight();
	b->newHeight();
	c->newHeight();
}
void AVL::LL(Node* a, Node* b) {
	a->left = b->right;
	b->right = a;

	a->newHeight();
	b->newHeight();
}
void AVL::LR(Node* a, Node* b, Node* c) {
	a->left = c;
	b->right = c->left;
	c->left = b;

	AVL::LL(a, c);

	a->newHeight();
	b->newHeight();
	c->newHeight();
}
Node* AVL::balance(Node* node) {
	if (node == NULL) {
		return NULL;
	}
	node->newHeight();

	if (node->getBalance() > 1) {
		// RR or RL case
		Node* b = node->right;

		if (b->getBalance() < 0) {
			Node* c = b->left;
			AVL::RL(node, b, c);
			return c;
		}
		else {
			AVL::RR(node, b);
			return b;
		}

	}
	else if (node->getBalance() < -1) {
		// LL or LR case
		Node* b = node->left;

		if (b->getBalance() > 0) {
			Node* c = b->right;
			AVL::LR(node, b, c);
			return c;
		}
		else {
			AVL::LL(node, b);
			return b;
		}

	}
	else {
		return node;
	}

}
