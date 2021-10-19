package com.example.withschedule;

import java.util.Comparator;

import com.example.withschedule.friend.Friend;

public class FriendComparator implements Comparator<Friend> {

	@Override
	public int compare(Friend a, Friend b) {
		if(a.getNickname().compareTo(b.getNickname())>=0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

}
