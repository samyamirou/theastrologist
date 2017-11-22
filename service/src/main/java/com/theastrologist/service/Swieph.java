package com.theastrologist.service;

import org.springframework.stereotype.Service;
import swisseph.SwissEph;

/**
 * Created by Samy on 12/05/2017.
 */
@Service
public class Swieph {

	private final SwissEph sw;

	/**
	 * Constructeur privé
	 */
	public Swieph() {
		sw = new SwissEph();
	}

	public SwissEph value() {
		return sw;
	}
}
