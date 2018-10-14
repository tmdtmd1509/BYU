#include "Rule.h"

#include <sstream>

Rule::Rule() {

}

Rule::Rule(Predicate p) {
	pred = p;
}

Rule::Rule(Predicate p, vector<Predicate> pList) {
	pred = p;
	predicateList = pList;
}

void Rule::addPredicate(string id, vector<Parameter> pred) {
	predicateList.push_back(Predicate(id, pred));
}

string Rule::toString() {
	stringstream ss;
	int vecSize = predicateList.size();
	ss << pred.toString() << " :- ";
	for (int i = 0; i < vecSize; i++) {
		if (i == 0) {
			ss << predicateList[i].toString();
		}
		else {
			ss << "," + predicateList[i].toString();
		}
	}
	ss << ".";
	return ss.str();
}