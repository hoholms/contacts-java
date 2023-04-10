package contacts.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PhoneBook {

    private final List<Contact> contacts;

    public PhoneBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public int countContacts() {
        return contacts.size();
    }

    public String listContacts() {
        StringBuilder contactsString = new StringBuilder();

        for (int i = 0; i < contacts.size(); i++) {
            contactsString
                    .append((i + 1))
                    .append(". ")
                    .append(contacts.get(i).getFullName())
                    .append("\n");
        }

        return contactsString.toString();
    }

    public List<Contact> searchContacts(String query) {
        List<Contact> searchResults = new ArrayList<>();
        query = query.toLowerCase();

        for (Contact contact : contacts) {
            boolean matchFound = false;

            Class<?> currentClass = contact.getClass();
            while (currentClass != null) {
                for (Field field : currentClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        Object fieldValue = field.get(contact);
                        if (fieldValue != null && fieldValue.toString().toLowerCase().contains(query)) {
                            matchFound = true;
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if (matchFound) {
                    break;
                }
                currentClass = currentClass.getSuperclass();
            }

            if (matchFound) {
                searchResults.add(contact);
            }
        }

        return searchResults;
    }

    public Contact getContact(int id) {
        return contacts.get(id);
    }
}
