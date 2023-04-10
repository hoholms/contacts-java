package contacts.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class PersonContact extends Contact {

    private String name;
    private String surname;
    private Character gender;
    private LocalDate birthDate;

    public PersonContact() {
        super();
    }

    public PersonContact(
            String name,
            String surname,
            String phone,
            Character gender,
            LocalDate birthDate,
            LocalDateTime creationDate,
            LocalDateTime modificationDate
    ) {
        this.name = name;
        this.surname = surname;
        setPhone(phone);
        this.gender = gender;
        this.birthDate = birthDate;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    private static boolean checkGender(Character gender) {
        return gender == 'M' || gender == 'F';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean hasBirthDate() {
        return this.birthDate != null;
    }

    public boolean hasGender() {
        return this.gender != null && (this.gender == 'M' || this.gender == 'F');
    }

    public String getGender() {
        return hasGender() ? this.gender.toString() : "[no data]";
    }

    public String getBirthDate() {
        return hasBirthDate() ? this.birthDate.toString() : "[no data]";
    }

    public boolean setGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            return false;
        }

        if (checkGender(gender.charAt(0))) {
            this.gender = gender.charAt(0);
            return true;
        } else {
            this.gender = null;
            System.out.println("Bad gender!");
            return false;
        }
    }

    public boolean setBirthDate(String birthDate) {
        try {
            this.birthDate = LocalDate.parse(birthDate);
            return true;
        } catch (DateTimeParseException ex) {
            this.birthDate = null;
            System.out.println("Bad birth date!");
            return false;
        }
    }

    @Override
    public String getFullName() {
        return this.name + " " + this.surname;
    }

    @Override
    public String toString() {
        return "Name: " + this.name +
                "\nSurname: " + this.surname +
                "\nBirth date: " + getBirthDate() +
                "\nGender: " + getGender() +
                "\n" + super.toString();
    }

    public static class Builder {
        private String name;
        private String surname;
        private String phone = "";
        private Character gender;
        private LocalDate birthDate;
        private LocalDateTime creationDate;
        private LocalDateTime modificationDate;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setPhone(String phone) {
            if (checkPhone(phone)) {
                this.phone = phone;
            } else {
                System.out.println("Wrong number format!");
                this.phone = "";
            }
            return this;
        }

        public Builder setCreationDate(LocalDateTime dateTime) {
            this.creationDate = dateTime;
            return this;
        }

        public Builder setModificationDate(LocalDateTime dateTime) {
            this.modificationDate = dateTime;
            return this;
        }

        public Builder setGender(Character gender) {
            if (checkGender(gender)) {
                this.gender = gender;
            } else {
                System.out.println("Bad gender!");
                this.gender = null;
            }
            return this;
        }

        public Builder setBirthDate(String birthday) {
            try {
                this.birthDate = LocalDate.parse(birthday);
            } catch (DateTimeParseException ex) {
                System.out.println("Bad birth date!");
            }

            return this;
        }

        public PersonContact build() {
            return new PersonContact(
                    this.name,
                    this.surname,
                    this.phone,
                    this.gender,
                    this.birthDate,
                    this.creationDate,
                    this.modificationDate
            );
        }
    }
}
