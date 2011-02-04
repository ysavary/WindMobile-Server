package ch.windmobile.server.windline;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import ch.windmobile.server.TestDataSource;

@ContextConfiguration(locations = { "applicationContext.xml" })
@Test
public class TestWindlineDataSource extends TestDataSource {
}
