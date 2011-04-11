package ch.windmobile.server.windline;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import ch.windmobile.server.ITTestDataSource;

@ContextConfiguration(locations = { "applicationContext.xml" })
@Test
public class ITTestWindlineDataSource extends ITTestDataSource {
}
