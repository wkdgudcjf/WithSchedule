package com.example.withschedule.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FriendList implements Serializable
{	
	private static final long serialVersionUID = 2523439256799563621L;
	private ArrayList<Friend> list;

	public FriendList(ArrayList<Friend> list) {
		this.list = list;
	}
	
	public FriendList(){
		list = new ArrayList<Friend>();
	}

	public ArrayList<Friend> getList() {
		return list;
	}

	public void setList(ArrayList<Friend> list) {
		this.list = list;
	}

	public void add(Friend friend) {
		// TODO Auto-generated method stub
		this.list.add(friend);
	}
	
	public void remove(Friend friend)
	{
		this.list.remove(friend);
	}
	
	@Override
	public String toString() {
		return "FriendList [list=" + list + "]";
	}	
	
}
