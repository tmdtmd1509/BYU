#include "DatalogProgram.h"
#include "Parameter.h"
#include <iostream>
#include <sstream>

using namespace std;

DLP::DLP() {
	schemes = vector<Predicate>();
	facts = vector<Predicate>();
	rules = vector<Rule>();
	queries = vector<Predicate>();
	domain = set<string>();
}
DLP::~DLP() {}

//add
///////////////////////////////////////////////////////
/*
void DLP::addScheme(Predicate scheme) {
schemes.push_back(scheme);

*/
void DLP::addScheme(string s, vector<Parameter> pred) {
	schemes.push_back(Predicate(s, pred));
}
void DLP::addFact(string s, vector<Parameter> pred) {
	facts.push_back(Predicate(s, pred));
}

void DLP::addRule(Predicate p, vector<Predicate> plist) {
	rules.push_back(Rule(p, plist));
	//addDomain();
}

void DLP::addQuery(string s, vector<Parameter> pred) {
	queries.push_back(Predicate(s, pred));
}

void DLP::addDomain(vector<Parameter> paramList) {
	int vecSize = paramList.size();
	for (int i = 0; i < vecSize; i++) {
		domain.insert(paramList[i].getValue());
	}
}

//string
/////////////////////////////////////////////////////

string DLP::stringSchemes() {
	stringstream ss;
	int vecSize = schemes.size();
	ss << "Schemes(" << vecSize << "):";
	for (int i = 0; i < vecSize; i++)
		ss << "\n  " << schemes[i].toString();
	return ss.str();
}

string DLP::stringFacts() {
	stringstream ss;
	int vecSize = facts.size();
	ss << "Facts(" << vecSize << "):";
	for (int i = 0; i < vecSize; i++)
		ss << "\n  " << facts[i].toString() << ".";
	return ss.str();
}

string DLP::stringRules() {
	stringstream ss;
	int vecSize = rules.size();
	ss << "Rules(" << vecSize << "):";
	for (int i = 0; i < vecSize; i++)
		ss << "\n  " << rules[i].toString();
	return ss.str();
}

string DLP::stringQueries() {
	stringstream ss;
	int vecSize = queries.size();
	ss << "Queries(" << vecSize << "):";
	for (int i = 0; i < vecSize; i++)
		ss << "\n  " << queries[i].toString() << "?";
	return ss.str();
}

string DLP::stringDomain() {
	stringstream ss;
	int vecSize = domain.size();
	ss << "Domain(" << vecSize << "):";
	set<string>::iterator iter;
	for (iter = domain.begin(); iter != domain.end(); iter++)
		ss << "\n  " << (*iter);
	return ss.str();
}

//tostring

string DLP::toString() {
	stringstream ss;
	ss << "Success!" << endl;
	ss << stringSchemes() << endl;
	ss << stringFacts() << endl;
	ss << stringRules() << endl;
	ss << stringQueries() << endl;
	ss << stringDomain();
	return ss.str();
}


vector<Predicate> DLP::getSchemes() {
	return schemes;
}
vector<Predicate> DLP::getQueries() {
	return queries;
}
vector<Predicate> DLP::getFacts() {
	return facts;
}
vector<Rule> DLP::getRules() {
       return rules;
}
