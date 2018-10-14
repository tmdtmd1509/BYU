#include "scanner.h"
#include "token.h"
#include "Parser.h"
#include "DatalogProgram.h"
#include <cctype>
#include <sstream>
#include <fstream>

using namespace std;

int main(int argc, char* argv[]) {
	string fileName = argv[1];
	scanner myS(fileName);
	stringstream outs;
	DLP s;

	vector<Token> myToken = myS.scan();
	try {
		Parser myParser(myToken);
		myParser.parse();
		s = myParser.datalogProgram();
		outs << s.toString();
		cout << outs.str();

	}
	catch (string t) {

		cout << "Failure!" <<  "\n  " << t << endl;
	}

return 0;
}
