public class Assert {
	public static void true(bool condition, string message) {
		if (!condition) throw new AssertionException(message);
	}
	public static void notNull(object o) {
		if (o == null) throw new NullReferenceException();
	}
	public static void cantReach(string message) {
		throw new UnreachableCodeException(message);
	}
	public static void impossibleException(Throwable e, string message) {
		throw new UnreachableCodeException(e, message);
	}
}