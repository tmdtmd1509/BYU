#pragma once
#ifndef INTERPRETER_H_
#define INTERPRETER_H_

#include "DataBase.h"
#include "DatalogProgram.h"

using namespace std;

class Interpreter {
private:
	vector<Predicate> q;
	vector<Predicate> s;
	vector<Predicate> f;
    vector<Rule> r;
	DataBase database;
	map<string, Relation> db;
	vector<int> positions;
	int changeCount = 0;

public:
	void resolveQueries(vector<Predicate> s, vector<Predicate> f, vector<Predicate> q, vector<Rule> r);
    void resolveRules(vector<Rule> r);
	void evaluateRule(vector<Rule> rule);
    void createRule(Rule rule);
	Scheme makeVecToScheme(Predicate querie);
	void evaluateQuerie(vector<Predicate> q);
	//pair<unsigned int, Relation> runQuery(Relation relation, Predicate query);
	void createQuerie(vector<Predicate> q);
	void toString(Relation relation, Predicate predicate, string name);
	//void toStringRule(Relation relation, string name);
	int find(vector<string> vec, string s);
	void makeGraph();
	//int checkIsId(vector<string> paramVal, unsigned int j);
};

#endif
