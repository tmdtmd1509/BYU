#include "scanner.h"
#include "DatalogProgram.h"

class Parser {
public:

	Parser(vector<Token>& tokens);
	~Parser();

	void parse();
	void match(TokenType expected);
	void scheme();
	void schemeList();
	void headPredicate();
	void idList();
	void predicate();
	void stringList();
	void fact();
	void factList();
	void rule();
	void ruleList();
	void query();
	void queryList();

	DLP datalogProgram();

	void predicateList();
	void parameterList();
	void parameter();
	void expression();
	void operators();
	void exParameter();


	vector<Token> myTok;
	Token current;
	int myIndex;
	Rule r;
	DLP dataLogPro;
	Predicate pred;
	vector<string> expressVec;

};