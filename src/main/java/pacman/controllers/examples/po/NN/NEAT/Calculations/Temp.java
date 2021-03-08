package pacman.controllers.examples.po.NN.NEAT.Calculations;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

public class Temp {

    public static void main(String[] args) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("jeff", true));
        for (int i = 10; i < 36; i++) {
            for (int x = 0; x < 20; x++) {
                writer.writeNext(new String[] {String.valueOf(i)});
            }
        }

        writer.close();
    }
}
