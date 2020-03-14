package gr.stratego.patrastournament.me.Models;

import java.util.ArrayList;
import java.util.HashMap;

import gr.stratego.patrastournament.me.Utils.StringUtils;

public class User {

    private String mail;
    private String name;
    private String surname;
    private String pin;
    private HashMap<String, Battle> prevGames;

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPin() {
        return pin;
    }

    public HashMap<String, Battle> getPrevGames() {
        return prevGames;
    }

    public String getDisplayName(){

        if(StringUtils.isNotNullOrEmpty(surname) && StringUtils.isNotNullOrEmpty(name)) {
            return surname.concat(", ").concat(name.substring(0,1));
        } else if(StringUtils.isNotNullOrEmpty(surname) && StringUtils.isNullOrEmpty(name)) {
            return surname;
        } else if(StringUtils.isNullOrEmpty(surname) && StringUtils.isNotNullOrEmpty(name)) {
            return name;
        } else {
            return "";
        }
    }

    public String getFullName(){

        if(StringUtils.isNotNullOrEmpty(surname) && StringUtils.isNotNullOrEmpty(name)) {
            return surname.concat(" ").concat(name);
        } else if(StringUtils.isNotNullOrEmpty(surname) && StringUtils.isNullOrEmpty(name)) {
            return surname;
        } else if(StringUtils.isNullOrEmpty(surname) && StringUtils.isNotNullOrEmpty(name)) {
            return name;
        } else {
            return "";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((mail == null) ? 0 : mail.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User ) obj;

        return StringUtils.areEqual(mail, other.mail);

    }

}
