package clueGame;

public class Solution {
	public String person;
	public String room;					//private instance variables for solution data
	public String weapon;
	
	public Solution(String p, String r, String w) {					//parameterized constructor to set solution data when new obj created
		this.person = p;
		this.room = r;
		this.weapon = w;
	}
	
	@Override
	public boolean equals(Object aObj) {					//equals method that tests cardName and cardType for equality for this and taken in obj
		if(aObj == this) {
			return true;
		}
		if(!(aObj instanceof Solution)) {
			return false;
		}
		
		Solution c = (Solution)aObj;
		
		return this.person.equals(c.person) && this.room.equals(c.room) && this.weapon.equals(c.weapon);
	}
}
