package com.github.ptgoetz.logback.kafka.formatter;

import org.junit.Test;

import static junit.framework.Assert.*;
import junit.framework.TestCase;

public class JsonFormatterTest extends TestCase {

    @Test
    public void testJsonFormat() {
        String nonJsonMessage = "{\"level\":\"INFO\",\"logger\":\"test\",\"timestamp\":1370918376296,\"message\":\"foobar\"}";
      String jsonMessage =    "{\"level\":\"INFO\",\"logger\":\"test\",\"timestamp\":1370918376296,\"message\":{\"foo\":\"bar\"}}";
      String nonJsonMessageWithGroupProperties =    "{\"level\":\"INFO\",\"logger\":\"test\",\"timestamp\":1370918376296,\"message\":\"foobar\",\"name2\":\"value2\",\"name1\":\"value1\"}";

      // non-JSON
      MockLoggingEvent event = new MockLoggingEvent(false);
      JsonFormatter formatter = new JsonFormatter();
      formatter.setExpectJson(false);
      String json = formatter.format(event);
      assertEquals(nonJsonMessage, json);

      // JSON
      event = new MockLoggingEvent(true);
      formatter.setExpectJson(true);
      json = formatter.format(event);
      assertEquals(jsonMessage, json);

      // Group Properties
      event = new MockLoggingEvent(false);
      formatter.setExpectJson(false);
      formatter.setGroupProperties("name1=value1\nname2=value2");
      json = formatter.format(event);
      assertEquals(nonJsonMessageWithGroupProperties, json);
    }
}
