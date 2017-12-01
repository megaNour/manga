package manga;

public class Producer {

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		if(args.length == 0) {
			System.out.println("batch all files");
		}
	}

}
