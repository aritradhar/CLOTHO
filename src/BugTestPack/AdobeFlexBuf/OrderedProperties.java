package BugTestPack.AdobeFlexBuf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

public class OrderedProperties
  extends Properties
  implements Serializable
{
  private Vector keys = new Vector();
  protected Map lines = new HashMap();
  private static final String whitespace = " \t\n\r\f";
  private static String terminators;
  private static final String valterminators = "\n\r\f";
  
  public OrderedProperties() {}
  
  public OrderedProperties(OrderedProperties props)
  {
    Enumeration k = props.keys();
    while (k.hasMoreElements())
    {
      String key = (String)k.nextElement();
      put(key, props.getProperty(key));
    }
  }
  
  public int size()
  {
    return this.keys.size();
  }
  
  public String getProperty(String property)
  {
    Object o = super.get(property);
    if ((o != null) && ((o instanceof String))) {
      return (String)o;
    }
    return null;
  }
  
  public String getProperty(String property, String defaultval)
  {
    Object o = super.get(property);
    String val = null;
    if ((o != null) && ((o instanceof String))) {
      val = (String)o;
    }
    return val != null ? val : defaultval;
  }
  
  public void replaceProperty(String property, String value)
  {
    super.put(property, value);
  }
  
  public Object putProperty(String property, String value)
  {
    return putProperty(null, property, value);
  }
  
  public Object setProperty(String property, String value)
  {
    return putProperty(null, property, value);
  }
  
  public Object putProperty(String comment, String property, String value)
  {
    Object old = super.put(property, value);
    if (old == null) {
      this.keys.addElement(property);
    }
    return old;
  }
  
  public Object get(Object property)
  {
    return super.get(property);
  }
  
  public Object put(Object key, Object value)
  {
    Object old = super.put(key, value);
    if (old == null) {
      this.keys.addElement(key);
    }
    return old;
  }
  
  public Object remove(Object key)
  {
    Object old = super.remove(key);
    if (old != null) {
      this.keys.removeElement(key);
    }
    return old;
  }
  
  public Enumeration keys()
  {
    return this.keys.elements();
  }
  
  public Enumeration propertyNames()
  {
    return this.keys.elements();
  }
  
  public void clear()
  {
    if (super.size() > 0) {
      super.clear();
    }
    this.keys.setSize(0);
  }
  
  public boolean contains(Object value)
  {
    return super.contains(value);
  }
  
  public boolean containsKey(String key)
  {
    return super.containsKey(key);
  }
  
  public Properties getProperties(String propPattern)
  {
    Properties props = new Properties();
    int index = propPattern.indexOf("*");
    if (index == -1)
    {
      String value = getProperty(propPattern);
      if (value != null) {
        props.put(propPattern, value);
      }
    }
    else
    {
      String startsWith = propPattern.substring(0, index);
      String endsWith;
      if (index == propPattern.length() - 1) {
        endsWith = null;
      } else {
        endsWith = propPattern.substring(index + 1);
      }
      Enumeration names = propertyNames();
      while (names.hasMoreElements())
      {
        String name = (String)names.nextElement();
        if (name.startsWith(startsWith)) {
          if (endsWith == null) {
            props.put(name, getProperty(name));
          } else if (name.endsWith(endsWith)) {
            props.put(name, getProperty(name));
          }
        }
      }
    }
    return props;
  }
  
  public void removeProperties(String propPattern)
  {
    int index = propPattern.indexOf("*");
    if (index == -1)
    {
      String value = getProperty(propPattern);
      if (value != null) {
        remove(propPattern);
      }
    }
    else
    {
      String startsWith = propPattern.substring(0, index);
      String endsWith;
      if (index == propPattern.length() - 1) {
        endsWith = null;
      } else {
        endsWith = propPattern.substring(index + 1);
      }
      Vector cle = (Vector)this.keys.clone();
      int size = cle.size();
      for (int i = 0; i < size; i++)
      {
        String name = (String)cle.elementAt(i);
        if (name.startsWith(startsWith)) {
          if (endsWith == null) {
            remove(name);
          } else if (name.endsWith(endsWith)) {
            remove(name);
          }
        }
      }
    }
  }
  
  public void setProperties(Properties props)
  {
    Enumeration names = props.propertyNames();
    while (names.hasMoreElements())
    {
      String name = (String)names.nextElement();
      setProperty(name, props.getProperty(name));
    }
  }
  
  public void load(InputStream is)
    throws IOException
  {
    load2(new BufferedReader(new InputStreamReader(is)));
  }
  
  public void load(Reader reader)
    throws IOException
  {
    load2(new BufferedReader(reader));
  }
  
  public void load2(BufferedReader br)
    throws IOException
  {
    terminators = getTerminators();
    


    StringBuffer buffer = new StringBuffer(100);
    int lineNumber = 0;
    int comment_length = 0;
    String sep = System.getProperty("line.separator");
    int sep_len = sep.length();
    String line;
    while ((line = br.readLine()) != null)
    {
      lineNumber++;
      
      int len = line.length();
      int start = 0;
      if ((lineNumber == 1) && (len > 0) && (line.charAt(0) == 65279)) {
        start = 1;
      }
      while ((start < len) && (" \t\n\r\f".indexOf(line.charAt(start)) != -1)) {
        start++;
      }
      if (line.trim().length() == 0)
      {
        buffer.append(sep);
        comment_length += sep_len;
      }
      else if ((len == 0)|| (line.charAt(start) == '!') || (line.charAt(start) == '#') || (" \t\n\r\f".indexOf(line.charAt(start)) != -1))
      {
        buffer.append(line);
        buffer.append(sep);
        comment_length += len + sep_len;
      }
      else
      {
        if (comment_length != 0) {
          buffer.setLength(comment_length);
        }
        buffer.setLength(0);
        

        buffer.append(line.substring(start));
        while ((line != null) && (line.length() > 1) && (line.charAt(line.length() - 1) == '\\'))
        {
          buffer.setLength(buffer.length() - 1);
          line = br.readLine();
          if (line != null)
          {
            lineNumber++;
            int new_start = 0;
            len = line.length();
            while ((new_start < len) && (" \t\n\r\f".indexOf(line.charAt(new_start)) != -1)) {
              new_start++;
            }
            buffer.append(line.substring(new_start));
          }
        }
        String propLine = buffer.toString();
        String com_key = loadProperty(propLine, lineNumber);
        if ((comment_length != 0) && (com_key != null)) {
          comment_length = 0;
        }
        buffer.setLength(0);
      }
    }
  }
  
  public void store(PrintWriter writer)
  {
    store2(writer, null);
  }
  
  public void store(OutputStream os)
  {
    store2(new PrintWriter(os), null);
  }
  
  public void store(OutputStream os, String header)
  {
    store2(new PrintWriter(os), header);
  }
  
  private void store2(PrintWriter out, String header)
  {
    if (header != null)
    {
      out.println("#" + header);
      out.println("#" + new Date().toString());
    }
    Enumeration ke = this.keys.elements();
    while (ke.hasMoreElements())
    {
      String key = (String)ke.nextElement();
      String value = (String)super.get(key);
      



      out.print(escape(key));
      out.print('=');
      out.println(escape(value));
    }
    out.flush();
  }
  
  private String loadProperty(String prop, int lineNumber)
  {
    int prop_len = prop.length();
    int prop_index = 0;
    for (prop_index = 0; prop_index < prop_len; prop_index++)
    {
      char current = prop.charAt(prop_index);
      if (current == '\\') {
        prop_index++;
      } else {
        if (terminators.indexOf(current) != -1) {
          break;
        }
      }
    }
    String key = prop.substring(0, prop_index);
    key = removeBadChars(prop, key, false);
    key = unescape(key);
    key = key.trim();
    while ((prop_index < prop.length()) && (" \t\n\r\f".indexOf(prop.charAt(prop_index)) != -1)) {
      prop_index++;
    }
    try
    {
      if ((prop.charAt(prop_index) == ':') || (prop.charAt(prop_index) == '='))
      {
        prop_index++;
        while ((prop_index < prop.length()) && (" \t\n\r\f".indexOf(prop.charAt(prop_index)) != -1)) {
          prop_index++;
        }
      }
    }
    catch (StringIndexOutOfBoundsException ex)
    {
      return null;
    }
    int value_start = prop_index;
    for (; prop_index < prop.length(); prop_index++)
    {
      char current = prop.charAt(prop_index);
      if (current == '\\') {
        prop_index++;
      } else {
        if ("\n\r\f".indexOf(current) != -1) {
          break;
        }
      }
    }
    String value = prop.substring(value_start, prop_index);
    value = removeBadChars(prop, value, true);
    value = unescape(value);
    if (!super.containsKey(key)) {
      this.keys.addElement(key);
    }
    super.put(key, value);
    this.lines.put(key, new Integer(lineNumber));
    
    return key;
  }
  
  protected String getTerminators()
  {
    return "=: \t";
  }
  
  protected String removeBadChars(String prop, String string, boolean isValue)
  {
    return string;
  }
  
  private String escape(String string)
  {
    if (string == null) {
      return null;
    }
    StringBuffer buffer = new StringBuffer(string.length() + 10);
    for (int i = 0; i < string.length(); i++)
    {
      char current = string.charAt(i);
      switch (current)
      {
      case '\\': 
        buffer.append('\\');buffer.append('\\');
        break;
      case '\t': 
        buffer.append('\\');buffer.append('t');
        break;
      case '\n': 
        buffer.append('\\');buffer.append('n');
        break;
      case '\r': 
        buffer.append('\\');buffer.append('r');
        break;
      default: 
        if ((current < '\024') || (current > ''))
        {
          buffer.append('\\');
          buffer.append('u');
          buffer.append(toHex(current >> '\f' & 0xF));
          buffer.append(toHex(current >> '\b' & 0xF));
          buffer.append(toHex(current >> '\004' & 0xF));
          buffer.append(toHex(current & 0xF));
        }
        else
        {
          buffer.append(current);
        }
        break;
      }
    }
    return buffer.toString();
  }
  
  private String unescape(String string)
  {
    if (string == null) {
      return null;
    }
    StringBuffer buffer = new StringBuffer(string.length());
    int string_index = 0;
    while (string_index < string.length())
    {
      char add = string.charAt(string_index++);
      if (add == '\\')
      {
        add = string.charAt(string_index++);
        if (add == 'u')
        {
          int unicode = 0;
          for (int i = 0; i < 4; i++)
          {
            add = string.charAt(string_index++);
            switch (add)
            {
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': 
              unicode = (unicode << 4) + add - 48;
              break;
            case 'a': 
            case 'b': 
            case 'c': 
            case 'd': 
            case 'e': 
            case 'f': 
              unicode = (unicode << 4) + 10 + add - 97;
              break;
            case 'A': 
            case 'B': 
            case 'C': 
            case 'D': 
            case 'E': 
            case 'F': 
              unicode = (unicode << 4) + 10 + add - 65;
              break;
            case ':': 
            case ';': 
            case '<': 
            case '=': 
            case '>': 
            case '?': 
            case '@': 
            case 'G': 
            case 'H': 
            case 'I': 
            case 'J': 
            case 'K': 
            case 'L': 
            case 'M': 
            case 'N': 
            case 'O': 
            case 'P': 
            case 'Q': 
            case 'R': 
            case 'S': 
            case 'T': 
            case 'U': 
            case 'V': 
            case 'W': 
            case 'X': 
            case 'Y': 
            case 'Z': 
            case '[': 
            case '\\': 
            case ']': 
            case '^': 
            case '_': 
            case '`': 
            default: 
              //ThreadLocalToolkit.log(new OrderedProperties.MalformedEncoding(this, string));
            }
          }
          add = (char)unicode;
        }
        else
        {
          switch (add)
          {
          case 't': 
            add = '\t';
            break;
          case 'n': 
            add = '\n';
            break;
          case 'r': 
            add = '\r';
            break;
          case 'f': 
            add = '\f';
          }
        }
        buffer.append(add);
      }
      else
      {
        buffer.append(add);
      }
    }
    return buffer.toString();
  }
  
  private static char toHex(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }
  
  private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  
  public void save(OutputStream os, String header)
  {
    store(os);
  }
  
  public static void main(String[] args)
  {
    try
    {
      FileInputStream fis = new FileInputStream("C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\sootOutput\\BugTestPack\\AdobeFlexBuf\\log.txt");
      OrderedProperties p = new OrderedProperties();
      Properties props = new Properties();
      
      p.load2(new BufferedReader(new FileReader("C:\\Users\\Aritra\\workspace\\git\\Repair_Spec\\sootOutput\\BugTestPack\\AdobeFlexBuf\\log.txt")));
      
      p.load(fis);
      fis.close();
      fis = new FileInputStream("log.txt");
      props.load(fis);
      
      p.store(System.out);
      System.out.println("-------");
      props.save(System.out, null);
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
  }
}

