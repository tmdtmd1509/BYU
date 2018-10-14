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

//match
////////////////////////////////////////////
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

//parse scheme data
//////////////////////////////////////////////
void Parser::scheme(){
	p = Predicate(current.val);
	headPredicate();
	dataLogPro.addScheme(p);
}

void Parser::headPredicate() {
	match(ID);
	match(LEFT_PAREN);
	if (current.type == ID) {
		Predicate::addParameter(Parameter(current.type, current.val));
	}
	match(ID);
	idList();
	match(RIGHT_PAREN);
}

void Parser::idList() {
	if (current.type == COMMA) {
		match(COMMA);
		if (current.type == ID) {
			Predicate::addParameter(Parameter(current.type, current.val));
		}
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
//parse fact data
///////////////////////////////////////////////////
void Parser::fact() {
	p = Predicate(current.val);
	match(ID);
	match(LEFT_PAREN);
	if (current.type == STRING) {
		Predicate::addParameter(Parameter(current.type, current.val));
	}
	match(STRING);
	stringList();
	match(RIGHT_PAREN);
	match(PERIOD);
	dataLogPro.addFact(p);
}

void Parser::stringList() {
	if (current.type == COMMA) {
		match(COMMA);
		if (current.type == STRING) {
			Predicate::addParameter(Parameter(current.type, current.val));
		}
		match(STRING);
		stringList();
	}
	else {
		return;
	}
}


void Parser::factList() {
	if (current.type == ID) {
		fact();
		factList();
	}
	else
		return;
}

//parse rule data
////////////////////////////////////////////////
void Parser::rule() {
	p = Predicate(current.val);
	r = Rule(p);
	headPredicate();

	match(COLON_DASH);
	predicate();
	predicateList();
	match(PERIOD);
	dataLogPro.addRule(r);
}

void Parser::ruleList() {
	if (current.type == ID) {
		rule();
		ruleList();
	}
	else
		return;
}

void Parser::predicate() {
	match(ID);
	match(LEFT_PAREN);
	parameter();
	parameterList();
	match(RIGHT_PAREN);
}

void Parser::predicateList() {
	if (current.type == COMMA) {
		match(COMMA);
		predicate();
		predicateList();
	}
	else
		return;
}

void Parser::parameter() {
	if (current.type == STRING) {
		Predicate::addParameter(Parameter(current.type, current.val));
		match(STRING);
	}
	else if (current.type == ID) {
		Predicate::addParameter(Parameter(current.type, current.val));
		match(ID);
	}
	else if (current.type == LEFT_PAREN) {
		expression();
	}
	else
		return;
}

void Parser::parameterList() {
	if (current.type == COMMA) {
		match(COMMA);
		parameter();
		parameterList();
	}
	else
		return;
}

void Parser:: expression() {
	match(LEFT_PAREN);
	parameter();
	operators();
	parameter();
	match(RIGHT_PAREN);
}

void Parser::operators() {
	if (current.type == ADD) {
		Predicate::addParameter(Parameter(current.type, current.val));
		match(ADD);
	}
	else if (current.type == MULTIPLY) {
		Predicate::addParameter(Parameter(current.type, current.val));
		match(MULTIPLY);
	}
}

//parse query data
/////////////////////////////////////////////////////
void Parser::query() {
	p = Predicate(current.val);
	predicate();
	match(Q_MARK);
	dataLogPro.addQuery(p);
}

void Parser::queryList() {
	if (current.type == ID) {
		query();
		queryList();
	}
	else 
		return;
}



