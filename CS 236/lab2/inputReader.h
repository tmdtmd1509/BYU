#include <fstream>
#include <stdio.h>
#include <string>
#include "token.h"

using namespace std;

class InputReader {
public:
	InputReader(string fileName);
	char getNextChar(); //.get
	char peekNextChar(); //.peek
	int atEOF();
        bool isSpace(char myC);

	ifstream in;
	int lineNum;
};

