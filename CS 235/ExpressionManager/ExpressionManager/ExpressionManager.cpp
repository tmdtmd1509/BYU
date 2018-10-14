#include "ExpressionManager.h"
#include <cstring>
#include <cctype>
#include <cstdlib>


ExpressionManager::ExpressionManager() {}
ExpressionManager::~ExpressionManager() {}

int precedence(string tmp) {
	if (tmp == ")" || tmp == "}" || tmp == "]") {
		return 3;
	}
	else if (tmp == "*" || tmp == "/" || tmp == "%") {
		return 2;
	}
	else if (tmp == "+" || tmp == "-") {
		return 1;
	}
	else {
		return 0;
	}
}


bool checkNumberOfThings(string exp){
stringstream ss(exp);
string curr;
string operators = "+-*%/";
int numberNum = 0;
int operatorNum = 0;

while(ss >> exp){
	if (strspn(exp.c_str(), "0123456789") == exp.size()){
		++numberNum;
	}
	else if(operators.find(exp) != string::npos){
		++operatorNum;
	}
}

if ((numberNum - operatorNum) == 1){
	return true;
}
else{
	return false;
	}
}


bool ExpressionManager::isBalanced(string expression) {
	stack <string> myStack;
	stringstream ss(expression);
	string exp;
	int open = 0;
	int close = 0;

	while (ss >> exp) {
		if (exp == "(" || exp == "{" || exp == "[") {
			myStack.push(exp);
			++open;
		}
		else if (exp == ")" || exp == "}" || exp == "]") {
			if (myStack.empty()) {
				return false;
			}
			else {
				if (((myStack.top() == "(") && (exp == ")"))
					|| ((myStack.top() == "{") && (exp == "}"))
					|| ((myStack.top() == "[") && (exp == "]"))) {
					myStack.pop();
					++close;
				}
				else {
					return false;
				}
			}
		}
	}
	if (open != close) {
		return false;
	}
	else {
		return true;
	}
}

string ExpressionManager::postfixToInfix(string postfixExpression) {
	stack <string> myStack1;
	stringstream ss(postfixExpression);
	string exp;
	string operators = "+-*/%";
	string rightNum, leftNum;
	string infixExp;

	//stack <int>

	if (!isBalanced(postfixExpression)) {
		return "invalid";
	}
	if (!checkNumberOfThings(postfixExpression)) {
		return "invalid";
	}
	while (ss >> exp) {
		if (exp == "NULL") {
			return "invaid";
		}
		else if (strspn(exp.c_str(), "0123456789") == exp.size()) {
			myStack1.push(exp);
		}
		else if (operators.find(exp) != string::npos) {
			if (myStack1.empty()) {
				return "invalid";
			}
			else {
				rightNum = myStack1.top();
				myStack1.pop();
				if (myStack1.empty()) {
					return "invalid";
				}
				leftNum = myStack1.top();
				myStack1.pop();
				infixExp = "( " + leftNum + " " + exp + " " + rightNum + " )";
				myStack1.push(infixExp);
			}
		}
		else {
			return "invalid";
		}
	}
	if (myStack1.empty()) {
		return "invalid";
	}
	return myStack1.top();
}



//need to evaluate
//int leftOperant
//int rightOperant

string ExpressionManager::postfixEvaluate(string postfixExpression) {
	stack <string> myStack3;
	stringstream ss(postfixExpression);
	string exp;
	string operators = "+-*/%";
	string rightNum;
	string leftNum;
	int result = 0;


	while (ss >> exp) {
		if (strspn(exp.c_str(), "0123456789") == exp.size()) {
			myStack3.push(exp);
		}
		else if (operators.find(exp) != string::npos) {
			if (myStack3.empty()) {
				return "invalid";
			}
			rightNum = myStack3.top();
			myStack3.pop();

			if (myStack3.empty()) {
				return "invalid";
			}
			leftNum = myStack3.top();
			myStack3.pop();

			int right = atoi(rightNum.c_str());
			int left = atoi(leftNum.c_str());
l			if (exp == "+") {
				result = left + right;
			}
			if (exp == "-") {
				result = left - right;
			}
			if (exp == "*") {
				result = left * right;
				cout << result << endl;
			}
			if (exp == "%") {
				result = left % right;
			}
			if (exp == "/") {
				if (right == 0) {
					return "invalid";
				}
				else {
					result = left / right;
				}
			}
			stringstream os;
			os << result;
			myStack3.push(os.str());
		}
		else {
			return "invalid";
		}
	}
	return myStack3.top();
}

string ExpressionManager::infixToPostfix(string infixExpression) {
	stack <string> myStack2;
	stringstream ss(infixExpression);
	stringstream os;
	string exp;
	string operators = "+-*/%";


	if (!isBalanced(infixExpression)) {
		return "invalid";
	}
	if (!checkNumberOfThings(infixExpression)) {
		return "invalid";
	}
	while (ss >> exp) {
		if (strspn(exp.c_str(), "0123456789") == exp.size()) {
			os << exp << " ";
		}
		else if (operators.find(exp) != string::npos) {
			if (myStack2.empty()) {
				myStack2.push(exp);
			}
			else if (myStack2.top() == "(" || myStack2.top() == "{" || myStack2.top() == "[" || myStack2.top() == ")" || myStack2.top() == "}" || myStack2.top() == "]") {
				myStack2.push(exp);
			}
			else if (precedence(exp) > precedence(myStack2.top())) {
				myStack2.push(exp);
			}
			else {
				while (!myStack2.empty() && (precedence(exp)) <= precedence(myStack2.top())) {
					os << myStack2.top() << " ";
					myStack2.pop();
				}
				myStack2.push(exp);
			}
		}
		else if (exp == "(" || exp == "{" || exp == "[") {
			myStack2.push(exp);
		}
		else if (exp == ")" || exp == "}" || exp == "]") {
			while (!((myStack2.top() == "(") && (exp == ")"))
				|| ((myStack2.top() == "{") && (exp == "}"))
				|| ((myStack2.top() == "[") && (exp == "]"))) {
				os << myStack2.top() << " ";
				myStack2.pop();
			}
			myStack2.pop();
		}
		else {
			return "invalid";
		}
	}
	while (!myStack2.empty()) {
		os << myStack2.top() << " ";
		myStack2.pop();
	}
	string myString = os.str();
	string myString2 = myString.substr(0, myString.size() - 1);
	return myString2;
}



