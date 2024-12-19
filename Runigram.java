import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);

		image = flippedVertically(tinypic);
		System.out.println();
		print(image);
		
		image = grayScaled(tinypic);
		System.out.println();
		print(image);

		image = scaled(tinypic , 3 , 5);
		System.out.println();
		print(image);

		Color c1 = new Color(100 , 40 , 100);
		Color c2 = new Color(200 , 20 , 40);

		print (blend(c1, c2, 0.25));

		Color[][] ironman = read("ironman.ppm");
		Color[][] thor = read("thor.ppm");
		morph(ironman, thor, 10);
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.

	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		for (int i = 0 ; i < numRows ; i++){
			for (int j = 0 ; j < numCols ; j++){
				int R = in.readInt();
				int G = in.readInt();
				int B = in.readInt();
				Color inArr  = new Color(R , G , B);
				image[i][j] = inArr;
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		int N = image.length;
		int M = image[0].length;

		for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                print(image[i][j]);
                if (j != M - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		int N = image.length;
		int M = image[0].length;
		Color[][] imageH = new Color[N][M];

		for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
				imageH[i][j] = image[i][M - 1 - j];
			}
		}
		
		return imageH;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		int N = image.length;
		int M = image[0].length;
		Color[][] imageV = new Color[N][M];

		for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
				imageV[i][j] = image[N - 1 - i][j];
			}
		}
		
		return imageV;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int R = pixel.getRed();
		int G = pixel.getGreen();
		int B = pixel.getBlue();
		int lum = (int) (0.299 * R + 0.587 * G + 0.114 * B);
		Color grayScale = new Color (lum , lum , lum);
		return grayScale;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		int N = image.length;
		int M = image[0].length;
		Color[][] gray = new Color[N][M];
		for (int i = 0 ; i < N ; i++){
			for (int j = 0 ; j < M ; j++){
				gray[i][j] = luminance(image[i][j]);
			}
		}
		return gray;
	}
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		int xScale = image[0].length;
		int yScale = image.length;
		Color[][] scaled = new Color[height][width];
		for (int i = 0 ; i < height ; i++) {
			for (int j = 0 ; j < width ; j++) {
				int newXindex = j * xScale / width;
				int newYindex = i * yScale / height;
				scaled[i][j] = image[newYindex][newXindex];
			}
		}
		return scaled;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int newRed = (int) (alpha * c1.getRed() + (1 -  alpha) * c2.getRed());
		int newGreen = (int) (alpha * c1.getGreen() + (1 -  alpha) * c2.getGreen());
		int newBlue = (int) (alpha * c1.getBlue() + (1 -  alpha) * c2.getBlue());
		Color blendedColor = new Color (newRed , newGreen , newBlue);
		return blendedColor;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		int N = image1.length;
		int M = image1[0].length;
		Color[][] blended = new Color[N][M];
		for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
				blended[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return blended;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		int N = source.length;
		int M = source[0].length;
		Color[][] morph = source;
		if (M != target[0].length || N != target.length){
			target = scaled(target , M , N);
		}
		setCanvas(morph);
		int i = 0;
		double alpha = 0;
		while (i <= n){
			alpha = (double) (n - i) / n;
			morph = blend(source , target , alpha);
			display(morph);
			StdDraw.pause(500);
			i++;
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

