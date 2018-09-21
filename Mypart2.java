package practice;
import java.awt.Dimension;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
// import practice.DemoMain.Lianxi;
import java.awt.Component.*;
import java.awt.image.BufferedImage;
import java.util.Timer; 
import java.util.TimerTask; 
import javax.swing.*;

public class Mypart2 extends JFrame {
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage leftimg;
	BufferedImage rightimg;
	static int width = 512;
	static int height = 512;
	static int lines=0;
    static double speed=0;
	static double fps=0;
	double rotate = 0;
	
	
	public Mypart2() {
		this.setLayout(new BorderLayout());
		RotateLeft rotateleft = new RotateLeft();
		rotateleft.setPreferredSize(new Dimension(512,512));
		RotateRight rotateright = new RotateRight();
		rotateright.setPreferredSize(new Dimension(512,512));
//		rotateleft.setLayout(null);
//		rotateleft.setSize(600, 600);
//		rotateright.setLayout(null);
//		rotateleft.setSize(600, 600);
		new Thread(rotateleft).start();
		new Thread(rotateright).start();
		
		this.add(rotateleft,BorderLayout.WEST);
		this.add(rotateright,BorderLayout.EAST);
		//contentPane.add(BorderLayout.SOUTH, rotateright);
		
		this.pack();
		this.setVisible(true);		
		this.setTitle("original video");
		this.setSize(1250, 1250);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class RotateLeft extends JPanel implements Runnable {
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			
			rotate += speed * 360/30;
			
			System.out.print(speed);
			//System.out.print(rotate);
			BufferedImage img = setImage(width,height,lines);
			if (rotate >= 360) {
				rotate = 0;
			}
			g2d.drawImage(img, 0, 0, null);
		}
	 
		@Override
		public void run() {
			while (true) {
				repaint();
					try {
						Thread.sleep(new Double(1/30 *1000).intValue());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
	
class RotateRight extends JPanel implements Runnable {
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			//double second = 1/(360 * speed / 2)*1000;
			rotate += speed * 360/fps;
			BufferedImage img = setImage(width,height,lines);
			if (rotate >= 360) {
				rotate = 0;
			}
			g2d.drawImage(img, 0, 0, null);
			
		}
	 
		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(new Double(1/fps *1000).intValue());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
	}
	
	
	
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
		
		double curangle= 0 + rotate ;
		for(int i = 1; i <= lines/2; i++) {
			double temp = curangle;
			if(temp >= 180 && temp< 360) {
				temp -= 180;
			}else if(temp >= 360) {
				while(temp > 180) {
					temp -= 360;
				}
			}
			double radians = temp * Math.PI/180;
			double tan = 0;
			 if(temp <= 90) {
				 tan = Math.tan(radians);
				 double y1 =0;
				 if(temp <=45) {
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
				 if((temp-90)<=45) {
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
		return img;
	}
	
	public static void showIms(String[] args){
		
		lines =Integer.parseInt(args[0]);
		speed = Double.parseDouble(args[1]);
		fps = Double.parseDouble(args[2]);
		
		

		System.out.println("The first parameter was: " + lines);
		System.out.println("The second parameter was: " + speed);
		System.out.println("The second parameter was: " + fps);

	}
		
	public static void main(String[] args) throws Exception {
		showIms(args);
		new Mypart2();		
	}
   

}
