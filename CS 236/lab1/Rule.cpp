#include "Rule.h"

#include <sstream>

Rule::Rule() {

}

Rule::Rule(Predicate p) {
	pred = p;
	predicateList;
}

Rule::Rule(Predicate p, vector<Predicate> pList) {
	pred = p;
	predicateList = pList;
}


void Rule::addPredicate(Predicate p) {
	predicateList.push_back(p);
}
Predicate Rule::getPredicate() {
	return pred;
}

vector<Predicate> Rule::getPredicateList() {
	return predicateList;
}


string Rule::toString() {
	stringstream ss;
	int vecSize = predicateList.size();
	ss << Predicate::toString() << " :- ";
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