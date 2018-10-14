
#include "Interpreter.h"
//#include "DataBase.h"

void Interpreter::resolveQueries(vector<Predicate> s, vector<Predicate> f, vector<Predicate> q, vector<Rule> r) {
	database.createDB(s, f);//make database

    resolveRules(r);
	createQuerie(q);//bring and evaluate queries
}
void Interpreter::resolveRules(vector<Rule> rule) {
    //print rules
		int passes = 0;
		bool change = true;
		cout << "Rule Evaluation" << endl;

		while(change) {
			changeCount = 0;
			//sizeBefore = database.tupleSize;//모든 relation의 tuple의 합으로 바꾸자
			evaluateRule(rule);
			passes++;
			//sizeAfter = database.tupleSize;//모든 relation의 tuple의 합으로 바꾸자
			/*
			if (sizeBefore == sizeAfter) {
				change = false;
			}
			*/
			if (changeCount == 0) {
				cout << "test7" << passes << endl;

				change = false;
			}
		}

		
        cout << "Schemes populated after " << passes << " passes through the Rules." << endl;
}
void Interpreter::evaluateRule(vector<Rule> rule) {

	for (unsigned int i = 0; i < rule.size(); i++) {
		cout << rule[i].toString() << endl;//print rule
		createRule(rule[i]);
	}
}


void Interpreter::createRule(Rule rule) {
	Relation unioned, relation, newRelation, original, toUnion;
	db = database.getDB();

	Predicate head = rule.pred;
	vector<Predicate> querieList = rule.predicateList;


	//head query
	string headName = head.getIdent();//head name
	Scheme newScheme = makeVecToScheme(head);//head Scheme

	original = db.find(headName)->second;

	//set queries to join first query become standard.
	string name = querieList[0].getIdent();//query name
	Scheme firstScheme = makeVecToScheme(querieList[0]);//query shceme

	newRelation = db.find(name)->second;//copy database
	cout << "test1" << endl;


	//when only 1 query
	if (querieList.size() == 1) {
		cout << "test2" << endl;
	}

	//when more than 2 queries
	else if (querieList.size() > 1) {
		cout << "test3" << endl;
		while (querieList.size() == 0) {
			querieList.erase(querieList.begin());
			string toUnionName = querieList[0].getIdent();//next query name
			Scheme toUnionScheme = makeVecToScheme(querieList[0]);//next query scheme

			unioned = db.find(toUnionName)->second;

			newRelation = relation.join(newRelation, unioned);
		}

		//compare head with joined
		for (unsigned int i = 0; i < newScheme.size(); i++) {
			for (unsigned int j = 0; j < newRelation.getScheme().size(); j++) {
				if (newRelation.getScheme()[j] == newScheme[i]) {
					positions.push_back(i);
				}
			}
		}
		cout << "test4" << endl;

		//project
		newRelation = newRelation.project(positions);
		cout << "test5" << endl;

		//rename
		newRelation = newRelation.rename(original.getScheme());
		cout << "test6" << endl;

		//print new tuples
		toStringRule(newRelation, newRelation.getName());
		cout << "test7" << endl;

		//union
		toUnion = db.find(newRelation.getName())->second;//copy the original relation
		cout << "test8" << endl;


		newRelation = toUnion.unionAdd(newRelation);//union new tuples with original
		cout << "test9" << endl;


		changeCount = newRelation.isChange;//isChanged?
		cout << "test10" << endl;


		database.setDB(newRelation.getName(), newRelation);//reset the relation
	}
}

Scheme Interpreter::makeVecToScheme(Predicate querie) {
	vector<Parameter> paramVal = querie.getParam();
	Scheme schem;
	for (unsigned i = 0; i < paramVal.size(); ++i) {//query scheme get only strings
		schem.push_back(paramVal[i].getValue());
	}
	return schem;
}



	//////////////////////////////////////////////////////
