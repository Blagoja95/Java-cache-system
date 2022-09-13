# Java cache system 
 - Key/Value cache
 - On close, save to file
 - On start, read from file

~  create object "planet"
~  create field  "planet.name" "Earth"
~  create object  "planet.continents"
~  create object "planet.continents" "europe"
~  create field "planet.continents.europe.name" "Europe"

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

Other CRUD actions implement on your own but follow `cache create` pattern

eg. `cache read "planet.earth"` returns JSON formatted output of branch

Advanced/Bonus:
- Implement advanced search via cli ('>' '==' '%')
