#include "scanner.h"

scanner::scanner(string file): in(file) {
}

vector<Token> scanner::scan() {
	char myC = 0;
	vector<Token>tokens;

	while (myC != EOF) {
		myC = in.getNextChar();
                if (true == in.isSpace(myC));

		else if (myC == EOF) {
                        tokens.push_back(Token(EndOfFile, "", in.lineNum));
			break;
                }
                else {
                        switch (myC) {
			case ',':
				tokens.push_back(Token(COMMA, ",", in.lineNum));
				break;

			case '.':
				tokens.push_back(Token(PERIOD, ".", in.lineNum));
				break;

			case '?':
				tokens.push_back(Token(Q_MARK, "?", in.lineNum));
				break;

			case '(':
				tokens.push_back(Token(LEFT_PAREN, "(", in.lineNum));
				break;

			case ')':
				tokens.push_back(Token(RIGHT_PAREN, ")", in.lineNum));
				break;

			case ':':
				if (in.peekNextChar() == '-') {
					in.getNextChar();
					tokens.push_back(Token(COLON_DASH, ":-", in.lineNum));
				}
				else {
					tokens.push_back(Token(COLON, ":", in.lineNum));
				}
				break;

			case '*':
				tokens.push_back(Token(MULTIPLY, "*", in.lineNum));
				break;

			case '+':
				tokens.push_back(Token(ADD, "+", in.lineNum));
				break;

			case '\'':
				tokens.push_back(StringChecker());
				break;

			case '#':
				tokens.push_back(CommentChecker());
				break;

			default:
                                defaultHandler(myC, tokens);
                        }
                }
	}
	return tokens;
}

void scanner::defaultHandler(char myC, vector<Token> &tokens) {
    string value = "";
    value += myC;
    if (isalpha(myC)) {
            while (isalpha(in.peekNextChar()) || isalnum(in.peekNextChar())) {
                    myC = in.getNextChar();
                    value += myC;
                    //cout << "add c" << endl;
            }
            //myC = in.getNextChar();
            //cout << "working 0" << endl;
            tokens.push_back(Token(CheckID(value)));
    }
    else if (isalnum(myC)) {
            tokens.push_back(Token(UNDEFINED, value, in.lineNum));
    }
    else {
            //cout << "working 2" << endl;
            tokens.push_back(Token(UNDEFINED, value, in.lineNum));
    }
}

vector<string>keywordsVec { "Schemes", "Facts", "Rules", "Queries" };
vector<TokenType>keywords {SCHEMES, FACTS, RULES, QUERIES};

int scanner::CheckKeywords(string word) {
	for (int i = 0; i < 4; i++) {
		if (word == keywordsVec[i]) return i;
	}
	return 4;
}

Token scanner::CheckID(string value) {
	if (CheckKeywords(value) == 4) {
		return Token(ID, value, in.lineNum);
	}
	else {
		TokenType KeyWord = keywords[CheckKeywords(value)];
		return Token(KeyWord, value, in.lineNum);
	}
}

Token scanner::StringChecker(){
	string value = "\'";
	char c;
        int extraLine = 0;
	while (true) {
		c = in.getNextChar();
		//cout << "add s" << c << endl;
		//cin >> t;
                if (c == '\'' && in.peekNextChar() == '\'') {
                    value += c;
                    c = in.getNextChar();
                    value += c;
                }
                else if (c == '\'' && in.peekNextChar() != '\'') {
                        value += c;
                        return Token(STRING, value, in.lineNum-extraLine);
		}
		else if (c == EOF) {
			//cout << "working 4" << endl;
                        return Token(UNDEFINED, value, in.lineNum-extraLine);
		}
		else if (c == '\n') {
			in.lineNum++;
                        extraLine++;
                        value += c;
		}
		else {
			value += c;
		}
	}
}

Token scanner::CommentChecker(){
	char c;
	c = in.getNextChar();
        if (c == '|') {
            return doubleComment(c);
	}

        return singleComment(c);
}
Token scanner::doubleComment(char c) {
//cout<< "double commnet?!"<<endl;
    string value = "#";
    int extraLine = 0;
    value += c;
    c = in.getNextChar();
    while (true) {
            if (c == '|') {
                //cout << value << "working" << endl;
                if (in.peekNextChar() == '#') {
                        value += c;
                        c = in.getNextChar();
                        value += c;
                        return Token(COMMENT, value, in.lineNum-extraLine);
                }
                value += c;
                c = in.getNextChar();
            }
            else if (c == EOF) {
                    //cout << "working 5" << value <<endl;
                    return Token(UNDEFINED, value, in.lineNum-extraLine);
            }
            else if (c == '\n') {
                    in.lineNum++;
                    extraLine++;
                    value += c;
                    c = in.getNextChar();
            }
            else {
                value += c;
                //cout << value << "else is working" <<endl;
                c = in.getNextChar();
            }
    }
}

Token scanner::singleComment(char c) {
    string value = "#";
    while (true) {
            //cout << "add cm2" << endl;
            if (c == EOF) {
                    //cout << "break" << endl;
                return Token(COMMENT, value, in.lineNum);
            }
            else if (c == '\n') {
                    in.lineNum++;
                    return Token(COMMENT, value, in.lineNum-1);
                    //cout << "endline" << endl;
            }
            value += c;
            c = in.getNextChar();
    }
}
