package models;

import java.util.Date;

public class Note {
    public Note() {}
    public Note(Long id, String user, String text) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.created = new Date();
    }

    public Long id;
    public String user;
    public String text;
    public Date created;
}
