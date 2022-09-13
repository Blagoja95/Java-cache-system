package cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cache {
	static final int MIN_ARGUMENTS = 2;
	static final int MAX_ARGUMENTS = 5;
	static final String HELP_MSG = """
			Available commands:
				
			1. create object object1.object2
			   create field  object1.field value
			2. read all // display all branches of data
			   rad ob1.ob2 // read from ob2 all data
			3. delete object1.object2.deleteTarget
			4. update value "object1.updateTarget" newValue
			   update value object1.updateTarget OBJECT // to update it with a new object
			   update key object1.object2.keyToUpdate newKeyValue""";

	public static void main(String[] args) {
		System.out.println("Welcome to the java cache system \n");
		System.out.println(HELP_MSG);

		LocalStorage io = new LocalStorage("json.ser");

		ObjectTemplate cache = io.loadData();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				String input = br.readLine();

				if (input.length() == 0) {
					System.out.println("Command can't be empty! Try: create object \"planet.name\"");
					continue;
				}

				if (input.equals("q") || input.equals("quit") || input.equals("exit"))
					break;

				if (input.equals("help")) {
					System.out.println(HELP_MSG);
					continue;
				}

				String[] commands = input.split(" ");

				if (commands.length >= MIN_ARGUMENTS && commands.length < MAX_ARGUMENTS)
					switch (commands[0]) {
						case "create" -> cache.create(commands);
						case "read" -> cache.read(commands);
						case "delete" -> cache.delete(commands[1]);
						case "update" -> cache.update(commands);
						default -> System.out.println("Wrong command! Try help");
					}
				else
					System.out.println("Wrong command. Try command help");
			}
		} catch (IOException ex) {
			System.out.println(ex);
			ex.printStackTrace();
		}
		io.storeData(cache);
	}
}