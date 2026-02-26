package com.example.lottery2;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Lottery0Main {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
//        Lottery1Download download = new Lottery1Download();
//        download.exec();

//        Lottery2Predict predict = new Lottery2Predict();
//        predict.exec();

        Lottery3PastStats pastStats = new Lottery3PastStats();
        pastStats.exec();

//        Lottery4Evaluate evaluation = new Lottery4Evaluate();
//        evaluation.exec();
    }
}