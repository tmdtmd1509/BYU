#include "Robot.h"
#include <cmath>

Robot::Robot(string name, int maxHP, int currentHp, int strength, int speed, int magic):Fighter(name, maxHP, currentHp, strength, speed, magic) {
  currentEnergy = 2.0 * magic;
  maxEnergy = 2.0 * magic;
  bonus_damage = 0;
}

  int Robot::getDamage() {
    int damage;
    damage = strength + bonus_damage;
	bonus_damage = 0;
    return damage;
  }

  void Robot::reset() {
    currentHp = maxHP;
    bonus_damage = 0;
	currentEnergy = maxEnergy;
  }

  bool Robot::useAbility() {
    if(currentEnergy >= ROBOT_ABILITY_COST) {
      bonus_damage = (double)strength * pow(((double)currentEnergy / maxEnergy), 4);
      currentEnergy = currentEnergy - ROBOT_ABILITY_COST;
      return true;
    }
    else {
      return false;
    }
  }
