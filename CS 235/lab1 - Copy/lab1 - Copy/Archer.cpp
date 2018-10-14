#include "Archer.h"

Archer::Archer(string name, int maxHP, int currentHp, int strength, int speed, int magic):Fighter(name, maxHP, currentHp, strength,speed, magic) {
  currentSpeed = speed;
}

  int Archer::getDamage() {
    return speed;
  }

  void Archer::reset() {
    currentHp = maxHP;
    speed = currentSpeed;
  }

  bool Archer::useAbility() {
    speed = speed + 1;
    return true;
  }
