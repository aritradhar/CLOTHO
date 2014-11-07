package BugTestPack.ApacheLoggingComponentBug;

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
			final StringBuilder name)
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
			throw new FileSystemException("vfs.provider.local/not-absolute-file-name.error", uri, null);
		}
		name.delete(0, startPos);
		// Look for drive name
		final String driveName = extractDrivePrefix(name);
		if (driveName != null)
		{
			return driveName;
		}
		// Look for UNC name
		if (startPos < 2)
		{
			throw new FileSystemException("vfs.provider.local/not-absolute-file-name.error", uri, null);
		}
		return "//" + extractUNCPrefix(uri, name);
					}


	/**
	 * Extracts a drive prefix from a path.  Leading '/' chars have been removed.
	 */
	private String extractDrivePrefix(final StringBuilder name)
	{
		// Looking for <letter> ':' '/'
		if (name.length() < 3)
		{
			// Too short
			return null;
		}
		char ch = name.charAt(0);
		if (ch == '/' || ch == ':')
		{
			// Missing drive letter
			return null;
		}
		if (name.charAt(1) != ':')
		{
			// Missing ':'
			return null;
		}
		if (name.charAt(2) != '/')
		{
			// Missing separator
			return null;
		}

		String prefix = name.substring(0, 2);
		name.delete(0, 2);

		return prefix.intern();
	}

	/**
	 * Extracts a UNC name from a path.  Leading '/' chars have been removed.
	 */
	private String extractUNCPrefix(final String uri,
			final StringBuilder name)
					throws FileSystemException
					{
		// Looking for <name> '/' <name> ( '/' | <end> )

		// Look for first separator
		int maxpos = name.length();
		int pos = 0;
		for (; pos < maxpos && name.charAt(pos) != '/'; pos++)
		{
		}
		pos++;
		if (pos >= maxpos)
		{
			throw new FileSystemException("vfs.provider.local/missing-share-name.error", uri, null);
		}

		// Now have <name> '/'
		int startShareName = pos;
		for (; pos < maxpos && name.charAt(pos) != '/'; pos++)
		{
		}
		if (pos == startShareName)
		{
			throw new FileSystemException("vfs.provider.local/missing-share-name.error", uri, null);
		}

		// Now have <name> '/' <name> ( '/' | <end> )
		String prefix = name.substring(0, pos);
		name.delete(0, pos);
		return prefix;
		}
	
	
	public static void main(String[] args) throws FileSystemException 
	{
		new VFSBug().extractWindowsRootPrefix("123", new StringBuilder("////////"));
	}
}
