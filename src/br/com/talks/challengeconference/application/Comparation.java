package br.com.talks.challengeconference.application;

import java.util.Comparator;

import br.com.talks.challengeconference.entity.Talk;

/**
 * sort the talks in descending order based on the talktime
 */
public class Comparation implements Comparator<Talk>{

    @Override
    public int compare(Talk t1, Talk t2) {
        if(t1.getMinutes() < t2.getMinutes()){
            return 1;
        } else {
            return -1;
        }
    }
}