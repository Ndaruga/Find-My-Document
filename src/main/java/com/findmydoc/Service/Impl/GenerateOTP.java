package com.findmydoc.Service.Impl;

import java.util.Random;

public class GenerateOTP {
    public static int generateOTP(int length) {
        String numbers = "0123456789";
        Random rand = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i<length; i++) {
            int index = rand.nextInt(numbers.length());
            otp.append(numbers.charAt(index));
        }
        return Integer.parseInt(otp.toString());
    }
}
