package br.com.talks.challengeconference.main;

import br.com.talks.challengeconference.application.Conference;
import br.com.talks.challengeconference.constants.TrackManagement;

// @author Hiago Forte, 20181116

public class ChallengeMain {

    public static void main(String[] args) {

        Conference conference = new Conference();

        conference.ProcessTalkInput(TrackManagement.INPUT_FILE);

        int numberOfTracks = conference.getCountTrack();

        int indexTalk = 0;

        for (int trackCount = 0; trackCount <numberOfTracks; trackCount++) {
            indexTalk = conference.ScheduleTalksIntoTrack(trackCount, 
            			conference.getTrackTalk(), conference.getCountTrack(), 
            			indexTalk, conference.getCountTalk());
        }

        conference.OutputOfTalksIntoTracks(conference.getTrackTalk());
    }
    
}
