package net.robinjam.bukkit.gettoken;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class GetTokenTest {
	
	@Test
	public void testGetToken() throws NoSuchAlgorithmException {
		assertEquals("7d73c", GetToken.getToken("robinjam", "NaCl"));
	}
	
}
