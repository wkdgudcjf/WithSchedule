package action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

public class ActionUtil {
	static String getInputString(HttpServletRequest request) throws IOException
	{
	    InputStream is = request.getInputStream();
	    if (is != null)
	    {
	        Writer writer = new StringWriter();

	        char[] buffer = new char[request.getContentLength()];

	        try
	        {
	            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

	            int n;
	            while ((n = reader.read(buffer)) != -1)
	            {
	                writer.write(buffer, 0, n);
	            }
	            reader.close();
	        }
	        finally
	        {
	            is.close();
	        }
	        
	        String result = writer.toString();
	        
	        writer.close();
	        return result;
	    }
	    else
	    {
	        return "";
	    }
	}
}
