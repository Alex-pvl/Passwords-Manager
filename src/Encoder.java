import java.util.*;

public class Encoder implements Encodeable {
	private final String original;
	private final String base64;
	private final int hash;
	private String result;

	public Encoder(String original) {
		this.original = original;
		this.base64 = Base64.getEncoder().encodeToString(original.getBytes());
		this.hash = original.hashCode();
		encode();
	}

	@Override
	public void encode() {
		result =
				(new Random().nextInt(100,10000)) +
				base64.substring(0, 11) +
				(hash % (int) Math.pow(10, new Random().nextInt(3, 5)));
	}

	public String getOriginal() {
		return original;
	}

	public String getResult() {
		return result;
	}
}
