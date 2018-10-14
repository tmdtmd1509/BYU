#pragma once
#ifndef INTERPRETER_H_
#define INTERPRETER_H_

#include "DataBase.h"
#include "DatalogProgram.h"
#include "Graph.h"
#include "Node.h"
#include <stack>


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
	set<int> position;
	vector<int> pos;
	bool isChange = true;
        int changeCount = 1;

	Graph forwardGraph;
	Graph reverseGraph;
	vector<Rule> newRule;

	//Node callNode;

public:
	void resolveQueries(vector<Predicate> s, vector<Predicate> f, vector<Predicate> q, vector<Rule> r);
	void makeGraph(vector<Rule> rul);
	vector<Rule> pickRules(vector<Rule> rule, int it);
	void resolveRules(vector<Rule> r);
	void evaluateRule(vector<Rule> rule);
    void createRule(Rule rule);
	Scheme makeVecToScheme(Predicate querie);
	void createQuerie(vector<Predicate> q);
	void toString(Relation relation, Predicate predicate, string name);
	int find(vector<string> vec, string s);
	Relation makeQueryOne(Relation newRelation, vector<Predicate>querieList);
	
	void forward(vector<Rule> rule);
	void reverse(vector<Rule> rule);
	void DFSforest();
        void DFSreverse(int node);
	void DFSforward(int node);
	void graphToString(Graph forwardGraph);
	void graphToString2(Graph reverseGraph);

	stack<int> postOrderList;
	set<int> SCC;
	vector<set<int>> SCCs;
	void printSCC(set<int> oneSCC);

};

#endif
