package com.theastrologist.domain;

import com.google.gson.annotations.JsonAdapter;
import org.joda.time.DateTime;

/**
 * Created by Samy on 20/05/2017.
 */
public class DateTimeJson {
	@JsonAdapter(DateTimeJSONAdapter.class)
	private DateTime date;

	public DateTimeJson(DateTime dateTime) {
		this.date = dateTime;
	}
}
