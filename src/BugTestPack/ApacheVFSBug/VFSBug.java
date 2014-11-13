package BugTestPack.ApacheVFSBug;

import java.nio.file.FileSystemException;

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
public class VFSBug 
{
	private String extractWindowsRootPrefix(final String uri,
			String name)
					throws FileSystemException
					{
		// Looking for:
		// ('/'){0, 3} <letter> ':' '/'
		// ['/'] '//' <name> '/' <name> ( '/' | <end> )
		// Skip over first 4 (unc) leading '/' chars
		int startPos = 0;
		final int maxlen = Math.min(4, name.length());
		for (; startPos < maxlen && name.charAt(startPos) == '/'; startPos++)
		{
		}
		if (startPos == maxlen && name.length() > startPos && name.charAt(startPos + 1) == '/') /* This Line Highlighted part is added */
		{
			// Too many '/'
			throw new FileSystemException("vfs.provider.local/not-absolute-file-name.error");
		}
		//name = name.substring(startPos+1);
		// Look for drive name
		final String driveName = " ".concat(" ");
		if (driveName != null)
		{
			return driveName;
		}
		// Look for UNC name
		if (startPos < 2)
		{
			throw new FileSystemException("vfs.provider.local/not-absolute-file-name.error");
		}
		return "//" ;
		}
	
	public static void main(String[] args) throws FileSystemException 
	{
		new VFSBug().extractWindowsRootPrefix("", "/////");
	}
}
