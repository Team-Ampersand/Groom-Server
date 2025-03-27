package com.ampersand.groom.domain.auth.application.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class GenerationAuthCode {
    public String generateAuthCode() {
        return String.valueOf(10000000 + new Random().nextInt(90000000));
    }
}