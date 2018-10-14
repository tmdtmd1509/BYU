
#include "Interpreter.h"
//#include "DataBase.h"

void Interpreter::resolveQueries(vector<Predicate> s, vector<Predicate> f, vector<Predicate> q, vector<Rule> r) {
	database.createDB(s, f);//make database

	makeGraph(r);
    resolveRules(r);
	createQuerie(q);//bring and evaluate queries
}
void Interpreter::makeGraph(vector<Rule> rule) {
	cout << "Dependency Graph" << endl;
	//graph
	forward(rule);
	//cout << "print1" << endl;

	reverse(rule);

	graphToString(forwardGraph);//print

	DFSforest();

}
void Interpreter::resolveRules(vector<Rule> rule) {
	//print rules

	cout << "Rule Evaluation" << endl;


	for (unsigned int it = 0; it < SCCs.size(); it++) {
		int passes = 0;

		cout << "SCC: ";
		printSCC(SCCs[it]);

		newRule = pickRules(rule, it);

		if (forwardGraph.graph[it].itSelf) {
			evaluateRule(newRule);
			//cout << "not itself" << endl;
			passes++;
		}
		else {
			while (changeCount > 0) {
				changeCount = 0;
				evaluateRule(newRule);
				//cout << "passes" << endl;
				passes++;
			}
		}

		cout << passes << " passes: ";
		printSCC(SCCs[it]);
	}


}

vector<Rule> Interpreter::pickRules(vector<Rule> rule, int it) {
	vector<Rule> newRule;
	for (auto j : SCCs[it]) {
		newRule.push_back(rule[j]);
	}
	return newRule;
}

void Interpreter::evaluateRule(vector<Rule> rule) {
	for (unsigned int i = 0; i < rule.size(); i++) {
		cout << rule[i].toString() << endl;//print rule
		createRule(rule[i]);
	}
}


