package com.cache;

import java.io.*;

public class LocalStorage {
	private String storageLocation;
	private File localFile;

	LocalStorage(String storageLocation) {
		this.storageLocation = storageLocation;
		checkFile(storageLocation);
	}

	public void setStorageLocation(String storageLocation) {

		if (storageLocation.length() == 0) {
			System.err.println("Path name can't be empty!");
			return;
		}
		this.storageLocation = storageLocation;
	}

	public void storeData(ObjectTemplate ob) {

		try (ObjectOutputStream obOut = new ObjectOutputStream(new FileOutputStream(localFile))) {

			obOut.writeObject(ob);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public ObjectTemplate loadData() {

		try (ObjectInputStream inObjectFile = new ObjectInputStream(new FileInputStream(localFile))) {
			return (ObjectTemplate) inObjectFile.readObject();

		} catch (ClassNotFoundException | IOException ex) {
			System.out.println("No entries found in local storage!");
			return new ObjectTemplate();
		}
	}

	private void checkFile(String path) {
		localFile = new File(path);

		if (!localFile.exists())
			try {
				localFile.createNewFile();
			} catch (IOException ex) {
				System.err.println(ex);
			}
	}
}
