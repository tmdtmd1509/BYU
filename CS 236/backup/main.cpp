#include "scanner.h"
#include "token.h"
#include "Parser.h"

using namespace std;

int main(int argc, char* argv[]) {
	string fileName = argv[1];
	scanner myS(fileName);

	vector<Token> myToken = myS.scan();
	try {
		Parser myParser(myToken);
		myParser.parse();
	}
	catch (string t) {
		cout << "Failure!" <<  "  " << t << endl;
	}

return 0;
}
