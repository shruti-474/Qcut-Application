package com.mountreachsolution.qcutadmin;

public class User {
    private String id;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String password;
    private String image;
    private String userrole;

    public User(String id, String name, String mobile, String email, String address,
                String password, String image, String userrole) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.password = password;
        this.image = image;
        this.userrole = userrole;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }
    public String getImage() { return image; }
    public String getUserrole() { return userrole; }
}
