#include "parameter.h"

using namespace std;

Parameter::Parameter() {
	// id or string
	type = "";
	value = "";
}

Parameter::Parameter(string tokenType, string val) {
	type = tokenType;
	value = val;
}

Parameter::~Parameter() {}

string Parameter::getValue() {
	return value;
}

string Parameter::getType() {
	return type;
}

string Parameter::toString() {
	/*	if(type == "ID")
	return value;
	else
	return ("'" + value + "'");*/
	return value;
}






Parameter() {
	if (string) {
		match
			makea a parameter pointer
			return parameter*
	}
	else if (ID)
		match(ID);
	makea parameter*
		return parameter*
	else
		expression* out = expression
		return out;
}

Expression* expression() {
	match(LP)
Parameter* left = Parameter()
	match(OP)
Parameter* right = Parameter()
	match(RP)

return Expression* out(left, op, right)
}


