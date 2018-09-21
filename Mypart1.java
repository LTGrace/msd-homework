//package practice;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

public class Mypart1 {
	JFrame frame;
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage leftimg;
	BufferedImage rightimg;
	int width = 512;
	int height = 512;

	// Draws a black line on the given buffered image from the pixel defined by (x1, y1) to (x2, y2)
	public void drawLine(BufferedImage image,  double x1, double y1, double x2, double y2) {
		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		Shape l = new Line2D.Double(x1, y1, x2, y2);
		g.draw(l);
		g.drawImage(image, 0, 0, null);
	}
	
	public BufferedImage setImage(int width, int height,int lines) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int ind = 0;
		for(int y = 0; y < height; y++){

			for(int x = 0; x < width; x++){

				// byte a = (byte) 255;
				byte r = (byte) 255;
				byte g = (byte) 255;
				byte b = (byte) 255;

				int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
				//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
				img.setRGB(x,y,pix);
				ind++;
			}
		}

		drawLine(img, 0, 0, width-1, 0);				// top edge
		drawLine(img, 0, 0, 0, height-1);				// left edge
		drawLine(img, 0, height-1, width-1, height-1);	// bottom edge
		drawLine(img, width-1, height-1, width-1, 0); 	// right edge
		
		//draw lines
		if(lines != 0 && lines%2 == 0) {
			
			drawLine(img,0,0.5*height-1,width-1,0.5*height-1);
			
			double curangle = 360.0 / lines;
			 
			 while(curangle < 180) {
				 System.out.print(curangle+" ");
				 double radians = curangle * Math.PI/180;
				 double tan = 0;
				 if(curangle <= 90) {
					 tan = Math.tan(radians);
					 double y1 =0;
					 if(curangle <=45) {
						 y1 = 0.5*width*tan;
						 drawLine(img,0,0.5*height-y1-1,width-1,0.5*height+y1-1);
					 }
					 else {
						 y1 = 0.5*height/tan;
						 drawLine(img,0.5*width-y1-1,0,0.5*width+y1-1,height-1);
					 } 
				 }else {
					 tan = Math.tan(radians-Math.PI/2);
					 double x1=0;
					 if((curangle-90)<=45) {
						 x1 = 0.5 * height*tan;
						 drawLine(img,0.5 * width + x1,0,0.5*width-x1,height);
					 }
					 else {
						 x1= 0.5 * height/tan;
						 drawLine(img,width-1,0.5*height-x1-1,0,0.5*height+x1);
					 }
				 }
				 curangle+=360.0/lines;
			 }
		}
		return img;
		
	}
	
	public BufferedImage antiAliasing(BufferedImage img) {
		BufferedImage image = img;
		float[] format = {1f/9f,1f/9f,1f/9f,1f/9f,1f/9f,1f/9f,1f/9f,1f/9f,1f/9f};
		Kernel kernel = new Kernel(3,3,format);
		BufferedImageOp ImageOp = new ConvolveOp(kernel);
		image = ImageOp.filter(image, null);
		return image;
	}

	public void showIms(String[] args){

		// Read a parameter from command line
		int lines = Integer.parseInt(args[0]);
		double scalefactor = Double.parseDouble(args[1]);
		String aliasing = args[2];
		
		System.out.println("The first parameter was: " + lines);
		System.out.println("The second parameter was: " + scalefactor);
		//System.out.println("The second parameter was: " + param2);

		// Initialize a plain white image
		
		leftimg = setImage(width,height,lines);
		rightimg = setImage(new Double(width/scalefactor).intValue(),new Double(height/scalefactor).intValue(),lines);
		if(aliasing.equals("1")){
			
			rightimg = antiAliasing(rightimg);
			
		}
		//System.out.print(new Double(width/scalefactor).intValue());
		//System.out.print(new Double(height/scalefactor).intValue());
		

		// Use labels to display the images
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		JLabel lbText1 = new JLabel("Original image (Left)");
		lbText1.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lbText2 = new JLabel("Image after modification (Right)");
		lbText2.setHorizontalAlignment(SwingConstants.CENTER);
		lbIm1 = new JLabel(new ImageIcon(leftimg));
		lbIm2 = new JLabel(new ImageIcon(rightimg));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(lbText1, c);
//
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		frame.getContentPane().add(lbText2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		frame.getContentPane().add(lbIm2, c);
		
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Mypart1 ren = new Mypart1();
		ren.showIms(args);
	}


}
