package br.com.talks.challengeconference.application;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.talks.challengeconference.constants.TrackManagement;
import br.com.talks.challengeconference.entity.Talk;

public class Conference {
    List<Talk> trackTalk;

    int totalTrackMinutes;
    int countTrack;
    int countTalk;
	
    public Conference(){
        this.trackTalk = new ArrayList<Talk>();
    }

    public int getTotalTrackMinutes() {
        return totalTrackMinutes;
    }

    public void setTotalTrackMinutes(int totalTrackMinutes) {
        this.totalTrackMinutes = totalTrackMinutes;
    }

    public int getCountTrack() {
        return countTrack;
    }

    public void setCountTrack(int countTrack) {
        this.countTrack = countTrack;
    }

    public int getCountTalk() {
        return countTalk;
    }

    public void setCountTalk(int countTalk) {
        this.countTalk = countTalk;
    }

    public List<Talk> getTrackTalk() {
        return trackTalk;
    }
    
    public void setTrackTalk(List<Talk> trackTalk) {
        this.trackTalk = trackTalk;
    }

    // Methods
    public void ProcessTalkInput(String fileName){
        int id = 0;
        int noOfTracks = 0;
        FileInputStream fstream = null;

        try {
            fstream = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        int intMinutes;
        int totalMinutes = 0;

        System.out.println("Input :");
        System.out.println("");

        // Line By Line
        try {
            while ((strLine = br.readLine()) != null) {
                // if (lines start from // then ignore it) else get it.
                if(strLine.contains("//") || strLine.isEmpty())
                    continue;
 
                id = id +1 ;

                System.out.println(strLine);

                String Title = strLine.substring(0, strLine.lastIndexOf(" "));
                String MinutesString = strLine.substring(strLine.lastIndexOf(" ") + 1);
                String Minutes = strLine.replaceAll("\\D+", "");
                if("lightning".equals(MinutesString))
                {
                    intMinutes = 5;

                    totalMinutes = totalMinutes + intMinutes;
                } else {
                    intMinutes = Integer.parseInt(Minutes);
                    totalMinutes = totalMinutes + intMinutes;
                }

                Talk singleTalk = new Talk(intMinutes,Title,id);

                trackTalk.add(singleTalk);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setCountTalk(id);

        this.setTotalTrackMinutes(totalMinutes);

        Double totalMinutesInDouble =  totalMinutes*1.0;

        Double numberOfTrack =  totalMinutesInDouble/TrackManagement.TOTAL_CONFERENCE;

        double fractionalPart = numberOfTrack % 1;
        double integralPart = numberOfTrack - fractionalPart;

        int leftMinutes = totalMinutes - (int)integralPart*TrackManagement.TOTAL_CONFERENCE.intValue();

        // if it comes 1.5 or 1.4 or 1.8 - it will give the value of 2 Tracks
        if (leftMinutes > 0) {
            noOfTracks = (int) integralPart + 1;
        }else
        {
            noOfTracks = (int) integralPart;
        }

        this.setCountTrack(noOfTracks);

        Collections.sort(trackTalk, new Comparation());

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Just to show the empty lines to display the test-input
        System.out.println("");
        System.out.println("");
    }


    public int ScheduleTalksIntoTrack(int trackCountIndex, List<Talk> trackTalks, 
    		int trackCount,int indexTalk , int totalTalkCount){

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);

        int sum180 = TrackManagement.MORNING_TIME;
        int sum240 = TrackManagement.AFTERNOON_TIME;

        int TalkIndex;

        String sessionTime;
        String SessionTitle;

        for(TalkIndex = indexTalk; TalkIndex < totalTalkCount;TalkIndex++) {
            // Get the combination of 180 and fill it
            if (sum180 >= trackTalk.get(TalkIndex).getMinutes()) {
                sum180 = sum180 - trackTalk.get(TalkIndex).getMinutes();
                sessionTime = sdf.format(cal.getTime()) + " " + trackTalk.get(TalkIndex).getTitle() + " " 
                											  + trackTalk.get(TalkIndex).getMinutes() + "min";
                trackTalk.get(TalkIndex).setTitle(sessionTime);
                cal.add(Calendar.MINUTE, trackTalk.get(TalkIndex).getMinutes());
                SessionTitle = "Track" + " " + (trackCountIndex + 1);
                trackTalk.get(TalkIndex).setTrackTitle(SessionTitle);
            }
            
            if (sum180 < trackTalk.get(TalkIndex).getMinutes())
                break;

            if (sum180 > 0)
                continue;

            if (sum180 <= 0)
                break;
        }

        trackTalk.get(TalkIndex).setLunch(true);
        sessionTime = "12:00" + " " + "Lunch";
        trackTalk.get(TalkIndex).setLunchTitle(sessionTime);
        cal.add(Calendar.MINUTE, 60);

        TalkIndex++;

        for(;TalkIndex < totalTalkCount;TalkIndex++) {
            if (sum240 >= trackTalk.get(TalkIndex).getMinutes()) {
                sum240 = sum240 - trackTalk.get(TalkIndex).getMinutes();
                sessionTime = sdf.format(cal.getTime()) + " " + trackTalk.get(TalkIndex).getTitle() + " " + trackTalk.get(TalkIndex).getMinutes() + "min";
                trackTalk.get(TalkIndex).setTitle(sessionTime);
                cal.add(Calendar.MINUTE, trackTalk.get(TalkIndex).getMinutes());
                SessionTitle = "Track" + " " + (trackCountIndex + 1);
                trackTalk.get(TalkIndex).setTrackTitle(SessionTitle);
            }
            if (sum240 < trackTalk.get(TalkIndex).getMinutes())
                break;

            if (sum240 > 0)
                continue;

            if (sum240 <= 0)
                break;
        }

        if(totalTalkCount == (TalkIndex))
            --TalkIndex;
        trackTalk.get(TalkIndex).setNetworkingFlag(true);
        sessionTime = "5:00" + " " + "Networking Event";
        trackTalk.get(TalkIndex).setNetworkingTitle(sessionTime);

        TalkIndex++;
        return TalkIndex;

    }


    public void OutputOfTalksIntoTracks(List<Talk> trackTalk){

        System.out.println("Output :");
        System.out.println("");
        String TrackTitle = "dummyValue";

        for(int trackCountIndex=0;trackCountIndex<trackTalk.size();trackCountIndex++)
        {

            if(!TrackTitle.equals(trackTalk.get(trackCountIndex).getTrackTitle())) {
                System.out.println(trackTalk.get(trackCountIndex).getTrackTitle() + ":");
                System.out.println("");
                TrackTitle = trackTalk.get(trackCountIndex).getTrackTitle();
            }

            System.out.println(trackTalk.get(trackCountIndex).getTitle());

            if(trackTalk.get(trackCountIndex).isLunch()) {
                System.out.println(trackTalk.get(trackCountIndex).getLunchTitle());
            }

            if(trackTalk.get(trackCountIndex).isNetworkingFlag()) {
                System.out.println(trackTalk.get(trackCountIndex).getNetworkingTitle());
                System.out.println("");
                System.out.println("");
            }

        }
    }

}


