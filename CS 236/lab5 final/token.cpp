#include "token.h"
#include <sstream>
#include <vector>

Token::Token(TokenType t, string v, int l) {
	type = t;
	val = v;
	lineNum = l;
}
Token::~Token() {}

vector<string> printType = {"COMMA", "PERIOD", "Q_MARK", "LEFT_PAREN", "RIGHT_PAREN", "COLON", "COLON_DASH", "MULTIPLY", "ADD", "SCHEMES", "FACTS", "RULES", "QUERIES", "ID", "STRING", "COMMENT", "UNDEFINED", "EOF"};


string Token::printToken() {
	stringstream ss;
	ss << "(" << printType[type] << ",\"" << val << "\"," << lineNum << ")" << endl;
	return ss.str();
}

