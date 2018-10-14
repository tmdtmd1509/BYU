#include "DataBase.h"
#include "Interpreter.h"
#include <sstream>
/*
Database::Database() {}

Database::~Database() {}
*/
void DataBase::createDB(vector<Predicate> s, vector<Predicate> f) {
	schemes = s;
	facts = f;

	//scheme
	for (unsigned int i = 0; i < schemes.size(); i++) {
		Relation relation;
		Scheme scheme;
		string name = schemes[i].getIdent();
		vector<Parameter> schemeName = schemes[i].getParam();//list of facts
		//get strings from parameters
		for (unsigned num = 0; num < schemeName.size(); ++num) {
			string schemeValue = schemeName[num].getValue();
			scheme.push_back(schemeValue);
		}

		relation.setName(name);
		relation.setScheme(scheme);

		//Tuple
		for (unsigned int j = 0; j < facts.size(); j++) {//store several facts
			Tuple tuple;//vector of strings
			//vector<string> factStrings;
			string factsName = facts[j].getIdent();//Facts name
			vector<Parameter> factTuples = facts[j].getParam();//list of strings
			if (name == factsName) {//name check
				//get strings from parameters
				for (unsigned int k = 0; k < factTuples.size(); k++) {//one fact's strings
					string tupleValue = factTuples[k].getValue();
					tuple.push_back(tupleValue);//store strings to tuple
					tupleSize++;
					//cout << tupleValue << " ";
				}
				//cout << "adding" << facts.at(j).toString() << endl;

			relation.addToTuple(tuple);//add one facts to tuple

			}
			//cout << endl;
		}
		addRelation(relation);//insert relation to db
		//relation.printTuples();
	}
}

void DataBase::addRelation(Relation r) {
	db.insert(pair<string, Relation>(r.getName(), r));
}
void DataBase::setDB(string name, Relation relation) {
	db[name] = relation;
}
map<string, Relation> DataBase::getDB() {
	return db;
}


/*
void DataBase::addTuples(Tuple t, string name) {
	Relation r;
	string n = t.getName();


	//Relation r = db.find(name)->second;
	//db.at(name) = r;
}
*/
/*
void DataBase::runQueries() {
	Relation temp;
	for (unsigned int i = 0; i < queries.size(); i++) {
		string name = queries[i].getIdent();
		vector<Parameter> params = queries[i].getParam();

		// find relation in DB that matches the query
		Relation match = db.find(name)->second;
		temp = match;

		// loop through params of query
		for (unsigned int j = 0; j < params.size(); j++) {
			string type = params[j].getType();
			string value = params[j].getValue();

			if (type == "STRING") {
				temp = temp.select(j, value);
			}

			if (type == "ID") {

				// fix this for A, B, A case
				// check if it's a duplicate
				bool isDup = false;
				int dupPosition = 0;
				for (unsigned int k = 0; k < IDs.size(); k++) {
					if (IDs[k] == value) {
						isDup = true;
						dupPosition = k;
					}
				}


				if (isDup) {

					// select (pos 1, pos 2)
					//                    if (temp.getName() == ""){ // if temp hasn't been edited yet
					//                        temp = match;
					//                        temp = temp.select(dupPosition, j);
					//                    }

					//else {
					temp = temp.select(dupPosition, j);
					//}
				}

				else {
					// only do this if there are no dups
					IDs.push_back(value);
					positions.push_back(j);
				}
			}
		}

		temp = temp.project(positions);
		temp = temp.rename(IDs);

		// debug
		stringstream ss;
		for (unsigned int z = 0; z < params.size(); z++) {
			ss << params[z].toString();
			if (z == (params.size() - 1)) {}
			else ss << ",";
		}

		// print query
		int number = temp.numTuples(positions, IDs);
		cout << name << "(" << ss.str() << ")?";
		if (temp.tuples.size() > 0) {//&& (!(temp.tupleIsEmpty(temp.tuples)))) {
			cout << " Yes(" << number << ")" << endl;
		}
		else {
			cout << " No" << endl;
		}

		temp.toTuples(positions, IDs, number);
		IDs.clear();
		positions.clear();
	}
}
*/
/*
void DataBase::printToPrint() {
	for (int i = 0; i < toPrint.size(); i++) {
		toPrint[i].printRelation();
	}
}
*/