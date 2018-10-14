#pragma once
#include <string>
#include "token.h"

using namespace std;

class Parameter {
public:

	Parameter(TokenType type, string val);
	~Parameter();

	string getType();
	string getValue();

	string type;
	string value;

	string toString();
};
