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
	string toString();
	//void setIdent();
	string getIdent();
	//void setParam();
	vector<Parameter> getParam();
};




/*
Predicate

- HeadID;
- vector<Parameter*>;
*/