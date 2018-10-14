#include "Tuple.h"

string Tuple::getTuples() {
	return tuples;
}

void Tuple::printTuple() {
	for (unsigned int i = 0; i < size(); i++) {
		string output = (*this)[i];
		cout << output;
	}
	cout << endl << endl;
}
