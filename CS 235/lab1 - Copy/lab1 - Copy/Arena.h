#ifndef ARENA_HPP_
#define ARENA_HPP_


#include <string>
#include <vector>
#include "ArenaInterface.h"
#include "Fighter.h"
#include "Archer.h"
#include "Robot.h"
#include "Cleric.h"

/*
*	WARNING: It is expressly forbidden to modify any part of this document, including its name
*/
class Arena : public ArenaInterface {
private:
  vector <FighterInterface*> fighters;

public:
	bool addFighter(string info);

	bool removeFighter(string name);

	FighterInterface* getFighter(string name);

	int getSize() const;
};

#endif
