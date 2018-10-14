#include "Relation.h"
#include <sstream>

Relation::Relation()
{
}

Relation::Relation(string n, Scheme s) {
	name = n;
	schema = s;
}
Relation::~Relation() {}

void Relation::setName(string n)
{
	name = n;
}

string Relation::getName()
{
	return name;
}

void Relation::setScheme(Scheme s)
{
	schema = s;
}

Scheme Relation::getScheme()
{
	return schema;
}

set<Tuple> Relation::getTuple()
{
	return tuples;
}
void Relation::addToTuple(Tuple t)
{
	tuples.insert(t);

}
/*
vector<string> Relation::printSchemes() {
        vector<string>vecScheme;
        for (auto it : schema) {
                vecScheme.push_back(it);
        }
        return vecScheme;
}
*/
void Relation::printTuples(Predicate predicate, string name) {
			unsigned int number = tuples.size();
			cout << predicate.toString() << "? ";//SK(a, b)? 
			if (number >= 1) {//if tupe exist
				cout << "Yes(" << number << ")" << endl;//howmany tuple Yes(0)
				for (const auto &tup : tuples) {//recurs many as row
					if (schema.size() == 0) {
						break;
					}
					cout << " ";
					for (unsigned i = 0; i < schema.size(); i++) {//recurs many as col
						cout << schema[i] << "=" << tup[i];
						if (i != schema.size() - 1) {
							cout << ", ";
						}
					}
					cout << endl;
				}
			}
			else {
				cout << "No" << endl;
			}
}
//not use
Relation Relation::select(unsigned int colNum, string value) {
	Relation r;
	r.schema = schema;//set scheme
	for (auto it : tuples) {
		if (it[colNum] == value) {
			r.addToTuple(it);
		}
	}
	return r;
}

Relation Relation::select(int colNum, int dupleNum) {
	Relation r;
	r.schema = schema;
	for (auto it : tuples) {
		if (it[colNum] == it[dupleNum]) {
			r.addToTuple(it);
		}
	}
	return r;
}

//new faster
Relation Relation::project(vector<int> positions) {
	Relation r;
	int indexPos;
	Scheme schem;
	//tuple
	for (auto temp : tuples) {
		Tuple tup;
		for (unsigned int i = 0; i < positions.size(); i++) {
			indexPos = positions[i];
			tup.push_back(temp[indexPos]);
		}
		r.tuples.insert(tup);
	}

	//scheme
	for (unsigned int i = 0; i < positions.size(); i++) {
		indexPos = positions[i];
		schem.push_back(schema[indexPos]);
	}
	r.schema = schem;
        //cout << "test"<< endl;
        //for (auto printing : r.schema) {
        //    cout << "out" << printing << endl;
        //}
	return r;
}

Relation Relation::rename(Scheme scheme) {
	Relation r;
	r.name = name;
	r.schema = scheme;
	r.tuples = tuples;

	return r;
}


bool Relation::unionAdd(Relation newRelation) {
	unsigned int countChange = tuples.size();
	for (auto tup : newRelation.getTuple()) {
		if (tuples.insert(tup).second) {
			for (unsigned int j = 0; j < schema.size(); j++) {
				cout << " ";
				cout << schema[j] << "=" << tup[j];
				if (j != schema.size() - 1) {
					cout << ",";
				}
			}
			cout << endl;
		}
	}
	if (tuples.size() == countChange) {
		return false;
	}
	return true;
}

Relation Relation::join(Relation r1, Relation r2) {
	Scheme s1 = r1.schema;
	Scheme s2 = r2.schema;
	Scheme s3 = joinScheme(s1, s2);//join Scheme
    Relation r3;//push s3 in the r3p
	Tuple t3;
	r3.name = name;


    r3.schema = s3;
	r3.tuples.clear();
//join tuple
	for (const auto &t1 : r1.tuples) {
		for (const auto &t2 : r2.tuples) {
			if (isJoinable(t1, t2, matchingCols)) {
				t3 = joinTuple(t1, t2, uniqueCols);//join Tuple
				r3.tuples.insert(t3);
			}
		}
	}

	uniqueCols.clear();
	matchingCols.clear();
    return r3;
}

Scheme Relation::joinScheme(Scheme s1, Scheme s2) {
    Scheme s3 = s1;
	for (unsigned int i = 0; i < s2.size(); i++) {
		unsigned int different = 0;
		for (unsigned int j = 0; j < s1.size(); j++) {
			if (s1[j] == s2[i]) {
				intPair = make_pair(j, i);
				matchingCols.push_back(intPair);
			}
			else if (different == s1.size()-1) {
				s3.push_back(s2[i]);
				uniqueCols.push_back(i);
			}
			else {
				different++;
			}
		}
	}
    return s3;
}

Tuple Relation::joinTuple(const Tuple &t1, const Tuple &t2, const vector<int> &uniqueCols) {
	Tuple t3 = t1;
	for (unsigned int j = 0; j < uniqueCols.size(); j++) {
		t3.push_back(t2[uniqueCols.at(j)]);
	}
	return t3;
}

bool Relation::isJoinable(const Tuple &t1, const Tuple &t2, const vector<pair<int, int>> &matchingCols) {

	for (unsigned int i = 0; i < matchingCols.size(); i++) {
		int p1 = matchingCols.at(i).first;
		int p2 = matchingCols.at(i).second;
		if (t1[p1] != t2[p2]) {
			return false;
		}
	}
	if (matchingCols.size() == 0) {
		return true;
	}
	return true;
}
