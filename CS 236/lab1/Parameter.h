#pragma once
#include <string>

using namespace std;

class Parameter {
public:

	Parameter(string type, string val);
	~Parameter();

	string type;
	string value;

	string getValue();
	string getType();
	string toString();
};
