# Java cache system
Recursive JSON like cash system made in JAVA where user can do all CRUD operations in his terminal.

## Functionality
 - Key/Value cache
 - On close, save to file
 - On start, read from file


<pre>
~  create object "planet"
~  create field  "planet.name" "Earth"
~  create object  "planet.continents"
~  create object "planet.continents" "europe"
~  create field "planet.continents.europe.name" "Europe"
</pre>
<pre>
planet = {
      name: "Earth",
      continents: {
            europe: {
                  name: 'Europe',
                  countries: {
                        {
                              name: 'Serbia",
                              population: 6908000
                        }
                  }
            }
      }
}
</pre>

Other CRUD actions are implement by following `cache create` pattern

eg. `cache read "planet.earth"` returns JSON formatted output of branch

## CLI MENU

Welcome to the java cache system

Available commands:

1. create object object1.object2
   create field object1.field value

2. read all // display all branches of data
   rad ob1.ob2 // read from ob2 all data

3. delete object1.object2.deleteTarget

4. update value "object1.updateTarget" newValue
   update value object1.updateTarget OBJECT // to update it with a new object
   update key object1.object2.keyToUpdate newKeyValue

**All entries are serialized and then saved localy in *json.ser* file.**

## How to try it out yourself

1. Clone this repository
2. By using Java supported IDE (Eclipse, InteliJ, ...) or by using some CLI build and run this project
3. If successful Java cash system grating and menu will appear in your terminal