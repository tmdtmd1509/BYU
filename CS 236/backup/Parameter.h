#include <string>

using namespace std;

class Parameter {
public:

	Parameter();
	Parameter(string tokenType, string val);
	~Parameter();

	string type;
	string value;

	string getValue();
	string getType();
	string toString();
};














Parameter
- string IdOrString;


//child or parameter
Expression
	Parameter* left
	operator
	Parameter* right


