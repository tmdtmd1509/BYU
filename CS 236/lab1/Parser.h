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

	void predicateList();
	void parameterList();
	void parameter();
	void expression();
	void operators();


	vector<Token> myTok;
	Token current;
	int myIndex;
	Predicate p;
	Rule r;
	DLP dataLogPro;
};