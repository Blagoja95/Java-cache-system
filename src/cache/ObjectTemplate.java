package cache;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ObjectTemplate implements CRUD, Serializable {
	private static final String WRONG_MSG = "Wrong arguments!";
	private static final String MISSING_MSG = "Missing arguments!";
	private static final String INVALID_MSG = "Invalid value!";
	private static final int TARGET = 1;
	private static final int PATH = 2;
	private static final int VALUE = 3;
	private static final int MIN_ARG_SIZE_VALUE = 4;

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

	private void recursiveDisplay(LinkedList<String> list, String last) {

		if (list.isEmpty()) {
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
		recursiveDisplay(list, next);
	}

	public void create(String[] commands) {

		if (commands.length < VALUE) {
			System.out.println(MISSING_MSG);
			return;
		}

		if (commands[PATH].length() == 0) {
			System.out.println(MISSING_MSG);
			return;
		}

		var listOfPaths = createListOfPath(commands[PATH]);

		if (listOfPaths.isEmpty()) {
			System.out.println(WRONG_MSG);
			return;
		}

		switch (commands[TARGET]) {
			case "object" -> createObject(listOfPaths);
			case "field" -> {

				if (listOfPaths.size() == 1) {
					System.out.println("Missing a value or a name of a field! Try cache create field \"map.fieldName\" ");
					return;
				}

				if (commands.length < MIN_ARG_SIZE_VALUE || commands[VALUE].length() == 0) {
					System.out.println("Missing field value!");
					return;
				}

				if (commands.length > MIN_ARG_SIZE_VALUE)
					createField(listOfPaths, createSentence(commands));
				else
					createField(listOfPaths, commands[VALUE]);
			}
			default -> System.out.println("Wrong command!");
		}
	}

	private String createSentence (String[] arguments){
		StringBuilder sentence = new StringBuilder();

		for (int i = VALUE; i < arguments.length; i++)
			sentence.append(arguments[i])
					.append(" ");

		return sentence.toString().trim();
	}

	private ValueStructure createValue(String value) {
		ValueStructure newVal;

		try {
			newVal = new ValueStructure(Double.parseDouble(value));
		} catch (NumberFormatException ex) {
			newVal = new ValueStructure(value);
		}

		return newVal;
	}

	private void createObject(LinkedList<String> list) {
		recursiveCreation(list, 0);
		objectDataPointer = floorMap;
	}

	private void createField(LinkedList<String> listOfPaths, String value) {
		recursiveCreation(listOfPaths, 1);
		String key = listOfPaths.getFirst();

		if (objectDataPointer.containsKey(key)) {
			System.out.println("An object or a filed with the same name already exist!");
			objectDataPointer = floorMap;
			return;
		}

		objectDataPointer.put(key, createValue(value));
		objectDataPointer = floorMap; // reset pointer
	}

	@Override
	public void read(String[] commands) {

		if (commands.length > 3) {
			System.out.println("Too many arguments");
			return;
		}

		if (commands[1].isEmpty()) {
			System.out.println("Path name can't be empty!. Try read all");
			return;
		}

		if (commands[1].equals("all"))
			display();
		else
			display(createListOfPath(commands[1]));
	}

	private void display() {

		if (floorMap.isEmpty()) {
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
		recursiveDisplay(path, "");

		for (Map.Entry<String, ValueStructure> i : objectDataPointer.entrySet()) {
			System.out.print(i.getKey() + " : ");
			ValueStructure temp = i.getValue();

			if (temp.getStringValue() != null) {
				System.out.println(temp.getStringValue() + ",");
			}else if (temp.getNumberValue() != null) {
				System.out.println(temp.getNumberValue() + ",");
			} else
				System.out.println("{\n" + i.getValue() + "},");
		}

		System.out.println("}");
		objectDataPointer = floorMap;
	}

	public void update(String[] commands) {

		if (commands.length < MIN_ARG_SIZE_VALUE) {
			System.out.println(MISSING_MSG);
			return;
		}

		if (commands[TARGET].isEmpty())
			return;

		String target = commands[TARGET];

		if (commands[PATH].isEmpty())
			return;

		var listOfPaths = createListOfPath(commands[PATH]);

		if (listOfPaths.isEmpty()) {
			System.out.println(WRONG_MSG);
			return;
		}

		if (commands[VALUE].isEmpty())
			return;

		switch (target) {
			case "key" -> {
				String value = checkValue(commands[VALUE]);

				if (value == null) {
					System.out.println(INVALID_MSG);
					return;
				}

				updateKey(listOfPaths, value);
			}
			case "value" -> {
				String value = commands[VALUE];

				if (value.equals("OBJECT")){
					updateValue(listOfPaths, new ValueStructure());
					return;
				}

				if (commands.length > MIN_ARG_SIZE_VALUE)
					updateValue(listOfPaths, createValue(createSentence(commands)));
				else
					updateValue(listOfPaths, createValue(value));
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
		if (path.isEmpty())
			return;

		var listOfPaths = createListOfPath(path);

		if (listOfPaths.isEmpty()) {
			System.out.println(WRONG_MSG);
			return;
		}

		String targetKeyName = listOfPaths.getLast();
		recursiveNavigation(listOfPaths);
		objectDataPointer.remove(targetKeyName);
		objectDataPointer = floorMap;
	}

	private LinkedList<String> createListOfPath(String path) {
		LinkedList<String> newList = new LinkedList<>(List.of(path
				.replace("\"", "")
				.split("\\W")));

		if (newList.isEmpty())
			return new LinkedList<>();

		return newList;
	}

	public String checkValue(String input) {

		if (input.isEmpty())
			return null;

		if (!input.matches("^[a-zA-Z0-9]*$"))
			return null;

		return input;
	}
}