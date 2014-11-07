package BugTestPack.ApacheCompressBug;

import java.io.File;
import java.util.Date;
import java.util.Locale;

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
public class TarArchiveEntry 
{
	private String name;
	private int mode;
	private int userId;
	private int groupId;
	private long size;
	private long modTime;
	private byte linkFlag;
	private String linkName;
	private String magic;
	private String version;
	private String userName;
	private String groupName;
	private int devMajor;
	private int devMinor;
	private File file;
	public static final int MAX_NAMELEN = 31;
	public static final int DEFAULT_DIR_MODE = 16877;
	public static final int DEFAULT_FILE_MODE = 33188;
	public static final int MILLIS_PER_SECOND = 1000;

	private TarArchiveEntry()
	{
		this.magic = "";
		this.version = "00";
		this.name = "";
		this.linkName = "";

		String user = System.getProperty("user.name", "");
		if (user.length() > 31) {
			user = user.substring(0, 31);
		}
		this.userId = 0;
		this.groupId = 0;
		this.userName = user;
		this.groupName = "";
		this.file = null;
	}
	
	 
	public TarArchiveEntry(String name, byte linkFlag)
	{
		this(name);
		this.linkFlag = linkFlag;
	}

	public TarArchiveEntry(File file)
	{
		this(file, normalizeFileName(file.getPath()));
	}

	public TarArchiveEntry(String name)
	{
		this();

		name = normalizeFileName(name);
		boolean isDir = name.endsWith("/");

		this.devMajor = 0;
		this.devMinor = 0;
		this.name = name;
		this.mode = (isDir ? 16877 : 33188);
		this.linkFlag = (byte) (isDir ? 53 : 48);
		this.userId = 0;
		this.groupId = 0;
		this.size = 0L;
		this.modTime = (new Date().getTime() / 1000L);
		this.linkName = "";
		this.userName = "";
		this.groupName = "";
		this.devMajor = 0;
		this.devMinor = 0;
	}
	
	public TarArchiveEntry(File file, String fileName)
	  {
	    this();
	    
	    this.file = file;
	    
	    this.linkName = "";
	    if (file.isDirectory())
	    {
	      this.mode = 16877;
	      this.linkFlag = 53;
	      
	      int nameLength = fileName.length();
	      if (fileName.charAt(nameLength - 1) != '/') {
	        this.name = (fileName + "/");
	      } else {
	        this.name = fileName;
	      }
	      this.size = 0L;
	    }
	    else
	    {
	      this.mode = 33188;
	      this.linkFlag = 48;
	      this.size = file.length();
	      this.name = fileName;
	    }
	    this.modTime = (file.lastModified() / 1000L);
	    this.devMajor = 0;
	    this.devMinor = 0;
	  }
	
	
	private static String normalizeFileName(String fileName)
	{
		String osname = System.getProperty("os.name").toLowerCase(Locale.US);
		if (osname != null) {
			if (osname.startsWith("windows"))
			{
				if (fileName.length() > 2)
				{
					char ch1 = fileName.charAt(0);
					char ch2 = fileName.charAt(1);
					if ((ch2 == ':') && (((ch1 >= 'a') && (ch1 <= 'z')) || ((ch1 >= 'A') && (ch1 <= 'Z')))) {
						fileName = fileName.substring(2);
					}
				}
			}
			else if (osname.indexOf("netware") > -1)
			{
				int colon = fileName.indexOf(':');
				if (colon != -1) {
					fileName = fileName.substring(colon + 1);
				}
			}
		}
		fileName = fileName.replace(File.separatorChar, '/');
		while (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		return fileName;
	}
	
	
	public static void main(String[] args) 
	{
		
		File f = new File("F:\\");
		new TarArchiveEntry(f);
	}
	
}




