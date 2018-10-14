#pragma once
#include "Fighter.h"

class Robot: public Fighter{
protected:
  int currentEnergy;
  int maxEnergy;
  int bonus_damage;

public:
  Robot(string name, int maxHP, int currentHp, int strength, int speed, int magic);
  int getDamage();
  void reset();
  bool useAbility();
};
