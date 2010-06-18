package com.derbysoft.jazure.sdk.connector.mina.roomrate;


public class RoomRateServiceStub implements RoomRateService {

	private final String dswitchUrl;

	public RoomRateServiceStub(String dswitchUrl) {
		this.dswitchUrl = dswitchUrl;
	}

	@Override
	public AvailabilityRS getAvailability(AvailabilityRQ availabilityRQ) {
		return new AvailabilityRS();
	}

}
