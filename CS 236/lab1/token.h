#pragma once
#include <iostream>
#include <stdio.h>
#include <string>

using namespace std;

enum TokenType {COMMA, PERIOD, Q_MARK, LEFT_PAREN, RIGHT_PAREN, COLON, COLON_DASH, MULTIPLY, ADD, SCHEMES, FACTS, RULES, QUERIES, ID, STRING, COMMENT, UNDEFINED, EndOfFile};

class Token {
public:
	Token(TokenType type, string val, int lineNum);
	~Token();

	string printToken();

	TokenType type;
	string val;
	int lineNum;

};

