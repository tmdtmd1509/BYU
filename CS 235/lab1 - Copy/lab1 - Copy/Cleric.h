#pragma once
#include "Fighter.h"

class Cleric: public Fighter{
protected:
  int currentMana;
  int maxMana;

public:
  Cleric(string name, int maxHP, int currentHp, int strength, int speed, int magic);
  int getDamage();
  void reset();
  void regenerate();
  bool useAbility();
};
