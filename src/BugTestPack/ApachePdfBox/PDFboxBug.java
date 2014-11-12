package BugTestPack.ApachePdfBox;

import java.io.IOException;
import java.util.Scanner;

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
public class PDFboxBug 
{
	 private static final String FDF_HEADER = "%FDF-";
	 private static final String PDF_HEADER = "%PDF-";
	 
	 
	 public static void main(String[] args) throws IOException 
	 {
		new PDFboxBug().parseHeader();
	}
	private void parseHeader() throws IOException
    {
        // read first line
		Scanner s = new Scanner(System.in);
        String header = s.next();
        // some pdf-documents are broken and the pdf-version is in one of the following lines
        if ((header.indexOf( PDF_HEADER ) == -1) && (header.indexOf( FDF_HEADER ) == -1))
        {
            header = "";
            while ((header.indexOf( PDF_HEADER ) == -1) && (header.indexOf( FDF_HEADER ) == -1))
            {
                // if a line starts with a digit, it has to be the first one with data in it
                if ((Character.isDigit(header.charAt(0))))
                {
                    break;
                }
                header = s.next();
            }
        }

        // nothing found
        if ((header.indexOf( PDF_HEADER ) == -1) && (header.indexOf( FDF_HEADER ) == -1))
        {
            throw new IOException( "Error: Header doesn't contain versioninfo" );
        }
        
        //sometimes there are some garbage bytes in the header before the header
        //actually starts, so lets try to find the header first.
        int headerStart = header.indexOf( PDF_HEADER );
        if (headerStart == -1)
        {
            headerStart = header.indexOf(FDF_HEADER);
        }

        //greater than zero because if it is zero then
        //there is no point of trimming
        if ( headerStart > 0 )
        {
            //trim off any leading characters
            header = header.substring( headerStart, header.length() );
        }

        /*
         * This is used if there is garbage after the header on the same line
         */
        /*
        if (header.startsWith(PDF_HEADER)) 
        {
            if(!header.matches(PDF_HEADER + "\\d.\\d")) 
            {
                String headerGarbage = header.substring(PDF_HEADER.length()+3, header.length()) + "\n";
                header = header.substring(0, PDF_HEADER.length()+3);
                pdfSource.unread(headerGarbage.getBytes());
            }
        }
        else 
        {
            if(!header.matches(FDF_HEADER + "\\d.\\d")) 
            {
                String headerGarbage = header.substring(FDF_HEADER.length()+3, header.length()) + "\n";
                header = header.substring(0, FDF_HEADER.length()+3);
                pdfSource.unread(headerGarbage.getBytes());
            }
        }
        document.setHeaderString(header);
        
        try
        {
            if (header.startsWith( PDF_HEADER )) 
            {
                float pdfVersion = Float. parseFloat(
                        header.substring( PDF_HEADER.length(), Math.min( header.length(), PDF_HEADER .length()+3) ) );
                document.setVersion( pdfVersion );
            }
            else 
            {
                float pdfVersion = Float. parseFloat(
                        header.substring( FDF_HEADER.length(), Math.min( header.length(), FDF_HEADER.length()+3) ) );
                document.setVersion( pdfVersion );
            }
        }
        catch ( NumberFormatException e )
        {
            throw new IOException( "Error getting pdf version:" + e );
        } */
    } 
}
