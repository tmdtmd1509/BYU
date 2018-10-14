import java.io.*;
import java.lang.*;
import java.util.*;

//import static jdk.nashorn.internal.objects.NativeMath.max;


public class ImageEditor {
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = args[0];
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+" );//skip comments and whitespace
        scanner.next(); //P3

        int width = scanner.nextInt();//set width
        int height = scanner.nextInt();//set height

        Image picture = new Image(height, width);

        scanner.nextInt();//255

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                picture.aPixel[i][j].setR(Integer.valueOf(scanner.nextInt()));
                picture.aPixel[i][j].setG(Integer.valueOf(scanner.nextInt()));
                picture.aPixel[i][j].setB(Integer.valueOf(scanner.nextInt()));
            }
        }

        ImageEditor imageEditor = new ImageEditor();
        File newFile = new File(args[1]);
        PrintWriter writer = new PrintWriter(newFile);

        writer.write("P3\n");
        writer.write(width+" "+height+"\n");
        writer.write("255\n");


        if (args[2].equals("invert")) {
            picture = imageEditor.inverse(picture);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(String.valueOf(picture.aPixel[i][j].getR())+"\n");
                    writer.write(String.valueOf(picture.aPixel[i][j].getG())+"\n");
                    writer.write(String.valueOf(picture.aPixel[i][j].getB())+"\n");
                }
            }
        }
        else if (args[2].equals("grayscale")) {
            picture = imageEditor.grayscale(picture);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(picture.aPixel[i][j].getR()+"\n");
                    writer.write(picture.aPixel[i][j].getG()+"\n");
                    writer.write(picture.aPixel[i][j].getB()+"\n");
                }
            }
        }
        else if (args[2].equals("emboss")) {
            picture = imageEditor.emboss(picture);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(picture.aPixel[i][j].getR()+"\n");
                    writer.write(picture.aPixel[i][j].getG()+"\n");
                    writer.write(picture.aPixel[i][j].getB()+"\n");
                }
            }
        }
        else if (args[2].equals("motionblur")) {
            int blurNum = 0;
            blurNum = Integer.parseInt(args[3]);
            picture = imageEditor.motionBlur(picture, blurNum);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.write(picture.aPixel[i][j].getR()+"\n");
                    writer.write(picture.aPixel[i][j].getG()+"\n");
                    writer.write(picture.aPixel[i][j].getB()+"\n");
                }
            }
        }

        scanner.close();
        writer.close();
    }

    public Image inverse(Image image) {
        Image picture = new Image(image.getH(), image.getW());
        for(int i = 0; i < image.getH(); i++) {
            for(int j = 0; j < image.getW(); j++) {
                picture.aPixel[i][j].setR(255 - Integer.valueOf(image.aPixel[i][j].getR()));
                picture.aPixel[i][j].setG(255 - Integer.valueOf(image.aPixel[i][j].getG()));
                picture.aPixel[i][j].setB(255 - Integer.valueOf(image.aPixel[i][j].getB()));
            }
        }
        return picture;
    }

    public Image grayscale(Image image) {
        Image picture = new Image(image.getH(), image.getW());

        for(int i = 0; i < image.getH(); i++) {
            for(int j = 0; j < image.getW(); j++) {
                int red = image.aPixel[i][j].getR();
                int green = image.aPixel[i][j].getG();
                int blue = image.aPixel[i][j].getB();
                int average  = (red+green+blue)/3;

                picture.aPixel[i][j].setR(average);
                picture.aPixel[i][j].setG(average);
                picture.aPixel[i][j].setB(average);
            }
        }
        return picture;
    }

    public Image emboss(Image image) {
        Image picture = new Image(image.getH(), image.getW());

        for(int i = 0; i < image.getH(); i++) {
            for(int j = 0; j < image.getW(); j++) {
                int value;
                if (i == 0 || j == 0) {
                    value = 128;
                }
                else {
                    int redDiff = image.aPixel[i][j].getR()-image.aPixel[i-1][j-1].getR();
                    int greenDiff = image.aPixel[i][j].getG()-image.aPixel[i-1][j-1].getG();
                    int blueDiff = image.aPixel[i][j].getB()-image.aPixel[i-1][j-1].getB();
                    int maxValue;

                    if(Math.abs(redDiff) >= Math.abs(greenDiff)) {
                        maxValue = redDiff;
                    }
                    else {
                        maxValue = greenDiff;
                    }

                    if(Math.abs(maxValue) < Math.abs(blueDiff)) {
                        maxValue = blueDiff;
                        //System.out.println("Max: " + maxValue);
                    }
                    value = maxValue + 128;

                    if(value < 0) {
                        value = 0;
                    }
                    else if(value > 255) {
                        value = 255;
                    }

                }
                picture.aPixel[i][j].setR(value);
                picture.aPixel[i][j].setG(value);
                picture.aPixel[i][j].setB(value);
            }
        }
        return picture;
    }

    public Image motionBlur(Image image, int blurNum) {
        Image picture = new Image(image.getH(), image.getW());
        int n = blurNum;

        for(int i = 0; i < image.getH(); i++) {
            for(int j = 0; j < image.getW(); j++) {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;
/*
                if((i + n) >= image.getW()) {
                    for(int k = i; k < image.getW(); k++) {
                        sumRed += image.aPixel[j][k].getR();
                        sumGreen += image.aPixel[j][k].getG();
                        sumBlue += image.aPixel[j][k].getB();
                    }
                    sumRed = sumRed/(image.getW()-i);
                    sumGreen = sumGreen/(image.getW()-i);
                    sumBlue = sumBlue/(image.getW()-i);
                }
                else {
                    for(int k = i; k < (i + n); k++) {
                        sumRed += image.aPixel[j][k].getR();
                        sumGreen += image.aPixel[j][k].getG();
                        sumBlue += image.aPixel[j][k].getB();
                    }
                    sumRed = sumRed/n;
                    sumGreen = sumGreen/n;
                    sumBlue = sumBlue/n;
                }

                picture.aPixel[j][i].setR(sumRed);
                picture.aPixel[j][i].setG(sumGreen);
                picture.aPixel[j][i].setB(sumBlue);
                */
                if((j + n) >= image.getW()) {
                    for(int k = j; k < image.getW(); k++) {
                        sumRed += image.aPixel[i][k].getR();
                        sumGreen += image.aPixel[i][k].getG();
                        sumBlue += image.aPixel[i][k].getB();
                    }
                    sumRed = sumRed/(image.getW()-j);
                    sumGreen = sumGreen/(image.getW()-j);
                    sumBlue = sumBlue/(image.getW()-j);
                }
                else {
                    for(int k = j; k < (j + n); k++) {
                        sumRed += image.aPixel[i][k].getR();
                        sumGreen += image.aPixel[i][k].getG();
                        sumBlue += image.aPixel[i][k].getB();
                    }
                    sumRed = sumRed/n;
                    sumGreen = sumGreen/n;
                    sumBlue = sumBlue/n;
                }

                picture.aPixel[i][j].setR(sumRed);
                picture.aPixel[i][j].setG(sumGreen);
                picture.aPixel[i][j].setB(sumBlue);
            }
        }
        return picture;
    }
    /*
        catch (FileNotFoundException e) {
            System.out.println("no such file");
        }*/
}