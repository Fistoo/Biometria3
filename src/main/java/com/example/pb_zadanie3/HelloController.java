package com.example.pb_zadanie3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class HelloController {

    private int width;
    private int height;
    private File selectedFile;

    @FXML
    private Label test;

    @FXML
    private ImageView imageOrginal;

    @FXML
    private ImageView niblackImage;

    @FXML
    protected void onLoadButtonClick() throws IOException {
        final FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.home") + "\\Pictures"));
        selectedFile = fc.showOpenDialog(test.getScene().getWindow());
        Image img = new Image(selectedFile.getPath());
        imageOrginal.setImage(img);
        imageOrginal.setFitWidth(200);
        imageOrginal.setFitHeight(150);
        width = (int)img.getWidth();
        height = (int)img.getHeight();
        Niblack(15,-1.5);
    }

    @FXML
    void initialize() {

    }


    public void Niblack (int window, double k1) throws IOException {
        BufferedImage imageO = ImageIO.read(selectedFile);
        BufferedImage img = ImageIO.read(selectedFile);

        int iB, iG, iR, rgb, sum, num;
        double area, mean, standardDeviation, average;

        int pixelRgb, pixelR, pixelG, pixelB, pixelA;
        double NiBlack;


        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {

                iR = iG = iB = 0;
                for(int ji = -window; ji < window; ji++){
                    for(int jj = -window; jj < window; jj++){
                        if((column + ji >= 0 && column + ji < width)&&(row + jj >= 0 && row + jj < height)){
                            rgb = imageO.getRGB(column + ji, row + jj);
                            iR += rgb & 0xff0000 >> 16;
                            iG += rgb & 0x00ff00 >> 8;
                            iB += rgb & 0xff;
                        }
                    }
                }
                sum = (iR + iG + iB) / 3;
                area = (window)*(window)*4;
                standardDeviation = 0.0;
                mean = sum/area;
                for(int ji = -window; ji < window; ji++){
                    for(int jj = -window; jj < window; jj++){
                        if(column + ji >= 0 && column + ji < width){
                            if(row + jj >= 0 && row + jj < height){
                                rgb = imageO.getRGB(column + ji, row + jj);
                                iR = rgb & 0xff0000 >> 16;
                                iG = rgb & 0x00ff00 >> 8;
                                iB = rgb & 0xff;
                                num = (iR + iG + iB) / 3;
                                standardDeviation += Math.pow(num - mean, 2);
                            }
                        }
                    }
                }
                standardDeviation = Math.sqrt(standardDeviation/area);
                pixelRgb = img.getRGB(column, row);
                pixelR = pixelRgb & 0xff0000 >> 16;
                pixelG = pixelRgb & 0x00ff00 >> 8;
                pixelB = pixelRgb & 0xff;
                pixelA = (pixelR + pixelG + pixelB) / 3;
                average = sum / area;

                NiBlack = average + k1 * standardDeviation;

                if(pixelA > NiBlack)
                    img.setRGB(column, row, 0xffffffff);
                else
                    img.setRGB(column, row, 0xff000000);
            }
        }



            niblackImage.setImage(convertToFxImage(img));
            niblackImage.setFitHeight(150);
            niblackImage.setFitWidth(200);
        }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }



}