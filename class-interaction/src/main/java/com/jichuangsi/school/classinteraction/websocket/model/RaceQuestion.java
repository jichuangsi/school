/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public class RaceQuestion extends AbstractQustionNotifyForStudentModel {

	private String raceId;
	
	public RaceQuestion() {
		this.wsType = AbstractQustionNotifyForStudentModel.WS_TYPE_RACE_QUESTION;
	}

	public String getRaceId() {
		return raceId;
	}

	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

}
