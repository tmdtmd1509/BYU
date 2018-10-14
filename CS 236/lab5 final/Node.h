#pragma once
#ifndef NODE_H_
#define NODE_H_

#include <set>
#include <vector>


using namespace std;

class Node {
private:

public:

	set<int> children;
	bool visited = false;
	bool itSelf = false;



};
#endif