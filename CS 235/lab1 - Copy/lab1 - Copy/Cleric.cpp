#include "Cleric.h"

Cleric::Cleric(string name, int maxHP, int currentHp, int strength, int speed, int magic):Fighter(name, maxHP, currentHp, strength,speed, magic) {
  currentMana = magic * 5;
  maxMana = magic * 5;
}

  int Cleric::getDamage() {
    return magic;
  }

  void Cleric::reset() {
    currentHp = maxHP;
    currentMana = maxMana;
  }

  void Cleric::regenerate() {
    if ((strength / 6) < 1) {
      currentHp = currentHp + 1;
    }
    else {
      currentHp = currentHp + (strength / 6);
    }
    if (currentHp > maxHP) {
      currentHp = maxHP;
    }



    if ((magic / 5) < 1) {
      currentMana = currentMana + 1;
    }
    else {
      currentMana = currentMana + (magic / 5);
    }
    if (currentMana > maxMana) {
      currentMana = maxMana;
    }
  }

  bool Cleric::useAbility() {
    if (currentMana >= CLERIC_ABILITY_COST) {
      if((magic / 3) < 1) {
        currentHp = currentHp + 1;
      }
      else {
        currentHp = (currentHp + (magic / 3));
      }

      currentMana = (currentMana - CLERIC_ABILITY_COST);

      if(currentHp > maxHP) {
        currentHp = maxHP;
      }
      return true;
    }
    else {
      return false;
    }
  }
