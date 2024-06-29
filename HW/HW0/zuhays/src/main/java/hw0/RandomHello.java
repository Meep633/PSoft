package hw0;
import java.util.Random;

/**
* RandomHello selects a random greeting to display to the user.
*/
public class RandomHello {
	/**
	* Uses a RandomHello object to print
	* a random greeting to the console.
	*/
	public static void main(String[] args) {
		RandomHello hi = new RandomHello();
		System.out.println(hi.getGreeting());
	}
	
	/**
	* @return a random greeting from a list of five different greetings.
	*/
	public String getGreeting() {
		Random rng = new Random();
		String[] greetings = new String[5];
		greetings[0] = "Hi";
		greetings[1] = "Hii";
		greetings[2] = "Hiii";
		greetings[3] = "Hiiii";
		greetings[4] = "Hi.";
		return greetings[rng.nextInt(5)];
	}
}
