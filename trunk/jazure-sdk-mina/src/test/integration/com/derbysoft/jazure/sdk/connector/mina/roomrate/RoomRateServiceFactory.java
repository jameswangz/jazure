package com.derbysoft.jazure.sdk.connector.mina.roomrate;

public class RoomRateServiceFactory {

	private final String dswitchUrl;

	public RoomRateServiceFactory(String dswitchUrl) {
		this.dswitchUrl = dswitchUrl;
	}

	public RoomRateService getService() {
		return new RoomRateServiceStub(dswitchUrl);
	}

}
