#include <string>
#include <vector>
#include <list>

#include "print.h"

using namespace std;

/*
vector<string> reverse(vector<string> items) {
    int i;
    int elfin;
    string tmp;
    
    for (i = 0; i < items.size() / 2; i++) {
        elfin = items.size() - 1 - i;
    
        tmp = items[i];
        items[i] = items[elfin];
        items[elfin] = tmp;
    }
    
    
    return items;
}
*/
/*
vector<string> reverse(vector<string> items) {
    int i;
    string tmp;
    
    for (vector<string>::iterator iters = items.begin(); iters != items.end(); iters++) {
        //for (i = 0; i < items.size(); i++) {
        tmp = *iters;//items[i];
        items.erase(iters);//items.begin()+i);
        items.insert(items.begin(), tmp);
    }

    return items;
}
*/

list<string> reverse(list<string> items) {
    int i;
    string tmp;
    
    for (list<string>::iterator iters = items.begin(); iters != items.end(); iters++) {
        //cout << "Beginning: " << *iters << endl;
    
        //for (i = 0; i < items.size(); i++) {
        tmp = *iters;//items[i];
        items.erase(iters);//items.begin()+i);
        items.insert(items.begin(), tmp);

        //cout << "After the insert: " << *iters << endl;
    }

    return items;
}

int main() {
    
    list<string> names;
    //vector<string> names;
    
    names.push_back("Mary");
    names.push_back("Jane");
    names.push_back("Fear");
    names.push_back("Faith");
    names.push_back("Mark");
    names.push_back("John");
    
    
    print(names);
    
    print(reverse(names));
    
    return 0;
}


