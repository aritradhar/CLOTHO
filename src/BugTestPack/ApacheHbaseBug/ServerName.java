package BugTestPack.ApacheHbaseBug;

//*************************************************************************************
//*********************************************************************************** *
//author Aritra Dhar																* *
//MT12004																			* *
//M.TECH CSE																		* * 
//INFORMATION SECURITY																* *
//IIIT-Delhi																		* *	 
//---------------------------------------------------------------------------------	* *																				* *
/////////////////////////////////////////////////									* *
//The program will do the following::::         //									* *
/////////////////////////////////////////////////									* *
//version 1.0																		* *
//*********************************************************************************** * 
//*************************************************************************************
public class ServerName
{
	public static final String SERVERNAME_SEPARATOR = ",";

	private final String servername;
	private final String hostname;
	private final int port;
	private final long startcode;

	public ServerName(final String hostname, final int port, final long startcode) {
		this.hostname = hostname;
		this.port = port;
		this.startcode = startcode;
		this.servername = getServerName(hostname, port, startcode);
	}

	public ServerName(final String serverName) {
		this(parseHostname(serverName), parsePort(serverName),
				parseStartcode(serverName));
	}

	public static String getServerName(String hostName, int port, long startcode) {
		final StringBuilder name = new StringBuilder(hostName.length() + 1 + 5 + 1 + 13);
		name.append(hostName);
		name.append(SERVERNAME_SEPARATOR);
		name.append(port);
		name.append(SERVERNAME_SEPARATOR);
		name.append(startcode);
		return name.toString();
	}


	public static int parsePort(final String serverName) {
		String [] split = serverName.split(SERVERNAME_SEPARATOR);
		return Integer.parseInt(split[1]);
	}

	public static long parseStartcode(final String serverName) {
		int index = serverName.lastIndexOf(SERVERNAME_SEPARATOR);
		return Long.parseLong(serverName.substring(index + 1));
	}

	public static String parseHostname(final String serverName) {
		if (serverName == null || serverName.length() <= 0) {
			throw new IllegalArgumentException("Passed hostname is null or empty");
		}
		int index = serverName.indexOf(SERVERNAME_SEPARATOR);
		return serverName.substring(0, index);
	}
	
	public static void main(String[] args) {
		
		ServerName sn = new ServerName("");
		
	}
}
