package ch.windmobile.server.socialmodel.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ch.windmobile.server.socialmodel.AuthenticationService;

public class SimpleAuthenticationServiceTest {

	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testInvalidToken() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate(null,null);
	}
		
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testInvalidUser() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate("userC", null);
	}
	
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testInvalidPassword() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate("userA", "bad");
	}
	
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testNullPassword() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate("userA", null);
	}
	
	public void testValidLogin() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		String sessionId = authenticationService.authenticate("userA", "pwdA");
		Assert.assertNotNull( sessionId );
	}
}
