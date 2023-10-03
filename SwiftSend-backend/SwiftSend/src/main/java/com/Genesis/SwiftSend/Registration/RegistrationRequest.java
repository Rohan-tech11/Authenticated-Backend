package com.Genesis.SwiftSend.Registration;

public record RegistrationRequest(String firstName, String lastName,
		String email, String password, long mobileNumber) {
}