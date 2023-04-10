package contacts.model;

import java.time.LocalDateTime;

public class OrganizationContact extends Contact {
    private String organizationName;
    private String address;

    public OrganizationContact() {
        super();
    }

    public OrganizationContact(
            String organizationName,
            String address,
            String phone,
            LocalDateTime creationDate,
            LocalDateTime modificationDate
    ) {
        this.organizationName = organizationName;
        this.address = address;
        setPhone(phone);
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getFullName() {
        return this.organizationName;
    }

    @Override
    public String toString() {
        return "Organization name: " + this.organizationName +
                "\nAddress: " + this.address +
                "\n" + super.toString();
    }

    public static class Builder {
        private String organizationName;
        private String address;
        private String phone = "";
        private LocalDateTime creationDate;
        private LocalDateTime modificationDate;

        public Builder setOrganizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
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

        public OrganizationContact build() {
            return new OrganizationContact(
                    this.organizationName,
                    this.address,
                    this.phone,
                    this.creationDate,
                    this.modificationDate
            );
        }
    }
}
