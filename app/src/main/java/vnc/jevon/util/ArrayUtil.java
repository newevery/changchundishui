package vnc.jevon.util;

public class ArrayUtil {
	public static <T> boolean contains(T[] array, T t) {
		for (T t1 : array) {
			if (t.equals(t1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(byte[] arr, byte b) {
		for (byte b1 : arr) {
			if (b == b1) {
				return true;
			}
		}
		return false;
	}

}
