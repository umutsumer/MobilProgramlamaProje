package com.yildiz.flatsearchapp;

public class RegistrationDB {
    public String name,surname,email,password,dateStarted,dateEnd;

    public RegistrationDB(String name, String surname, String email, String password, String dateStarted, String dateEnd) {

        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.dateStarted = dateStarted;
        this.dateEnd = dateEnd;
    }
}
