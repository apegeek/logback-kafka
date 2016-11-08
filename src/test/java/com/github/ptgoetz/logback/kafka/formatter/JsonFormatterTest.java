package com.github.ptgoetz.logback.kafka.formatter;

import org.junit.Test;

import static junit.framework.Assert.*;

import junit.framework.TestCase;

public class JsonFormatterTest extends TestCase {

    @Test
    public void testJsonFormat() {
        String nonJsonMessage = "{\"level\":\"INFO\",\"logger\":\"test\",\"message\":\"foobar\",\"timestamp\":1370918376296}";
        String jsonMessage = "{\"level\":\"INFO\",\"logger\":\"test\",\"message\":{\"foo\":\"bar\"},\"timestamp\":1370918376296}";
        String nonJsonMessageWithGroupProperties = "{\"level\":\"INFO\",\"logger\":\"test\",\"message\":\"foobar\",\"name2\":\"value2\",\"name1\":\"value1\",\"timestamp\":1370918376296}";

        // non-JSON
        MockLoggingEvent event = new MockLoggingEvent(false);
        JsonFormatter formatter = new JsonFormatter();
        formatter.setExpectJsonMessage(false);
        String json = formatter.format(event);
        assertEquals(nonJsonMessage, json);

        // JSON
        event = new MockLoggingEvent(true);
        formatter.setExpectJsonMessage(true);
        json = formatter.format(event);
        assertEquals(jsonMessage, json);

        // Extra Properties
        event = new MockLoggingEvent(false);
        formatter.setExpectJsonMessage(false);
        formatter.setExtraProperties("name1=value1\nname2=value2");
        json = formatter.format(event);
        assertEquals(nonJsonMessageWithGroupProperties, json);
    }
}
