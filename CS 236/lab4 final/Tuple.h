#pragma once
#ifndef TUPLE_H_
#define TUPLE_H_

#include <vector>
#include "Parameter.h"

using namespace std;

class Tuple: public vector<string> {
public:
	string getTuples();

	//void setTupleList(vector<string> tlist);

	void printTuple();

	//bool operator< (const Tuple b) const;
protected:
	string tuples;
	
};

#endif