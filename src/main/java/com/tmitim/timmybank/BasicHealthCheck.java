package com.tmitim.timmybank;
import com.codahale.metrics.health.HealthCheck;

public class BasicHealthCheck extends HealthCheck {
	@Override
	protected Result check() throws Exception {
		return Result.healthy("hello world!");
	}
}