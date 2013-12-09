package massage;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(recursion(1));
		
		System.out.println(add(2));
	}

	public static int recursion(int x) {

		if (x < 2) {
			return 1;
		}
		return recursion(--x) + recursion(--x);

	}

	public static int add(int i) {
		return i = i + 1;
	}

}
