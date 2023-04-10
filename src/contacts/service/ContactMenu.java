package contacts.service;

import contacts.model.Contact;
import contacts.model.OrganizationContact;
import contacts.model.PersonContact;
import contacts.model.PhoneBook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ContactMenu {

    private final PhoneBook phoneBook;
    private final Scanner scanner;

    public ContactMenu() {
        this.phoneBook = new PhoneBook();
        this.scanner = new Scanner(System.in);
    }

    public void startMenu() {
        String action;
        do {
            System.out.print("\n[menu] Enter action (add, list, search, count, exit): ");
            action = scanner.nextLine();

            switch (action) {
                case "add" -> addContact();
                case "list" -> listContacts();
                case "count" -> System.out.println("The Phone Book has " + phoneBook.countContacts() + " records.");
                case "search" -> searchContacts();
                case "exit" -> System.out.println("Goodbye!");
                default -> System.out.println("There is no action \"" + action + "\"");
            }
        } while (!"exit".equals(action));

        scanner.close();
    }

    private void listContacts() {
        if (phoneBook.countContacts() == 0) {
            System.out.println("No contacts found!");
            return;
        }

        System.out.print(phoneBook.listContacts());

        System.out.print("\n[list] Enter action ([number], back): ");
        String action = scanner.nextLine();

        if (action.equalsIgnoreCase("back")) {
        } else {
            try {
                int index = Integer.parseInt(action) - 1;
                if (index >= 0 && index < phoneBook.countContacts()) {
                    infoContact(phoneBook.getContact(index));
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private void searchContacts() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();
        List<Contact> results = phoneBook.searchContacts(query);

        System.out.println("Found " + results.size() + " results:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getFullName());
        }

        System.out.print("\n[search] Enter action ([number], back, again): ");
        String action = scanner.nextLine();

        if ("back".equalsIgnoreCase(action)) {
        } else if ("again".equalsIgnoreCase(action)) {
            searchContacts();
        } else {
            try {
                int index = Integer.parseInt(action) - 1;
                if (index >= 0 && index < results.size()) {
                    infoContact(results.get(index));
                } else {
                    System.out.println("Invalid selection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private void infoContact(Contact contact) {
        System.out.println(contact);

        String action = null;
        System.out.print("\n[record] Enter action (edit, delete, menu): ");
        action = scanner.nextLine().toLowerCase();
        switch (action) {
            case "edit" -> {
                editContact(contact);
                infoContact(contact);
            }
            case "delete" -> {
                phoneBook.removeContact(contact);
                System.out.println("The record removed!");
            }
            case "menu" -> {
            }
            default -> {
                System.out.println("There is no action \"" + action + "\"");
                infoContact(contact);
            }
        }
    }

    private void addContact() {
        System.out.print("Enter the type (person, organization): ");
        String contactType = scanner.nextLine();
        Contact contact = null;

        if (contactType.equalsIgnoreCase("person")) {
            contact = createContact(PersonContact.class);
        } else if (contactType.equalsIgnoreCase("organization")) {
            contact = createContact(OrganizationContact.class);
        }

        if (contact != null) {
            phoneBook.addContact(contact);
            System.out.println("The record added.");
        } else {
            System.out.println("Invalid contact type.");
        }
    }

    private <T extends Contact> T createContact(Class<T> contactClass) {
        T contact = null;

        try {
            contact = contactClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        List<String> fieldNames = contact.getFieldNames();
        for (String fieldName : fieldNames) {
            String setterName = "set" + capitalizeFirstLetter(fieldName);

            Class<?> currentClass = contactClass;
            Method method = null;
            while (currentClass != null && method == null) {
                try {
                    method = currentClass.getDeclaredMethod(setterName, String.class);
                } catch (NoSuchMethodException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }

            if (method != null) {
                System.out.print("Enter " + fieldName + ": ");
                String userInput = scanner.nextLine();
                try {
                    method.invoke(contact, userInput);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        contact.setCreationDate(LocalDateTime.now());
        contact.setModificationDate(LocalDateTime.now());

        return contact;
    }


    private void editContact(Contact contactToEdit) {
        System.out.print("Select a field (");
        List<String> fields = contactToEdit.getFieldNames();
        for (int i = 0; i < fields.size(); ++i) {
            System.out.print(fields.get(i));
            if (i < fields.size() - 1) {
                System.out.print(", ");
            } else {
                System.out.print("): ");
            }
        }

        String selectedField = scanner.nextLine();

        try {
            Method setter = contactToEdit.getClass().getMethod("set" + capitalizeFirstLetter(selectedField), String.class);

            System.out.print("Enter " + selectedField + ": ");
            String newValue = scanner.nextLine();

            setter.invoke(contactToEdit, newValue);

            contactToEdit.setModificationDate(LocalDateTime.now());

            System.out.println("Saved");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Invalid field or error occurred: " + e.getMessage());
        }
    }

    private String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}