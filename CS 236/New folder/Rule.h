#pragma once
#include "Parameter.h"
#include "Predicate.h"
#include <vector>

using namespace std;

class Rule {
public:
	Rule();
	Rule(Predicate p);
	Rule(Predicate pred, vector<Predicate> predList);

	Predicate pred;
	vector<Predicate> predicateList;

	void addPredicate(string s, vector<Parameter> pred);
	string toString();

};
