
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
	forward(rule);
	reverse(rule);
	graphToString(forwardGraph);//print
	DFSforest();

}
void Interpreter::resolveRules(vector<Rule> rule) {
	//print rules

	cout << "Rule Evaluation" << endl;

        for (unsigned int i = 0; i < SCCs.size(); i++) {
		int passes = 0;

		cout << "SCC: ";
                printSCC(SCCs[i]);
                vector<int> nodeNum;
                for (unsigned int it : SCCs[i]) {
                    nodeNum.push_back(it);
                }
                newRule = pickRules(rule, i);

                if (SCCs[i].size() == 1 && !forwardGraph.graph[nodeNum[0]].itSelf) {
                        evaluateRule(newRule);
                        //cout << "not itself" << endl;
			passes++;
		}
                else {
                    do {
                        //cout << "why this?" << endl;
                       //cout << "change Count " << changeCount << endl;
                           changeCount = 0;
                           evaluateRule(newRule);
                           //cout << "passes" << endl;
                           passes++;
                   }while (changeCount > 0);
                }

		cout << passes << " passes: ";
                printSCC(SCCs[i]);
	}


}

vector<Rule> Interpreter::pickRules(vector<Rule> rule, int i) {
	vector<Rule> newRule;
        for (auto j : SCCs[i]) {
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
                    //cout << "changed!"<< endl;
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
                forwardGraph.graph[i] = node;
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
                    //cout << "test3" << i << k << endl;
                }
                for (unsigned int it : node.children) {
                     if (i == it) {
                         //cout << "true " << i << it << endl;
                         forwardGraph.graph[i].itSelf = true;
                     }
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
		reverseGraph.graph[i] = node;
	}
	for (unsigned int i = 0; i < rule.size(); i++) {
		Node node;
		rules = rule[i];
		for (unsigned int j = 0; j < rules.predicateList.size(); j++) {
			Predicate predicate = rules.predicateList[j];
			for (unsigned int k = 0; k < rule.size(); k++) {
                standardRule = rule[k];
                if (predicate.id == standardRule.pred.id) {
					reverseGraph.graph[k].children.insert(i);
					//cout << "test3" << k << i << endl;

				}
			}
		}
	}
}

/*
void Interpreter::forward(vector<Rule> rule) {
        for (unsigned int i = 0; i < rule.size(); i++) {
            Node node;
                Rule rules = rule[i];
                //cout << "test1" << endl;
                for (unsigned int j = 0; j < rules.predicateList.size(); j++) {
                        Predicate predicate = rules.predicateList[j];
                        //cout << "test2" << endl;
                        for (unsigned int k = 0; k < rule.size(); k++) {
                                Rule standardRule = rule[k];
                                if (predicate.id == standardRule.pred.id) {
                                        node.children.insert(k);
                                        forwardGraph.graph[i] = node;
                                        //cout << "test3" << endl;
                                }
                                if (i == k) {
                                        node.itSelf = true;
                                        forwardGraph.graph[i] = node;
                                        cout << "test4" << i << " " << k << endl;
                                }


                        }


                }
        }
}

void Interpreter::reverse(vector<Rule> rule) {
    for (unsigned int i = 0; i < rule.size(); i++) {
        Node node;
            Rule rules = rule[i];
            //cout << "test1" << endl;
            for (unsigned int j = 0; j < rules.predicateList.size(); j++) {
                    Predicate predicate = rules.predicateList[j];
                    //cout << "test2" << endl;
                    for (unsigned int k = 0; k < rule.size(); k++) {
                            Rule standardRule = rule[k];
                            if (predicate.id == standardRule.pred.id) {
                                    node.children.insert(i);
                                    forwardGraph.graph[k] = node;
                                    //cout << "test3" << endl;
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

*/


void Interpreter::DFSforest() {
	//cout << "test7" << endl;
	for (unsigned int i = 0; i < reverseGraph.graph.size(); i++) {
                DFSreverse(i);
	}
        unsigned int stackSize = postOrderList.size();
        for (unsigned int j = 0; j < stackSize; j++) {
			//cout << postOrderList.top() << endl;
                DFSforward(postOrderList.top());
                postOrderList.pop();
                if (SCC.size() != 0) {
			SCCs.push_back(SCC);
                        //cout << "push back SCC to SCCs" << endl;
			SCC.clear();
		}
	}

}

void Interpreter::DFSreverse(int node) {
	//cout << "test " << node << endl;
        if (!reverseGraph.graph[node].visited) {
                reverseGraph.graph[node].visited = true;
		//cout << "mark" << endl;
                for (auto it : reverseGraph.graph[node].children) {
					//cout << "go to children" << it << endl;
                        DFSreverse(it);
				}
				//cout << "stack node" << node << endl;
				postOrderList.push(node);
		}
		//cout << "passed" << endl;
}

void Interpreter::DFSforward(int node) {
	//cout << "test10" << endl;

        if (!forwardGraph.graph[node].visited) {
		//cout << "test11" << endl;
                forwardGraph.graph[node].visited = true;
                for (auto it : forwardGraph.graph[node].children) {
                        DFSforward(it);
		}
		SCC.insert(node);
        }
}


void Interpreter::createQuerie(vector<Predicate> q) {
	db = database.getDB();
	Scheme newScheme;
	vector <int>firstValueIndex;
	vector <string> firstValue;
        cout << endl << "Query Evaluation" << endl;

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

		newRelation.printTuples(q[i], name);
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
	//relation.printTuples(Predicate predicate, string name);
	// print query
	//vector<string> sche = relation.printSchemes();//schemes (col)
	//vector<string> tup = relation.printTuples();//tuples (row)
	/*
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
				cout << sche[i] << "=" << tup[i];
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
	*/
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

void Interpreter::graphToString2(Graph reverseGraph) {
	for (unsigned int i = 0; i < reverseGraph.graph.size(); i++) {
		cout << "R" << i << ":";
		unsigned int commaCount = 0;
		for (auto j : reverseGraph.graph[i].children) {
			commaCount++;
			if (commaCount != reverseGraph.graph[i].children.size())
				cout << "R" << j << ",";
			else {
				cout << "R" << j << endl;
			}
		}
		if (reverseGraph.graph[i].children.size() == 0) {
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
