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
vector<string> Relation::printSchemes() {
        vector<string>vecScheme;
        for (auto it : schema) {
                vecScheme.push_back(it);
        }
        return vecScheme;
}

vector<string> Relation::printTuples() {
        vector<string>vecTuple;
        for (set<Tuple>::iterator it = tuples.begin(); it != tuples.end(); it++) {
                Tuple tup = *it;
                for (unsigned int i = 0; i < tup.size(); i++) {
                        vecTuple.push_back(tup[i]);
                }
        }
        return vecTuple;
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
/*
Relation Relation::project(vector<int> positions) {
	Relation r;
	for (auto tup : tuples) {
		Tuple temp;
		for (unsigned int i = 0; i < positions.size(); i++) {
			r.schema.push_back(schema[positions[i]]);

			for (unsigned k = 0; k < schema.size(); k++) {
				for (unsigned j = 1; j < schema.size(); j++) {
					if (schema[k] == schema[j]) {
						schema.erase(schema.begin() + k);
					}
				}
			}
			temp.push_back(tup[positions[i]]);
		}
		r.tuples.insert(temp);
	}

	if (positions.size() == 0) {
		r.tuples = tuples;
	}
	return r;
}
*/
//new faster
Relation Relation::project(vector<int> positions) {
	Relation r;
	int indexPos;
	Scheme schem;
        Tuple tup;
	for (auto temp : tuples) {
		for (unsigned int i = 0; i < positions.size(); i++) {
			indexPos = positions[i];
			tup.push_back(temp[indexPos]);
			schem.push_back(schema[indexPos]);
		}
		r.tuples.insert(tup);
	}
	r.schema = schem;
        cout << "test"<< endl;
        for (auto printing : r.schema) {
            cout << "out" << printing << endl;
        }
	return r;
}

//added project
Relation Relation::project(vector<Parameter> paramList) {
	Relation r;
	r.schema = schema;
	vector<int> positions;
	
	for (unsigned int i = 0; i < paramList.size(); i++) {
		for (unsigned int j = 0; j < r.schema.size(); j++) {
			if (paramList.at(i).getValue() == r.schema.at(j)) {
				positions.push_back(j);
			}
		}
	}
	r = project(positions);
	return r;
}


Relation Relation::rename(Scheme scheme) {
	Relation r;
	r.schema = scheme;
	r.tuples = tuples;

	return r;
}

Relation Relation::unionAdd(Relation newRelation) {
	Relation r;
	r.schema = schema;
	r.tuples = tuples;
	if (newRelation.getTuple().size() > 0) {
		for (auto it : newRelation.getTuple()) {
			r.addToTuple(it);
			isChange++;
		}
	}
        for (auto tupl : tuples) {
                cout << " ";
                for (unsigned int j = 0; j < schema.size(); j++) {
                    cout << schema[j] << "=" << tupl[j] << endl;
                    if (j != schema.size()-1) {
                        cout << ", ";
                    }
                }
            cout << endl;
        }
	return r;
}

Relation Relation::join(Relation r1, Relation r2) {
	Scheme s1 = r1.schema;
	Scheme s2 = r2.schema;
	Scheme s3 = joinScheme(s1, s2);//join Scheme
    Relation r3;//push s3 in the r3p

    r3.schema = s3;
    //Tuple t1;
    //Tuple t2;
	for (auto t1 : r1.tuples) {
		for (auto t2 : r2.tuples) {
			if (isJoinable(t1, t2, matchingCols)) {
				Tuple t3 = joinTuple(t1, t2, uniqueCols);//join Tuple
				r3.tuples.insert(t3);
			}
		}
	}
	/*
    for(unsigned int i = 0; i < r1.tuples.size(); i++) {

        for(unsigned int j = 0; j < r2.tuples.size(); j++) {
            t2.push_back(r2.tuples[j]);
            if(isJoinable(s1, t1, s2, t2)) {
                Tuple t3 = joinTuple(s1, t1, s2, t2);
                r3.tuples = t3;
            }
        }
    }
	*/
    return r3;
}

Scheme Relation::joinScheme(Scheme s1, Scheme s2) {
    Scheme s3 = s1;
	vector<int> uniqueCols;
	for (unsigned int i = 0; i < s2.size(); i++) {
		unsigned int different = 0;
		for (unsigned int j = 0; j < s1.size(); j++) {
			if (s1[j] == s2[i]) {
				intPair = make_pair(j, i);
				matchingCols.push_back(intPair);
				break;
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

Tuple Relation::joinTuple(Tuple t1, Tuple t2, vector<int> uniqueCols) {
	Tuple t3 = t1;
	for (unsigned int j = 0; j < uniqueCols.size(); j++) {
		t3.push_back(t2.at(j));
	}
	return t3;
}

bool Relation::isJoinable(Tuple t1, Tuple t2, vector<pair<int, int>> matchingCols) {

	for (unsigned int i = 0; i < matchingCols.size(); i++) {
		int p1 = matchingCols.at(i).first;
		int p2 = matchingCols.at(i).second;
		if (t1[p1] != t2[p2]) {
			return false;
		}
	}
	return true;
}


/*
Tuple Relation::joinTuple(Scheme s1, Tuple t1, Scheme s2, Tuple t2) {
	Tuple t3 = t1;
	for (unsigned int i = 0; i < t2.size(); i++) {
		for (unsigned int j = 0; j < s1.size(); j++) {
			if (s1[j] == s2[i]) {

			}
			else {
				t3.push_back(t2.at(i));
			}
		}
	}
    return t3;
}
*/
/*
bool isJoinable(Scheme s1, Tuple t1, Scheme s2, Tuple t2) {
    for(unsigned int i = 0; i < s1.size(); i++) {
        for(unsigned int j = 0; j < s2.size(); j++) {
            if (s1[i] == s2[j]) {
                if(t1[i] != t2[j]) {
                    return false;
                }
            }
        }
    }
    return true;
}
*/

