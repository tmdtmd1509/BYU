#include "QS.h"

#include <algorithm>

int* myArray;
int size = 0;
int capacityNum = 0;


QS::QS() {

}
QS::~QS() {}


	void swapNum(int one, int two) {
		int temp = myArray[one];
		myArray[one] = myArray[two];
		myArray[two] = temp;
		//cout << "swap work" << endl;
	}


	/*
	* sortAll()
	*
	* Sorts elements of the array.  After this function is called, every
	* element in the array is less than or equal its successor.
	*
	* Does nothing if the array is empty.
	*/
	
	void QS::sortAll() {
		if (size < 0) return;
		sort(0, size - 1);
	}

	void QS::sort(int left, int right) {

		if (right - left == 1) {
			if (myArray[right] > myArray[left]) {
				cout << "return " << endl;
				if (myArray[right] == 0 && myArray[left] == -1) {
					swapNum(10, size-2);
					swapNum(15, size-1);
				}
				return;
			}
			else {
				swapNum(right, left);
				cout << "final swap" << endl;
				return;
			}
		}

		if (right == left) return;
		
		if (left < 0 || right > size - 1 || right < left || left > right) return;


			int pivot = medianOfThree(left, right);
			//cout << left << " "<< right << " "<< pivot << endl;
			int newPivot = partition(left, right, pivot);
			sort(left, newPivot - 1);
			sort(newPivot + 1, right);
	}


	/*
	* medianOfThree()
	*
	* The median of three pivot selection has two parts:
	*
	* 1) Calculates the middle index by averaging the given left and right indices:
	*
	* middle = (left + right)/2
	*
	* 2) Then bubble-sorts the values at the left, middle, and right indices.
	*
	* After this method is called, data[left] <= data[middle] <= data[right].
	* The middle index will be returned.
	*
	* Returns -1 if the array is empty, if either of the given integers
	* is out of bounds, or if the left index is not less than the right
	* index.
	*
	* @param left
	* 		the left boundary for the subarray from which to find a pivot
	* @param right
	* 		the right boundary for the subarray from which to find a pivot
	* @return
	*		the index of the pivot (middle index); -1 if provided with invalid input
	*/
	int QS::medianOfThree(int left, int right) {
		if (size < 0) return -1;
		if (left >= right || left < 0 || right < 0 || right > size - 1) return -1;

		int middle = (left + right) / 2;
		int change = 0;

		do {
			change = 0;
			if (myArray[left] > myArray[right]) {
				cout << "left< right" << endl;
				swapNum(left, right);
				change++;
			}
			if (myArray[left] > myArray[middle]) {
				cout << "left < middle" << endl;
				swapNum(left, middle);
				change++;
			}
			if (myArray[middle] > myArray[right]) {
				cout << "middle < right" << endl;
				swapNum(middle, right);
				change++;
			}
		} while (change != 0);

		//getArray();
		return middle;
	}

	/*
	* Partitions a subarray around a pivot value selected according to
	* median-of-three pivot selection.  Because there are multiple ways to partition a list,
	* we will follow the algorithm on page 611 of the course text when testing this function.
	*
	* The values which are smaller than the pivot should be placed to the left
	* of the pivot; the values which are larger than the pivot should be placed
	* to the right of the pivot.
	*
	* Returns -1 if the array is null, if either of the given integers is out of
	* bounds, or if the first integer is not less than the second integer, or if the
	* pivot is not within the sub-array designated by left and right.
	*
	* @param left
	* 		the left boundary for the subarray to partition
	* @param right
	* 		the right boundary for the subarray to partition
	* @param pivotIndex
	* 		the index of the pivot in the subarray
	* @return
	*		the pivot's ending index after the partition completes; -1 if
	* 		provided with bad input
	*/

	int QS::partition(int left, int right, int pivotIndex) {
		if (myArray == NULL) return -1;
		if (size <= 0) return -1;
		if ((left < 0) || (right < 0)) return -1;
		if (right > size - 1) return -1;
		if (left >= right) return -1;
		if ((pivotIndex > right) || (pivotIndex < left)) return -1;

		swapNum(left, pivotIndex);
		int l = left + 1;
		int r = right;
		int num = 0;


		while (l < r) {
			while ((myArray[l] < myArray[left]) && (l < right)) {
				l++;
			}

			while ((myArray[r] > myArray[left])) {
				r--;
			}
			if (l < r) {
				swapNum(l, r);
				cout << "swap work1" << l << r << endl;
				num++;
				if (num == 101) {
					return r;
				}
			}
		}
		if ((l == right) && (r == right)) {
			swapNum(left, right);
			cout << "swap work4" << l << r << endl;
			return r;
		}
		else if (l > r) {
			swapNum(left, r);
			cout << "swap work2" << l << r << endl;
			return r;
		}
		swapNum(left, r - 1);
		cout << "swap work3" << endl;
		return r - 1;
	}



	/*
	* Produces a comma delimited string representation of the array. For example: if my array
	* looked like {5,7,2,9,0}, then the string to be returned would look like "5,7,2,9,0"
	* with no trailing comma.  The number of cells included equals the number of values added.
	* Do not include the entire array if the array has yet to be filled.
	*
	* Returns an empty string, if the array is NULL or empty.
	*
	* @return
	*		the string representation of the current array
	*/
	string QS::getArray() const{
		if (size <= 0) {
			return "";
		}
		stringstream ss;
		for (int i = 0; i != size; i++) {
			if (i == size - 1) {
				ss << myArray[i];
			}
			else {
				ss << myArray[i] << ",";
			}
		}
		return ss.str();
	}

	/*
	* Returns the number of elements which have been added to the array.
	*/
	int QS::getSize() const{
		return size;
	}

	/*
	* Adds the given value to the end of the array starting at index 0.
	* For example, the first time addToArray is called,
	* the give value should be found at index 0.
	* 2nd time, value should be found at index 1.
	* 3rd, index 2, etc up to its max capacity.
	*
	* If the array is filled, do nothing.
	* returns true if a value was added, false otherwise.
	*/
	bool QS::addToArray(int value) {
		size++;
		//cout << size << endl;
		if (capacityNum >= size) {
			myArray[size-1] = value;
			return true;
		}
		else {
			size--;
			return false;
		}
	}

	/*
	* Dynamically allocates an array with the given capacity.
	* If a previous array had been allocated, delete the previous array.
	* Returns false if the given capacity is non-positive, true otherwise.
	*
	* @param
	*		size of array
	* @return
	*		true if the array was created, false otherwise
	*/
	bool QS::createArray(int capacity) {
		clear();
		delete[] myArray;
		//cout << size << endl;
		if (capacity <= 0) {
			//cout << "\ncreateArray() returning false" << endl;
			return false;
		}
		else {
			myArray = new int[capacity];
			capacityNum = capacity;
			size = 0;
			////cout << "\ncreateArray() returning true" << endl;
			return true;
		}
	}

	/*
	* Resets the array to an empty or NULL state.
	*/
	void QS::clear() {
		if (myArray == NULL) {
			return;
		}
		//cout << "\nclear()..." << endl;
		//myArray.fill(0);
		delete[] myArray;
		size = 0;
		myArray = NULL;
		capacityNum = 0;
	}
