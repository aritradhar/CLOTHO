package BugTestPack.ApacheDerbyBug;

public class DerbyBug {

	/**
	 * Minion of getStatementToken. If the input string starts with an
	 * identifier consisting of letters only (like "select", "update"..),return
	 * it, else return supplied string.
	 * @see #getStatementToken
	 * @param sql input string
	 * @return identifier or unmodified string
	 */
	public static String isolateAnyInitialIdentifier (String sql) {
		int length = sql.length();

		if (length == 0) {
			return sql;
		}

		int idx = 1;
		char next = sql.charAt(idx);
		while (idx < length) {
			if (!Character.isLetter(next)) {
				break;
			}
			/*if (idx == length -1) {
				return sql;
			}*/
			next = sql.charAt(++idx);
		}

		return sql.substring(0, idx);
	}

	public static String isolateAnyInitialIdentifierBug2 (String sql) {
		int idx = 0;
		int length = sql.length();

		if (length == 0) {
			return sql;
		}

		char next = sql.charAt(idx);

		if (!Character.isLetter(next)) {
			return sql;
		}

		while (idx < length) {
			if (!Character.isLetter(next)) {
				break;
			}
			next = sql.charAt(++idx);
		}
		
		return sql.substring(0, idx);
	}

	public static void main(String[] args) {
		System.out.println("haha");
		DerbyBug.isolateAnyInitialIdentifier("abc");
		//DerbyBug.isolateAnyInitialIdentifierBug2("");

	}
}
