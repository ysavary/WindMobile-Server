package ch.windmobile.server.jdc;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import ch.windmobile.server.ITTestDataSource;

@ContextConfiguration(locations = { "applicationContext.xml" })
@Test
public class ITTestJdcDataSource extends ITTestDataSource {
}