#include "Pathfinder.h"

#include <sstream>



Pathfinder::Pathfinder() {
	int Cols = 5;
	int Rows = 5;
	int Floors = 5;
}
Pathfinder::~Pathfinder() {}

string Pathfinder::toString() const{
	int x = 0;
	int y = 0;
	int z = 0;
	string mz;

	if(valid) {
		//cout << "working!" << endl;
		for (z = 0; z<5; z++) {
			for (y = 0; y<5; y++) {
				for (x = 0; x<5; x++) {
					if (x == 4) {
						mz += currentMaze[x][y][z];
					}
					else {
						mz += currentMaze[x][y][z];
						mz += " ";
					}
				}
				mz += "\n";
			}
			if (z != 4) {
				mz += "\n";
			}
		}
	}
	else {
		//cout << "not valid" << endl;
		for (int z = 0; z < 5; z++) {
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					if (x == 4) {
						mz += "1";
					}
					else {
						mz += "1";
						mz += " ";
					}
				}
				mz += "\n";
			}
			if (z != 4) {
				mz += "\n";
			}
		}
	}
	return mz;
}



void Pathfinder::createRandomMaze() {
	int one_count = 0;
	int zero_count = 0;
	int variance = 5;

	for (int z = 0; z < 5; z++) {
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 5; x++) {
				if ((z == 0 && y == 0 && x == 0) || (z == 4 && y == 4 && x == 4)) {
					currentMaze[x][y][z] = "1";
				}
				else {
					int random = rand() % 2;
					//make equal
					if (abs(one_count - zero_count) > variance) {
						if (one_count > zero_count) {
							random = 0;
						}
						else {
							random = 1;
						}
					}
					if (random == 1) ++one_count;
					else ++zero_count;

					currentMaze[x][y][z] = to_string(random);
				}
			}
		}
	}
	valid = true;
}

bool Pathfinder::importMaze(string file_name) {
	string line;
	int count = 0;
	char temp;
	
	ifstream file(file_name);
	if (file.is_open()){
		while (file >> temp){
			if (temp == '1'){
				count++;
			}
			else if (temp == '0'){
				count++;
			}
			else{
				return false;
			}

		}
		file.close();
	}

	if (count != 125) {
		//cout << "count error" << count << endl;
		return false;
	}
	//cout << count << endl;
	file.open(file_name);
	if (file.is_open()) {
		for (int k = 0; k < 5; k++) {
			for (int j = 0; j < 5; j++) {
				for (int i = 0; i < 5; i++) {
					file >> temp;
					currentMaze[i][j][k] = temp;
					//cout << currentMaze[i][j][k] << " ";

					if (currentMaze[4][4][4] == "0" || currentMaze[0][0][0] == "0") {
						//cout << "start end error" << endl;

						return false;
					}
				}
			}
		}
		file.close();
	}
	else{
		//cout << "else error" << endl;
		return false;
	}
	//cout << "it is valid" << endl;
	valid = true;
	return true;
}

vector<string> Pathfinder::solveMaze() {
	printPath.clear();
	for (int k = 0; k < 5; k++) {
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < 5; i++) {
				solveTestMaze[k][j][i] = currentMaze[k][j][i];
			}
		}
	}
	recurseMaze(0, 0, 0);
	cout << printPath.size() << endl;
	return printPath;
}

bool Pathfinder::recurseMaze(int x, int y, int z) {
	if (x < 0 || y < 0 || z < 0) {
		return false;
	}
	if (x > 4 || y > 4 || z > 4) {
		return false;
	}
	if (solveTestMaze[x][y][z] != "1") {
		return false;
	}
	if (x == 4 && y == 4 && z == 4) {
		string cordinate;
		//string test = "444!!!";
		cordinate = "(" + to_string(x) + ", " + to_string(y) + ", " + to_string(z) + ")";
		printPath.insert(printPath.begin(), cordinate);
		return true;
	}

	solveTestMaze[x][y][z] = "2";

	if (recurseMaze(x - 1, y, z) || recurseMaze(x + 1, y, z) || recurseMaze(x, y - 1, z) || recurseMaze(x, y + 1, z) || recurseMaze(x, y, z - 1) || recurseMaze(x, y, z + 1)) {
		string cordinate;
		cordinate = "(" + to_string(x) + ", " + to_string(y) + ", " + to_string(z) + ")";
		printPath.insert(printPath.begin(), cordinate);

		return true;
	}
}
