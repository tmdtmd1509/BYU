#ifndef FIGHTER_HPP_
#define FIGHTER_HPP_

#include <iostream>
#include <string>
#include <vector>
#include "FighterInterface.h"

using namespace std;


class Fighter: public FighterInterface
{
protected:
	string name;
	int maxHP;
	int currentHp;
	int strength;
	int speed;
	int magic;

public:
	Fighter() {}
	Fighter(string name, int maxHP, int currentHp, int strngth, int speed, int magic);
	virtual ~Fighter() {}


	string getName() const;

	int getMaximumHP() const;

	int getCurrentHP() const;

	int getStrength() const;

	int getSpeed() const;

	int getMagic() const;

	int getDamage();

	void takeDamage(int damage);

	void reset();

	void regenerate();

	bool useAbility();

};

#endif
