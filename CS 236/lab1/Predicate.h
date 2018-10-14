#pragma once
#include "Parameter.h"
#include <vector>

using namespace std;

class Predicate {
public:
	Predicate();
	Predicate(string ident);
	Predicate(string ident, vector<Parameter> paramList);
	~Predicate();

	vector<Parameter> paramList;
	void addParameter(Parameter param);

	string id;
	string getID();
	vector<Parameter> getParameters();
	string toString();
};




/*
Predicate

- HeadID;
- vector<Parameter*>;
*/