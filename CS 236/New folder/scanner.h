#pragma once
#include <iostream>
#include <stdio.h>
#include <string>
#include <map>
#include <vector>
#include "token.h"
#include "inputReader.h"

class scanner {
public:
	scanner(string fileName);
	InputReader in;
	vector<Token>scan();

	Token StringChecker();
	Token CommentChecker();
	    Token singleComment(char c);
       Token doubleComment(char c);
	Token CheckID(string word);

	//Token CheckColonDash();
	int CheckKeywords(string word);
        void defaultHandler(char c, vector<Token> &token);
};
