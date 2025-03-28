package com.ampersand.groom.global.annotation.usecase;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@UseCase
@Transactional(readOnly = true, rollbackFor = Exception.class)
public @interface UseCaseWithReadOnlyTransaction {
}