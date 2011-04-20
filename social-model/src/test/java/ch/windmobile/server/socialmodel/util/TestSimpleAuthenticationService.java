package ch.windmobile.server.socialmodel.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.AuthenticationToken;


public class TestSimpleAuthenticationService {

	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testInvalidToken() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate(new AuthenticationToken() {			
			@Override
			public boolean isAuthenticate() {
				return false;
			}
			
			@Override
			public String getUserPseudo() {
				return null;
			}
			
			@Override
			public String getTokenIdentifier() {
				return null;
			}
		});
	}
		
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testInvalidUser() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate(new BasicAuthenticationToken("userC", null));
	}
	
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testInvalidPassword() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate(new BasicAuthenticationToken("userA", "bad"));
	}
	
	@Test(expected=AuthenticationService.AuthenticationServiceException.class)
	public void testNullPassword() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		authenticationService.authenticate(new BasicAuthenticationToken("userA", null));
	}
	
	public void testValidLogin() throws Exception {
		Map<String,Object> upMap  =new HashMap<String,Object>();
		upMap.put("userA", "pwdA");
		upMap.put("userB", "pwdB");
		AuthenticationService authenticationService = new SimpleAuthenticationService(upMap);
		AuthenticationToken result = authenticationService.authenticate(new BasicAuthenticationToken("userA", "pwdA"));
		Assert.assertNotNull( result );
		Assert.assertTrue( result instanceof BasicAuthenticationToken);
		Assert.assertNotNull( result.getTokenIdentifier() );
		Assert.assertEquals("userA", result.getUserPseudo() );
		Assert.assertTrue( result.isAuthenticate() );
	}
}
