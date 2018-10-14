#include "Predicate.h"

#include <sstream>

Predicate::Predicate() {
}

Predicate::Predicate(string ident) {
	id = ident;
}

Predicate::Predicate(string ident, vector<Parameter> pList) {
	//cout << "CALLING PREDICATE WITH TWO PARAMS" << endl;
	id = ident;
	paramList = pList;
}

Predicate::~Predicate() {}

void Predicate::addParameter(Parameter param) {
	paramList.push_back(param);
}

string Predicate::toString() {//might have problem
	stringstream ss;
	ss << id << "(";
	for (int i = 0; i < (int)paramList.size(); i++) {
		if (i == 0)
			ss << paramList[i].toString();
		else
			ss << "," << paramList[i].toString();
	}
	ss << ")";
	return ss.str();
}

string Predicate::getIdent()
{
	return id;
}

vector<Parameter> Predicate::getParam()
{
	return paramList;
}
