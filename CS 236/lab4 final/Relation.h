#pragma once
#ifndef RELATION_H_
#define RELATION_H_

#include <vector>
#include <set>
#include <string>
#include "Tuple.h"
#include "Scheme.h"
#include "Predicate.h"
#include <iostream>


using namespace std;

class Relation {
private:
	pair<int, int> intPair;
	vector<pair<int, int>> matchingCols;
	vector<int> uniqueCols;

public:
	Relation();
	Relation(string name, Scheme s);
	~Relation();

	string name = "testname";
	Scheme schema;
	set<Tuple> tuples;
	int isChange = 0;


	void setName(string name);
	string getName();

	void setScheme(Scheme schema);
	Scheme getScheme();
	//void addScheme();

	//void setTuple(set<Tuple> tableBody);
	set<Tuple> getTuple();
	void addToTuple(Tuple t);

	//string toString();
	vector<string> printSchemes();
	vector<string> printTuples();

	Relation select(unsigned int index, string value);
	Relation select(int index1, int index2);
	Relation project(vector<int> indexes);
	Relation project(vector<Parameter> paramList);//added
	Relation rename(Scheme schema);

	Relation join(Relation r1, Relation r2);

	Scheme joinScheme(Scheme s1, Scheme s2);

	Tuple joinTuple(Tuple t1, Tuple t2, vector<int> uniqueCols);

	bool isJoinable(Tuple t1, Tuple t2, vector<pair<int, int>> matchingCols);

	bool unionAdd(Relation relation);

	//Relation evalParams(vector<Predicate> q, int index);
	
	//void toTuples(vector<int> position, vector<string> ID, int num);
	//int numTuples(vector<int> position, vector<string> ID);
	//void printRelation();

};



#endif
