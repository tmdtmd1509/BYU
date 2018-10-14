#include "Parser.h"
#include "token.h"

Parser::Parser(vector<Token>& Tokens): myTok(Tokens), current(myTok[0]) {
	myIndex = 0;
	current = myTok[myIndex];
}

Parser::~Parser() {
}

void Parser::parse() {
	try {
		match(SCHEMES);
		match(COLON);
		scheme();
		schemeList();

		match(FACTS);
		match(COLON);
		factList();

		match(RULES);
		match(COLON);
		ruleList();

		match(QUERIES);
		match(COLON);
		query();
		queryList();
		match(EndOfFile);
	}
	catch (exception e) {
		cout << "failer" << endl;
		throw;
	}
	cout << "Success!" << endl;
}

void Parser::match(TokenType expected) {
	if (current.type == expected) {
		++myIndex;
		current = myTok[myIndex];
		cout << "match" << endl;
	}
	else {
		cout << "fail" << current.type << endl;
		throw runtime_error("no match");
	}
}

void Parser::scheme(){
	headPredicate();
}

void Parser::headPredicate() {
	match(ID);
	match(LEFT_PAREN);
	match(ID);
	idList();
	match(RIGHT_PAREN);
}

void Parser::idList() {
	if (current.type == COMMA) {
		match(COMMA);
		match(ID);
		idList();
	}
	else {
		return;
	}
}

void Parser::schemeList() {
	if (current.type == ID) {
		scheme();
		schemeList();
	}
}

void Parser::predicate() {
	match(ID);
	match(LEFT_PAREN);
	parameter();
	parameterList();
	match(RIGHT_PAREN);
}

void Parser::parameter() {
	if (current.type == STRING) {
		match(STRING);
	}
	else if (current.type == ID) {
		match(ID);
	}
}

void Parser::parameterList() {
	if (current.type == COMMA) {
		match(COMMA);
		predicate();
		parameterList();
	}
	else {
		return;
	}
}

void Parser::fact() {
	predicate();
	match(PERIOD);
}


void Parser::factList() {
	if (current.type == ID) {
		fact();
		factList();
	}
}

void Parser::rule() {
	headPredicate();
	match(PERIOD);
}

void Parser::ruleList() {
	if (current.type == ID) {
		rule();
		ruleList();
	}
}

void Parser::query() {
	predicate();
	match(Q_MARK);
}

void Parser::queryList() {
	if (current.type == ID) {
		query();
		queryList();
	}
	else return;
}



