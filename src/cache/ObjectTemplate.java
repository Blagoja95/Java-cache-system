package cache;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ObjectTemplate implements CRUD, Serializable {
	private final int TARGET = 1, PATH = 2, VALUE = 3; // indexes
	LinkedHashMap<String, ValueStructure> floorMap = new LinkedHashMap<>();
	LinkedHashMap<String, ValueStructure> objectDataPointer = floorMap;

	private void recursiveCreation(LinkedList<String> list, int untilIndex) {

		if (list.size() == untilIndex)
			return;

		String next = list.getFirst();
		ValueStructure tempOb;

		if (objectDataPointer.containsKey(next))
			tempOb = objectDataPointer.get(next);
		else {
			tempOb = new ValueStructure();
			objectDataPointer.put(next, tempOb);
		}

		objectDataPointer = tempOb.valueMap;
		list.removeFirst();
		recursiveCreation(list, untilIndex);
	}

	private void recursiveNavigation(LinkedList<String> list) {

		if (list.size() < 2)
			return;

		ValueStructure tempOb;
		String next = list.getFirst();

		if (objectDataPointer.containsKey(next))
			tempOb = objectDataPointer.get(next);
		else {
			objectDataPointer = floorMap;
			System.out.println("Target doesn't exist!");
			return;
		}

		objectDataPointer = tempOb.valueMap;
		list.removeFirst();
		recursiveNavigation(list);
	}

	private void recursiveRead(LinkedList<String> list, String last) {

		if (list.size() == 0) {
			System.out.println("\"" + last + "\" : {");
			return;
		}

		ValueStructure tempOb;
		String next = list.getFirst();

		if (objectDataPointer.containsKey(next))
			tempOb = objectDataPointer.get(next);
		else {
			objectDataPointer = floorMap;
			System.out.println("Target doesn't exist!");
			return;
		}

		objectDataPointer = tempOb.valueMap;
		list.removeFirst();
		recursiveRead(list, next);
	}

	public void create(String[] commands) {

		if (commands.length < VALUE) {
			System.out.println("Missing arguments!");
			return;
		}

		if (commands[PATH].length() == 0) {
			System.out.println("Missing arguments! Try command help!");
			return;
		}

		var listOfPaths = createListOfPath(commands[PATH]);

		switch (commands[TARGET]) {
			case "object" -> createObject(listOfPaths);
			case "field" -> {

				if (listOfPaths.size() == 1) {
					System.out.println("Missing a value or a name of a field! Try cache create field \"map.fieldName\" ");
					return;
				}

				if (commands.length != VALUE + 1 || commands[VALUE].length() == 0) {
					System.out.println("Missing field value!");
					return;
				}

				String value = commands[VALUE];
				createField(listOfPaths, value);
			}
		}
	}

	private void createObject(LinkedList<String> list) {
		recursiveCreation(list, 0);
		objectDataPointer = floorMap; // reset pointer;
	}

	private void createField(LinkedList<String> listOfPaths, String value) {
		recursiveCreation(listOfPaths, 1);
		String key = listOfPaths.getFirst();

		if (objectDataPointer.containsKey(key)) {
			System.out.println("An object or a filed with the same name already exist!");
			objectDataPointer = floorMap;
			return;
		}

		objectDataPointer.put(key, new ValueStructure(value));
		objectDataPointer = floorMap; // reset pointer
	}

	@Override
	public void read(String[] commands) {

		if (commands.length > 3) {
			System.out.println("Too many arguments");
			return;
		}

		if (commands[1].length() == 0) {
			System.out.println("Path name can't be empty!. Try read all");
			return;
		}

		if (commands[1].equals("all"))
			display();
		else
			display(createListOfPath(commands[1]));
	}

	private void display() {

		if (floorMap.size() == 0) {
			System.out.println("No data! Try create object objectName.");
			return;
		}

		ValueStructure.resetHierarchyLevel();

		for (Map.Entry<String, ValueStructure> i : floorMap.entrySet())
			System.out.print("\"" + i.getKey() + "\" : {\n" + i.getValue());
		System.out.println("}");
	}

	private void display(LinkedList<String> path) {
		ValueStructure.resetHierarchyLevel();

		recursiveRead(path, "");

		for (Map.Entry<String, ValueStructure> i : objectDataPointer.entrySet()) {
			System.out.print(i.getKey() + " : ");
			ValueStructure temp = i.getValue();

			if (temp.getStringValue() != null) {
				System.out.println(temp.getStringValue() + ",");
			} else
				System.out.println("{\n" + i.getValue() + "},");
		}

		System.out.println("}");
		objectDataPointer = floorMap;
	}

	public void update(String[] commands) {

		if (commands.length != 4) {
			System.out.println("Missing arguments!");
			return;
		}

		if (commands[TARGET].length() == 0)
			return;

		String target = commands[TARGET];

		if (commands[PATH].length() == 0)
			return;

		var listOfPaths = createListOfPath(commands[PATH]);

		if (commands[VALUE].length() == 0)
			return;

		String value = commands[VALUE].replace("\"", "");

		switch (target) {
			case "key" -> updateKey(listOfPaths, value);
			case "value" -> {

				if (value.equals("OBJECT"))
					updateValue(listOfPaths, new ValueStructure());
				else
					updateValue(listOfPaths, new ValueStructure(value));
			}
			default -> System.out.println("Wrong target");
		}
	}

	private void updateKey(LinkedList<String> listOfPaths, String keyToUpdate) {
		recursiveNavigation(listOfPaths);

		String key = listOfPaths.getFirst();

		if (objectDataPointer.containsKey(key)) {
			objectDataPointer.put(keyToUpdate, objectDataPointer.get(key));
			objectDataPointer.remove(key);
		} else
			System.out.println("Key not found!");

		objectDataPointer = floorMap;
	}

	private void updateValue(LinkedList<String> listOfPaths, ValueStructure valueToUpdate) {
		recursiveNavigation(listOfPaths);

		String key = listOfPaths.getFirst();

		if (objectDataPointer.containsKey(key))
			objectDataPointer.replace(key, valueToUpdate);
		else
			System.out.println("Target doesn't exit");
		objectDataPointer = floorMap;
	}

	public void delete(String path) {

		if (path.length() == 0)
			return;

		var listOfPaths = createListOfPath(path);
		String targetKeyName = listOfPaths.getLast();

		recursiveNavigation(listOfPaths);

		objectDataPointer.remove(targetKeyName);
		objectDataPointer = floorMap; // reset pointer;
	}

	private LinkedList<String> createListOfPath(String path) {
		return new LinkedList<>(List.of(path
				.replace("\"", "")
				.split("\\W")));
	}
}