#include "inputReader.h"

InputReader::InputReader(string file) {
	in.open(file);
        lineNum = 1;
}

char InputReader::getNextChar() {
	char next;
	next = in.get();
	if (next == EOF) {
		return EOF;
	}
	return next;
}

char InputReader::peekNextChar() {
	char next1;
	next1 = in.peek();
	return next1;
}

bool InputReader::isSpace(char c) {
    if (isspace(c)) {
            if (c == ' ')
                return true;
            else if (c == '\n') {
                    lineNum++;
                    return true;
            }
    }
    return false;
}
