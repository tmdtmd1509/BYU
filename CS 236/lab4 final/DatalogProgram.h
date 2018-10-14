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

	void addScheme(string s, vector<Parameter> pred);
	void addFact(string s, vector<Parameter> pred);
	void addRule(Predicate p, vector<Predicate> plist);
	void addQuery(string s, vector<Parameter> pred);
	void addDomain(vector<Parameter> d);

	string stringSchemes();
	string stringFacts();
	string stringRules();
	string stringQueries();
	string stringDomain();
	string toString();

	vector<Predicate> getSchemes();
	vector<Predicate> getQueries();
	vector<Predicate> getFacts();
    vector<Rule> getRules();
};
