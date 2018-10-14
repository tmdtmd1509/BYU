#include "Parser.h"
#include "token.h"
#include <sstream>

Parser::Parser(vector<Token>& Tokens): myTok(Tokens), current(myTok[0]) {
	myIndex = 0;
	current = myTok[myIndex];
}

Parser::~Parser() {
}

void Parser::parse() {
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
}

//match
////////////////////////////////////////////
void Parser::match(TokenType expected) {
	if (current.type == expected) {
		++myIndex;
		current = myTok[myIndex];
	}
	else {
		throw string(current.printToken());
		return;
	}
}

//parse scheme data
//////////////////////////////////////////////
void Parser::scheme(){
	string s = current.val;
	headPredicate();
	dataLogPro.addScheme(s, pred.paramList);
	pred.paramList.clear();
}

void Parser::headPredicate() {
	match(ID);
	match(LEFT_PAREN);
	if (current.type == ID) {
		pred.addParameter(Parameter(current.type, current.val));
	}
	match(ID);
	idList();
	match(RIGHT_PAREN);
}

void Parser::idList() {
	if (current.type == COMMA) {
		match(COMMA);
		if (current.type == ID) {
			pred.addParameter(Parameter(current.type, current.val));
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
	string s = current.val;
	match(ID);
	match(LEFT_PAREN);
	if (current.type == STRING) {
		pred.addParameter(Parameter(current.type, current.val));
	}
	match(STRING);
	stringList();
	match(RIGHT_PAREN);
	match(PERIOD);
	dataLogPro.addFact(s, pred.paramList);
	dataLogPro.addDomain(pred.paramList);
	pred.paramList.clear();
}

void Parser::stringList() {
	if (current.type == COMMA) {
		match(COMMA);
		if (current.type == STRING) {
			pred.addParameter(Parameter(current.type, current.val));
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
	string s = current.val;
	headPredicate();
	Predicate p = Predicate(s, pred.paramList);
	pred.paramList.clear();
	match(COLON_DASH);

	string id = current.val;

	predicate();

	r.addPredicate(id, pred.paramList);
	pred.paramList.clear();

	predicateList();
	match(PERIOD);

	dataLogPro.addRule(p, r.predicateList);
	r.predicateList.clear();
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
		string id = current.val;
		predicate();

		r.addPredicate(id, pred.paramList);
		pred.paramList.clear();

		predicateList();
	}
	else
		return;
}

void Parser::parameter() {
	if (current.type == STRING) {
		pred.addParameter(Parameter(current.type, current.val));
		vector<Parameter> domainVec;
		domainVec.push_back(Parameter(current.type, current.val));
		//dataLogPro.addDomain(domainVec);
		match(STRING);
	}
	else if (current.type == ID) {
		pred.addParameter(Parameter(current.type, current.val));
		match(ID);
	}
	else if (current.type == LEFT_PAREN) {
		expressVec.push_back(current.val);
		expression();
		stringstream ss;
		int size = expressVec.size();
		for (int i = 0; i < size; ++i) {
			ss << expressVec[i];
		}
		pred.addParameter(Parameter(STRING, ss.str()));
		expressVec.clear();
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

void Parser::exParameter() {
	if (current.type == STRING) {
		expressVec.push_back(current.val);
		match(STRING);
	}
	else if (current.type == ID) {
		expressVec.push_back(current.val);
		match(ID);
	}
	else if (current.type == LEFT_PAREN) {
		expressVec.push_back(current.val);
		expression();
	}
	else
		return;
}

void Parser::expression() {
	match(LEFT_PAREN);
	exParameter();
	operators();
	exParameter();
	if (current.type == RIGHT_PAREN) {
		expressVec.push_back(current.val);
	}
	match(RIGHT_PAREN);
}

void Parser::operators() {
	if (current.type == ADD) {
		expressVec.push_back(current.val);
		match(ADD);
	}
	else if (current.type == MULTIPLY) {
		expressVec.push_back(current.val);
		match(MULTIPLY);
	}
	else {
		throw string(current.printToken());
	}

}

//parse query data
/////////////////////////////////////////////////////
void Parser::query() {
	string s = current.val;
	predicate();
	match(Q_MARK);
	dataLogPro.addQuery(s, pred.paramList);
	pred.paramList.clear();
}

void Parser::queryList() {
	if (current.type == EndOfFile) {
	}
	else {
		if (current.type == ID) {
			query();
			queryList();
		}
		else if (current.type != EndOfFile)
			throw string(current.printToken());
	}
}

DLP Parser::datalogProgram() {
	return dataLogPro;
}

