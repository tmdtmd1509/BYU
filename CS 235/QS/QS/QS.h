#pragma once
#include "QSInterface.h"
#include <sstream>

class QS: public QSInterface {

public:
	QS();
	~QS();

	//void swapNum(int one, int two);

	void sort(int left, int right);

	void sortAll();

	int medianOfThree(int left, int right);

	int partition(int left, int right, int pivotIndex);

	string getArray() const;

	int getSize() const;

	bool addToArray(int value);

	bool createArray(int capacity);

	void clear();

};


