package model;


import fhtechnikum.robert.application.user.UserProfile;
import fhtechnikum.robert.system.EntitySerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntitySerializerTest {
    @Test
    void testSerializer() {
        EntitySerializer serializer = new EntitySerializer();
        UserProfile userProfile = new UserProfile("Kaneki", "I am a Ghoul", "O_O");

        Assertions.assertEquals("{\r\n  \"Name\" : \"Kaneki\",\r\n  \"Bio\" : \"I am a Ghoul\",\r\n  \"Image\" : \"O_O\"\r\n}", serializer.serialize(userProfile));
    }
}
