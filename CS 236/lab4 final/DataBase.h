#pragma once
#ifndef DATABASE_H_
#define DATABASE_H_

#include <map>
#include "Relation.h"
#include "Predicate.h"
//#include "Query.h"
#include "Tuple.h"

class DataBase {
private:
	vector<Predicate> schemes;
	vector<Predicate> facts;
	vector<Relation> toPrint;

public:
	void createDB(vector<Predicate> s, vector<Predicate> f);
	void addRelation(Relation r);
	void setDB(string name, Relation relation);
	map<string, Relation> getDB();
	int tupleSize;
	//void addTuples(Tuple t, string n);
	//void runQueries();
	//void printToPrint();
protected:
	map<string, Relation> db;
};
#endif