void Interpreter::createRule(Rule rule) {
	Relation relation, newRelation, original, toUnion;
	db = database.getDB();

	Predicate head = rule.pred;
	vector<Predicate> querieList = rule.predicateList;


	//head query
	string headName = head.getIdent();//head name
	Scheme headScheme = makeVecToScheme(head);//head Scheme

        original = db[headName];
		original.setName(headName);

	//set queries to join first query become standard.
	string name = querieList[0].getIdent();//query name
	Scheme firstScheme = makeVecToScheme(querieList[0]);//query shceme

        newRelation = db[name];//copy database
		newRelation.setScheme(firstScheme);
		newRelation.setName(name);
		for (unsigned int i = 0; i < newRelation.schema.size(); i++) {
			if (newRelation.schema[i].at(0) == '\'') {//string?
				newRelation = newRelation.select(i, newRelation.schema[i]);
			}
		}

	//when more than 1 queries
	if (querieList.size() > 0) {
		//compare head with joined
		newRelation = makeQueryOne(newRelation, querieList);

		vector<int> pos;

		for (unsigned int i = 0; i < headScheme.size(); i++) {
			//cout << headScheme[i] << " ";
			for (unsigned int j = 0; j < newRelation.schema.size(); j++) {
				//cout << newRelation.schema[j] << endl;
				if (newRelation.schema[j] == headScheme[i]) {
					pos.push_back(j);			
				}
			}
		}

		//project
		newRelation = newRelation.project(pos);
		newRelation.setName(headName);

		//rename
		newRelation = newRelation.rename(headScheme);

		//union		
		isChange = original.unionAdd(newRelation);//union new tuples with original
		if (isChange) {
			changeCount++;
		}
		//cout << "test3" << original.tuples.size() << endl;
		database.setDB(original.getName(), original);//reset the relation
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

Relation Interpreter::makeQueryOne(Relation newRelation, vector<Predicate> querieList) {
	Relation nextRelation;
	while (querieList.size() != 0) {
		querieList.erase(querieList.begin());//since we used first one erase
		if (querieList.size() != 0) {
			string toUnionName = querieList[0].getIdent();//next query name
			Scheme toUnionScheme = makeVecToScheme(querieList[0]);//next query scheme
			nextRelation = db[toUnionName];//search relation by name and get it
			nextRelation.setScheme(toUnionScheme);
			nextRelation.setName(toUnionName);//set name

			newRelation = newRelation.join(newRelation, nextRelation);//join!
		}
	}
	return newRelation;
}

void Interpreter::forward(vector<Rule> rule) {
	Rule rules;
	Rule standardRule;
	for (unsigned int i = 0; i < rule.size(); i++) {
		Node node;
		rules = rule[i];
		//cout << "test1" << endl;
		for (unsigned int j = 0; j < rules.predicateList.size(); j++) {
			Predicate predicate = rules.predicateList[j];
			//cout << "test2" << endl;
			for (unsigned int k = 0; k < rule.size(); k++) {
				standardRule = rule[k];
				if (predicate.id == standardRule.pred.id) {
					node.children.insert(k);
					forwardGraph.graph[i] = node;
					//cout << "test3" << endl;
				}
				if (rules.pred.id == standardRule.pred.id) {
					node.itSelf = true;
					forwardGraph.graph[i] = node;
					cout << "test4 " << i << k << endl;
				}
			}
		}
	}
}

void Interpreter::reverse(vector<Rule> rule) {
	Rule rules;
	Rule standardRule;
	for (unsigned int i = 0; i < rule.size(); i++) {
		Node node;
		rules = rule[i];
		for (unsigned int j = 0; j < rules.predicateList.size(); j++) {
			Predicate predicate = rules.predicateList[j];
			for (unsigned int k = 0; k < rule.size(); k++) {
				standardRule = rule[k];
				if (predicate.id == standardRule.pred.id) {
					node.children.insert(i);
					reverseGraph.graph[k] = node;
					//cout << "test5" << endl;
				}
				if (rules.pred.id == standardRule.pred.id) {
					node.itSelf = true;
					reverseGraph.graph[i] = node;
					//cout << "test6" << endl;
				}
			}
		}
	}
}

void Interpreter::DFSforest() {
	//cout << "test7" << endl;
	for (unsigned int i = 0; i < reverseGraph.graph.size(); i++) {
		DFSreverse(reverseGraph.graph, i);
	}

	for (unsigned int j = 0; j < postOrderList.size(); j++) {
		DFSforward(forwardGraph.graph, postOrderList.top());
		postOrderList.pop();
		if (SCC.size() != 0) {
			SCCs.push_back(SCC);
			SCC.clear();
		}
	}

}

void Interpreter::DFSreverse(map<int, Node> graph, int node) {
	//cout << "test8" << endl;
	if (!graph[node].visited) {
		graph[node].visited = true;
		//cout << "test9" << endl;
		for (auto it : graph[node].children) {
			DFSreverse(graph, it);
		}
		postOrderList.push(node);
	}
}

void Interpreter::DFSforward(map<int, Node> graph, int node) {
	//cout << "test10" << endl;

	if (!graph[node].visited) {
		//cout << "test11" << endl;
		graph[node].visited = true;
		for (auto it : graph[node].children) {
			DFSforward(graph, it);
		}
		SCC.insert(node);
	}
}


void Interpreter::createQuerie(vector<Predicate> q) {
	db = database.getDB();
	Scheme newScheme;
	vector <int>firstValueIndex;
	vector <string> firstValue;
        cout << "Query Evaluation" << endl;

	for (unsigned int i = 0; i < q.size(); i++) {//see each query
		Relation newRelation;
		string name = q[i].getIdent();//query name
		vector<Parameter> queryValues = q[i].getParam();//list of query's value
		//get strings from parameter
		vector<string> paramVal; //contain elements of query
		for (unsigned num = 0; num < queryValues.size(); ++num) {
			paramVal.push_back(queryValues[num].getValue());
		}
		newRelation = db.find(name)->second;

		for (unsigned int j = 0; j < paramVal.size(); j++) {//distinguish string or ID, and how many strings?
			if (paramVal[j].at(0) == '\'') {//string?
				newRelation = newRelation.select(j, paramVal[j]);
				//newRelation.printTuples();
			}
			else {//ID?
				unsigned int index = find(firstValue, paramVal[j]);
				if (index != firstValue.size()) {
					newRelation = newRelation.select(j, firstValueIndex.at(index));
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

void Interpreter::graphToString(Graph forwardGraph) {
	for (unsigned int i = 0; i < forwardGraph.graph.size(); i++) {
		cout << "R" << i << ":";
		unsigned int commaCount = 0;
		for (auto j : forwardGraph.graph[i].children) {
			commaCount++;
			if (commaCount != forwardGraph.graph[i].children.size())
			cout << "R" << j << ",";
			else {
				cout << "R" << j << endl;
			}
		}
		if (forwardGraph.graph[i].children.size() == 0) {
			cout << endl;
		}
	}
	cout << endl;
}

void Interpreter::printSCC(set<int> oneSCC) {
	unsigned int commaCount = 0;
	for (auto i : oneSCC) {
		commaCount++;
		if (commaCount != oneSCC.size())
			cout << "R" << i << ",";
		else {
			cout << "R" << i << endl;
		}
	}
}