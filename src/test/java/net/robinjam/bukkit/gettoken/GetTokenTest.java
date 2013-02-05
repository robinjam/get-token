package net.robinjam.bukkit.gettoken;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class GetTokenTest {
	
	@Test
	public void testGetToken() throws NoSuchAlgorithmException {
		String salt = "60605fba88917c65aa6a848c525acbd48f1cd4526f6c52f0bc135e5a3383683aed80f83767a84e01ebec11772a1dfd7fc6cf6f1b400346363969e66acfc75a41";
		assertEquals("W2Nj4za", GetToken.getToken("foo", salt));
		assertEquals("61I7FP6", GetToken.getToken("bar", salt));
	}
	
}
