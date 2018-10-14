#pragma once
#include "token.h"
#include "Predicate.h"
#include "Rule.h"
#include <set>

using namespace std;

class DLP {
public:

	DLP();
	~DLP();

	vector<Predicate> schemes;
	vector<Predicate> facts;
	vector<Predicate> queries;
	vector<Rule> rules;
	set<string> domain;

	void addScheme(Predicate s);
	void addFact(Predicate f);
	void addRule(Rule r);
	void addQuery(Predicate q);
	void addDomain(vector<Parameter> d);

	string stringSchemes();
	string stringFacts();
	string stringRules();
	string stringQueries();
	string stringDomain();
	string toString();
};