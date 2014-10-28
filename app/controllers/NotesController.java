package controllers;


import models.Note;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotesController extends Controller {
    private static Map<Long, Note> db = new HashMap<Long, Note>();
    static {
        db.put(1L, new Note(1L, "John", "Hello from John"));
        db.put(2L, new Note(2L, "Bob", "Hello from Bob"));
    }
    public static Result list() {
        return ok(Json.toJson(db.values()));
    }

    public static Result get(Long id) {
        if (!db.containsKey(id))
            return notFound("The note with this id does not exist");
        return ok(Json.toJson(db.get(id)));
    }

    public static Result create() {
        Note note = Form.form(Note.class).bindFromRequest().get();
        note.created = new Date();
        note.id = System.currentTimeMillis();
        db.put(note.id, note);
        return redirect(controllers.routes.NotesController.get(note.id));
    }

    public static Result delete(Long id) {
        if (!db.containsKey(id))
            return notFound("The note with this id does not exist");
        db.remove(id);
        return redirect(controllers.routes.NotesController.list());
    }
}
