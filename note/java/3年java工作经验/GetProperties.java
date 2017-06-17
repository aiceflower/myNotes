public class JavaPoint{
	
	/**
	*¼ÓÔØpropertiesÎÄ¼þ
	*
	*/
	public static Properties Properties(String filePath){
		Properties p  = new Properties();
		InputStream in = null;
		try {
			in = getClass().getClassLoader().getResourceAsStream(filePath);
			p.load(in);
		} catch (java.io.FileNotFoundException fileErr) {
			throw new InvalidConfigException("Config Read Failure ! File Not Found!", fileErr);
		} catch (java.io.IOException ioErr) {
			throw new InvalidConfigException("Config Read Failure ! IOException!", ioErr);
		} finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (Exception e) {
				SysLog.error(e.getMessage());
			}
		}
	}
	
}