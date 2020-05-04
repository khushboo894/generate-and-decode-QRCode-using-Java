package com.demo.QRCodeDemo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeDemo {
	private static Logger logger = Logger.getLogger(QRCodeDemo.class.getName());
	private static final String QR_CODE_IMAGE_PATH = "/QRCode.png";
	private static final Path filePath = FileSystems.getDefault().getPath(System.getProperty("user.dir").concat(QR_CODE_IMAGE_PATH));
	private static void generateQRCodeImage(String text, int width, int height)
			throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
	}
	private static String decodeQRCode(Path qrCodePath) throws IOException
	{
		File file=qrCodePath.toFile();
		BufferedImage bufferedImage = ImageIO.read(file);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            logger.debug("There is no QR code in the image");
            return null;
        }
	}

	public static void main(String args[])
	{
		try {
			BasicConfigurator.configure();
			
			generateQRCodeImage("Khushboo Kumbhar", 350, 350);
			String decodedText=decodeQRCode(filePath);
			logger.info(decodedText);
		} catch (WriterException e) {
			
			logger.debug("Could not generate QR Code, WriterException :: "+e.getMessage());
        } catch (IOException e) {
        	logger.debug("Could not generate QR Code, IOException :: " + e.getMessage());
        }
	}

}
