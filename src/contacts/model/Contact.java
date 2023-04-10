package contacts.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact {
    protected static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+)?(\\w+([- ])?)?(\\(\\w+\\)([- ])?|(\\w{2,})?([- ])?)?((\\w{2,}[- ])?(\\w{2,})?)+$");

    protected String phone = "";
    protected LocalDateTime creationDate;
    protected LocalDateTime modificationDate;

    protected Contact() {
    }

    public static boolean checkPhone(String phone) {
        Matcher phoneMatcher = PHONE_PATTERN.matcher(phone);
        return !phone.isEmpty() && phoneMatcher.matches();
    }

    public abstract String getFullName();

    public boolean setPhone(String phone) {
        if (checkPhone(phone)) {
            this.phone = phone;
            return true;
        } else {
            this.phone = "";
            System.out.println("Wrong number format!");
            return false;
        }
    }

    public List<String> getFieldNames() {
        List<String> fieldNames = new ArrayList<>();

        Class<?> currentClass = this.getClass();
        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers()) && !field.getName().equalsIgnoreCase("creationDate") && !field.getName().equalsIgnoreCase("modificationDate")) {
                    fieldNames.add(field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return fieldNames;
    }

    public boolean hasPhone() {
        return !phone.isEmpty();
    }

    public String getPhone() {
        return phone.isBlank() ? "[no number]" : phone;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "Number: " + getPhone() +
                "\nTime created: " + this.creationDate.withSecond(0).withNano(0) +
                "\nTime last edit: " + this.modificationDate.withSecond(0).withNano(0);
    }
}
