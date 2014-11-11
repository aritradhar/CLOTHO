package BugTestPack.ApacheSlingBug;

public class SlingBug {

	static Object parsePossiblyTypedValue(String value)
    {
      boolean explicitMultiValue = false;
      
      int hintEnd = -1;
      //if (value.isEmpty()) {		// This is the Patch
        //return value;
      //}
      String rawValue;
      if (value.charAt(0) != '{')
      {
        rawValue = value;
      }
      else
      {
        hintEnd = value.indexOf('}');
        rawValue = value.substring(hintEnd + 1);
      }
      String[] values;
      if ((rawValue.length() > 0) && (rawValue.charAt(0) == '['))
      {
        if (rawValue.charAt(rawValue.length() - 1) != ']') {
          throw new IllegalArgumentException("Invalid multi-valued property definition : '" + rawValue + "'");
        }
        String rawValues = rawValue.substring(1, rawValue.length() - 1);
        values = rawValues.split(",");
        explicitMultiValue = true;
      }
      else
      {
        values = new String[] { rawValue };
      }
      if (hintEnd == -1)
      {
        if ((values.length == 1) && (!explicitMultiValue)) {
          return values[0];
        }
        return values;
      }
      String rawHint = value.substring(1, hintEnd);
      throw new IllegalArgumentException("Unknown typeHint value '" + rawHint + "'");
    }	
	
	public static void main(String[] args) {
		SlingBug.parsePossiblyTypedValue("a");
		SlingBug.parsePossiblyTypedValue("");	//Bug
	}
}
