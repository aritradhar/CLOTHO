package BugTestPack.ApacheSOAPbug;

import java.util.Hashtable;

import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.soap.util.mime.MimeUtils;

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
public class SoapBug
{
	
	/*  81 */   protected Hashtable bag = new Hashtable();
	/*  82 */   protected ClassLoader loader = null;
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*  90 */   protected boolean rootPartSet = false;
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*  96 */   private static final String[] ignoreHeaders = { "Message-ID", "Mime-Version" };
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/* 104 */   protected MimeMultipart parts = null;
	/*     */   
	/*     */ 
	/*     */   private static final String DEFAULT_BASEURI = "thismessage:/";
	
	
	
	
	
	/*     */   public MimeBodyPart getBodyPart(String paramString)
	/*     */   {
	/* 152 */     if (this.parts == null) {
	/* 153 */       return null;
	/*     */     }
	/*     */     try {
	/* 156 */       return (MimeBodyPart)this.parts.getBodyPart(paramString);
	/*     */     }
	/*     */     catch (MessagingException localMessagingException) {
	/* 159 */       return null;
	/*     */     }
	/*     */     catch (NullPointerException localNullPointerException) {
	/* 162 */       return null;
	/*     */     }
	/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
	/* 165 */     return null;
	/*     */   }
	
	/*     */   public MimeBodyPart getBodyPart(int paramInt)
	/*     */   {
	/* 127 */     if (this.parts == null) {
	/* 128 */       return null;
	/*     */     }
	/*     */     try {
	/* 131 */       return (MimeBodyPart)this.parts.getBodyPart(paramInt);
	/*     */     }
	/*     */     catch (MessagingException localMessagingException) {
	/* 134 */       throw new IndexOutOfBoundsException(localMessagingException.getMessage());
	/*     */     }
	/*     */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
	/* 137 */       return null;
	/*     */     }
	/*     */     catch (NullPointerException localNullPointerException) {}
	/* 140 */     return null;
	/*     */   }
	
	/*     */ 
	/*     */   public MimeBodyPart findPartByLocation(String paramString)
	/*     */   {
	/*     */     try
	/*     */     {
	/* 250 */       String str1 = getBaseURI();
	/* 251 */       paramString = normalizeURI(paramString, str1);
	/* 252 */       if (paramString == null)
	/* 253 */         return null;
	/* 254 */       for (int i = 0; i < this.parts.getCount(); i++) {
	/* 255 */         MimeBodyPart localMimeBodyPart = getBodyPart(i);
	/* 256 */         if (localMimeBodyPart != null) {
	/* 257 */           String str2 = localMimeBodyPart.getHeader(
	/* 258 */             "Content-Location", null);
	/* 259 */           if (paramString.equals(normalizeURI(str2, str1))) {
	/* 260 */             return localMimeBodyPart;
	/*     */           }
	/*     */         }
	/*     */       }
	/*     */     } catch (MessagingException localMessagingException) {}
	/* 265 */     return null;
	/*     */   }
	/*     */   public String getBaseURI()
	/*     */   {
	/* 222 */     String str = null;
	/*     */     try {
	/* 224 */       str = getRootPart().getHeader(
	/* 225 */         "Content-Location", null);
	/*     */     }
	/*     */     catch (MessagingException localMessagingException) {}
	/* 228 */     if (str == null)
	/* 229 */       str = "thismessage:/";
	/* 230 */     return str;
	/*     */   }
	
	/*     */ 
	/*     */   public MimeBodyPart getRootPart()
	/*     */     throws MessagingException
	/*     */   {
	/* 438 */     MimeBodyPart localMimeBodyPart = null;
	/* 439 */     if (getCount() > 1) {
	/* 440 */       String str = new ContentType(
	/* 441 */         this.parts.getContentType()).getParameter("start");
	/* 442 */       if (str != null)
	/* 443 */         localMimeBodyPart = getBodyPart(MimeUtils.decode(str));
	/*     */     }
	/* 445 */     if (localMimeBodyPart == null)
	/* 446 */       localMimeBodyPart = getBodyPart(0);
	/* 447 */     return localMimeBodyPart;
	/*     */   }
	
	/*     */   public int getCount()
			/*     */     throws MessagingException
			/*     */   {
			/* 478 */     if (this.parts == null) {
			/* 479 */       return 0;
			/*     */     }
			/* 481 */     return this.parts.getCount();
			/*     */   }
	/*     */   
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */ 
	/*     */   private String normalizeURI(String paramString1, String paramString2)
	/*     */   {
	/* 275 */     int i = paramString1.indexOf(':');
	/* 276 */     if (i >= 0) {
	/* 277 */       String str = paramString1.substring(0, i);
	/* 278 */       if ((str.indexOf('/') == -1) && 
	/* 279 */         (str.indexOf('?') == -1) && 
	/* 280 */         (str.indexOf('#') == -1))
	/*     */       {
	/* 282 */         return paramString1;
	/*     */       }
	/*     */     }
	/* 285 */     return paramString2 + paramString1;
				}
	
	public MimeBodyPart findBodyPart(String uri) {
        if (parts == null || uri == null) {
            return null;
        }
        try {
            if (uri.length() > 4 &&
               uri.substring(0, 4).equalsIgnoreCase("cid:")) {
                // It's a Content-ID. URLDecode and lookup.
                String cid = MimeUtils.decode(uri.substring(4));
                // References are not supposed to be inside brackets, but be
                // tolerant.
                if (cid.charAt(0) != '<' || cid.charAt(cid.length()) != '>')  // This is the line
                    cid = '<' + cid + '>';

                try {
                    return (MimeBodyPart)parts.getBodyPart(cid);
                } catch(NullPointerException npe) {
                }
            } else {
                // It's a URI.
                return findPartByLocation(uri);
            }

        } catch (MessagingException me) {
        } catch (NullPointerException npe) {
        }
        return null;
    }


	public static void main(String[] args) 
	{
		SoapBug sb = new SoapBug();
		sb.parts = new MimeMultipart(DEFAULT_BASEURI);
		sb.findBodyPart("cid:<bla");
	}

}