/*
	Predicate head = rule.pred;
	vector<Predicate> querieList = rule.predicateList;

	db = database.getDB();


	Relation joined = NULL;

	Scheme newScheme;
	vector <int>firstValueIndex;
	vector <string> firstValue;


	for (unsigned int i = 0; i < querieList.size(); i++) {//see each query
		Relation newRelation;
		string name = querieList[i].getIdent();//query name
		vector<Parameter> queryValues = querieList[i].getParam();//list of query's value
		newRelation = db.find(name)->second;
		vector<string> paramVal; //contain elements of query
		//copy elements to paramVal. its all IDs
		for (unsigned num = 0; num < queryValues.size(); ++num) {
			paramVal.push_back(queryValues[num].getValue());
		}
		pair<unsigned int, Relation> queryResult = runQuery(newRelation, querieList[i]);
		if (joined == NULL;) {
			joined = get<1>(queryResult);
		}
		else {
			joined = newRelation.join(joined, get<1>(queryResult));
		}
	}

	if (joined == NULL) {
		return false;
	}

	vector<Parameter> parameterList = head.paramList;
	Scheme newScheme;
	for (unsigned int i = 0; i < parameterList.size(); i++) {
		newScheme.push_back(parameterList[i].getValue());
	}

	Relation newRelation;
	Relation projected = newRelation.project(joined, newScheme);

	string relationName = head.getIdent;
	Relation originalRelation = db.find(relationName)->second;
	if (originalRelation == = NULL) {
		cout << "error" << endl;
	}

	Relation newRelation;
	Relation renamed = newRelation.rename(projectd, originalRelation.schema());

	unsigned int sizeBefore = 0;
	if (originalRelation != NULL) {
		sizeBefore = originalRelation.getTuple().size()
	}

	Relation newRelation;
	Relation unioned = newRelation.unionAdd(originalRelation, renamed);
	DataBase database;
	database.addRelation(unioned);

	if (unioned.getTuple().size() != sizeBefore) {
		return;
	}

	return false;
}
*/
void Interpreter::evaluateQuerie(vector<Predicate> q) {
}
/*
pair<unsigned int, Relation> Interpreter::runQuery(Relation relation, Predicate query) {
	Relation newRelation;
	vector<Parameter> parameterList = query.paramList;
	Scheme values;
	for (unsigned int i = 0; i < parameterList.size(); i++) {
		values.push_back(parameterList[i].getValue());
	}
	Relation selected = newRelation.select(relation, values);

	vector<string> newScheme;
	for (unsigned int i = 0; i < parameterList.size(); i++) {
		newScheme.push_back(relation.getScheme[i]);
	}
	Relation projected = newRelation.project(selected, newScheme);

	Scheme scheme;
	for (unsigned int i = 0; i < parameterList.size(); i++) {
		scheme.push_back(parameterList[i].getValue());
	}
	Relation renamed = newRelation.rename(projected, scheme);

	return pair<unsigned int, Relation>(selected.getTuple().size(), renamed);
}
*/
void Interpreter::createQuerie(vector<Predicate> q) {
	db = database.getDB();
	Scheme newScheme;
	vector <int>firstValueIndex;
	vector <string> firstValue;

	for (unsigned int i = 0; i < q.size(); i++) {//see each query
		Relation newRelation;
		string name = q[i].getIdent();//query name
		vector<Parameter> queryValues = q[i].getParam();//list of query's value
		//get strings from parameter
		vector<string> paramVal; //contain elements of query
		for (unsigned num = 0; num < queryValues.size(); ++num) {
			paramVal.push_back(queryValues[num].getValue());
		}
		//for (unsigned int a = 0; a < paramVal.size(); a++) {
			//cout << paramVal[a] << " ";
		//}
		//cout << endl;
                //find same name relation
		newRelation = db.find(name)->second;
		//newRelation.printTuples();

		for (unsigned int j = 0; j < paramVal.size(); j++) {//distinguish string or ID, and how many strings?
			if (paramVal[j].at(0) == '\'') {//string?
				newRelation = newRelation.select(j, paramVal[j]);
				//newRelation.printTuples();
			}
			else {//ID?
				unsigned int index = find(firstValue, paramVal[j]);
				//unsigned int duplePosition = 0;
				//duplePosition = checkIsId(paramVal, j);
				//if (duplePosition > j) {
				//	newRelation = newRelation.select(duplePosition, j);
				//}
				if (index != firstValue.size()) {
					newRelation = newRelation.select(j, firstValueIndex.at(index));
					//newRelation.printTuples();
				}
				else {
					firstValueIndex.push_back(j);
					firstValue.push_back(paramVal[j]);
					positions.push_back(j);
					newScheme.push_back(paramVal[j]);
				}
			}
		}
		newRelation = newRelation.project(positions);
		newRelation = newRelation.rename(newScheme);

		toString(newRelation, q[i], name);
		//justincase clear
		firstValue.clear();
		firstValueIndex.clear();
		newScheme.clear();
		positions.clear();
	}
}
/*
int Interpreter::checkIsId(vector<string> paramVal, unsigned int j) {
	unsigned int duplePosition = 0;
	for (unsigned int dup = j + 1; dup < paramVal.size(); dup++) {
		if (paramVal[j] == paramVal[dup]) {
			duplePosition = dup;
		}
	}
	return duplePosition;
}
*/
int Interpreter::find(vector<string> valueVec, string value) {
	for (unsigned i = 0; i < valueVec.size(); i++) {
		if (valueVec[i] == value) {
			return i;
		}
	}
	return valueVec.size();
}




void Interpreter::toString(Relation relation, Predicate predicate, string name) {
	// print query
	vector<string> sche = relation.printSchemes();//schemes (col)
	vector<string> tup = relation.printTuples();//tuples (row)
	unsigned int number = relation.tuples.size();
        cout << "Query Evaluation" << endl;
	cout << predicate.toString() << "? ";//SK(a, b)? 
	if (number >= 1) {//if tupe exist
		cout << "Yes(" << number << ")" << endl;//howmany tuple Yes(0)
		for (unsigned int num = 0; num < number; num++) {//recurs many as row
			if (sche.size() == 0) {
				break;
			}
			cout << " ";
			for (unsigned i = 0; i < sche.size(); i++) {//recurs many as col
				cout << sche[i] << "=" << tup.front();
				tup.erase(tup.begin());
				if (i != sche.size() - 1) {
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

void Interpreter::toStringRule(Relation relation, string name) {
	Scheme sche = relation.schema;
	set<Tuple> tup = relation.tuples;

	for (unsigned int i = 0; i < tup.size(); i++) {//recurs many as row
		cout << " ";

		for (unsigned int j = 0; j < sche.size(); j++) {
			cout << sche[j] << "=" << tup[i][j] << endl;
			if (j+1 != sche.size()) {

				cout << ", ";
			}
		}
		cout << "test 6-3" << endl;

		cout << endl;
    }
}
