#pragma once
#include "Fighter.h"

class Archer: public Fighter {
protected:
  int currentSpeed;
  int bonus_speed;

public:
  Archer(string name, int maxHP, int currentHp, int strength, int speed, int magic);
  int getDamage();
  void reset();
  bool useAbility();
};
