import models.Note;
import org.junit.*;

import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.test.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class IntegrationTest {

    private static final int PORT = 3333;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "http://" + HOST + ":" + PORT;
    private static TestServer testServer;
    private static final int TIMEOUT = 30000;

    @BeforeClass
    public static void beforeClass() {
        testServer = testServer(PORT);
        testServer.start();
    }

    @AfterClass
    public static void afterClass() {
        testServer.stop();
    }

    @Test
    public void testGetList() {
        WSResponse response = WS.url(BASE_URI + "/notes").get().get(TIMEOUT);
        assertEquals(200, response.getStatus());
        Note[] notes = getInstanceOfResponse(response, Note[].class);
        assertNotNull(notes);
        assertTrue(notes.length > 0);
    }

    @Test
    public void testGetById() {
        WSResponse response = WS.url(BASE_URI + "/notes/1").get().get(TIMEOUT);
        Note note = getInstanceOfResponse(response, Note.class);
        assertEquals(200, response.getStatus());
        assertNotNull(note);
        assertEquals(1L, note.id.longValue());
    }

    @Test
    public void testGetById404() {
        assertEquals(404, WS.url(BASE_URI + "/notes/9999").get().get(TIMEOUT).getStatus());
    }

    @Test
    public void testCreate() {
        String name = "Ivan Drago";
        WSResponse response = WS.url(BASE_URI + "/notes")
                .setContentType("application/json")
                .post("{\"user\": \"" + name + "\", \"text\": \"I must break you\"}")
                .get(TIMEOUT);
        assertEquals(200, response.getStatus());
        Note note = getInstanceOfResponse(response, Note.class);
        assertNotNull(note);
        assertEquals(name, note.user);
    }

    @Test
    public void testDelete() {
        WSResponse response = WS.url(BASE_URI + "/notes/2").delete().get(TIMEOUT);
        assertEquals(200, response.getStatus());
        Note[] notes = getInstanceOfResponse(response, Note[].class);
        assertNotNull(notes);
        assertTrue(notes.length > 0);
        for(Note note : notes) {
            assertNotEquals(2L, note.id.longValue());
        }
    }

    @Test
    public void testDelete404() {
        assertEquals(404, WS.url(BASE_URI + "/notes/999999").delete().get(TIMEOUT).getStatus());
    }

    private <T> T getInstanceOfResponse(WSResponse response,  Class<T> clazz) {
        return Json.fromJson(Json.parse(response.getBody()), clazz);
    }

}
