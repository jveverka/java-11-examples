public class Display {

	static {
		System.out.println("Loading Library...");
	        System.loadLibrary("Display");
		System.out.println("Library loaded."); 
	}

	public native void setPixel(int x, int y, int r, int g, int b);

	public static void main(String[] args) {
	       Display display = new Display();
	       display.setPixel(0,0, 0, 255, 0);
	       System.out.println("done.");
	}

}

