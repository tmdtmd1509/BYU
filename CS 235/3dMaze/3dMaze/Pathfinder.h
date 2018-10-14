#ifndef PATHFINDER_H_
#define PATHFINDER_H_


#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <cmath>
#include <sstream>
#include <fstream>
#include <stack>

#include "PathfinderInterface.h"


class Pathfinder:public PathfinderInterface {
private:

	int Cols;
	int Rows;
	int Floors;

public:
	Pathfinder();
	~Pathfinder();

	string toString() const;

	void createRandomMaze();

	bool importMaze(string file_name);

	vector<string> solveMaze();

	bool recurseMaze(int x, int y, int z);

	vector<string> printPath;

	string currentMaze[5][5][5];
	string solveTestMaze[5][5][5];

	bool valid = false;
};
#endif
