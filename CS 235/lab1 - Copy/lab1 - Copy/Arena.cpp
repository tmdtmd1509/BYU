#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include "Arena.h"

bool Arena::addFighter(string info) {
    string name;
    string type;
    int currentHp, maxHP, strength, speed, magic;

    istringstream mystream(info);
    mystream >> name;
    mystream >> type;
    mystream >> maxHP;
    mystream >> strength;
    mystream >> speed;
    mystream >> magic;

    if (maxHP <= 0){
      return false;
    }

    if(strength <= 0){
    return false;
  }

    if(speed <= 0){
      return false;
    }

    if(magic <= 0){
      return false;
    }

    for (int i = 0; i < fighters.size(); i++){
      string newName;
      newName = fighters[i]->getName();
      if (newName == name){
        return false;
      }
    }

      if (mystream.fail()){
        return false;
      }

      if (!mystream.eof()){
        return false;
      }

	  currentHp = maxHP;

      if (type == "A"){
        Archer* temp = new Archer(name, maxHP, currentHp, strength, speed, magic);
        fighters.push_back(temp);
        return true;
      }

      if (type == "R"){
        Robot* temp = new Robot(name, maxHP, currentHp, strength, speed, magic);
        fighters.push_back(temp);
        return true;
      }

      if (type == "C"){
        Cleric* temp = new Cleric(name, maxHP, currentHp, strength, speed, magic);
        fighters.push_back(temp);
        return true;
      }

    return false;
  }

	/*
	*	removeFighter(string)
	*
	*	Removes the fighter whose name is equal to the given name.  Does nothing if
	*	no fighter is found with the given name.
	*
	*	Return true if a fighter is removed; false otherwise.
	*/
	bool Arena::removeFighter(string name) {
		for (int i = 0; i < fighters.size(); i++) {
			string newName;
			newName = fighters[i]->getName();
			if (newName == name) {
				fighters.erase(fighters.begin() + i);
				return true;
			}
		}
    return false;
  }

	/*
	*	getFighter(string)
	*
	*	Returns the memory address of a fighter whose name is equal to the given
	*	name.  Returns NULL if no fighter is found with the given name.
	*
	*	Return a memory address if a fighter is found; NULL otherwise.
	*/
	FighterInterface* Arena::getFighter(string name) {
    for (int i = 0; i < fighters.size(); i++) {
		string newName;
		newName = fighters[i]->getName();
      if(newName == name) {
        return fighters[i];
      }
    }
  }

	/*
	*	getSize()
	*
	*	Returns the number of fighters in the arena.
	*
	*	Return a non-negative integer.
	*/
	int Arena::getSize() const{
    int fightersNum;
    fightersNum = fighters.size();
    return fightersNum;
  }
