package com.example.lottery2;

import com.example.lottery2.net.Lottery26021;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Lottery4Evaluate {

    public void exec() throws IOException {
        scorePrefLotteryBasePastOrNet();
        statsNetLottery();
    }

    private void scorePrefLotteryBasePastOrNet() throws IOException {
        Lottery[] prefLotteries = new Lottery[]{
        };
        scorePrefLotteryBasePast(prefLotteries);
        scorePrefLotteryBaseNet(prefLotteries);
    }

    private void scorePrefLotteryBasePast(Lottery[] prefLotteries) throws IOException {
        System.out.println("偏好号码得分(BasePast)：");
        int[][] pastResults = Lottery0Utils.loadPastResults();
        for (Lottery lottery : prefLotteries) {
            for (int[] one : lottery.results) {
                long score = 0;
                int prize = 0;
                for (int[] pastResult : pastResults) {
                    String key = Lottery0Utils.calShotResult(one, pastResult);
                    score += Lottery0Utils.getScore(key);
                    prize += Lottery0Utils.getPrize(key);
                }
                System.out.println(Arrays.toString(one) + ": " + score + ", " + prize);
            }
            System.out.println();
        }
    }

    private void scorePrefLotteryBaseNet(Lottery[] prefLotteries) {
        System.out.println("偏好号码奖金(BaseNet)：");
        for (Lottery prefLottery : prefLotteries) {
            for (int[] one : prefLottery.results) {
                long score = 0;
                for (Lottery netLottery : Lottery26021.lotteries) {
                    for (int[] netResult : netLottery.results) {
                        String prizeKey = Lottery0Utils.calShotResult(one, netResult);
                        score += Lottery0Utils.getPrize(prizeKey, netLottery.multiple);
                    }
                }
                System.out.println(Arrays.toString(one) + ": " + score);
            }
            System.out.println();
        }
    }

    private void statsNetLottery() {
        System.out.println("网络票统计:");

        int[] preNumCount = new int[33 + 1];
        int[][] preNumCount2 = new int[6][33 + 1];
        int[] sufNumCount = new int[16 + 1];

        for (Lottery lottery : Lottery26021.lotteries) {
            for (int[] one : lottery.results) {
                for (int i = 0; i < 6; i++) {
                    preNumCount[one[i]] += lottery.multiple;
                    preNumCount2[i][one[i]] += lottery.multiple;
                }
                sufNumCount[one[6]] += lottery.multiple;
            }
        }

        System.out.println("前区:");
        for (int i = 1; i <= 33; i++) {
            System.out.printf("%12s", i + "(" + preNumCount[i] + ")");
            if (i % 11 == 0) {
                System.out.println();
            }
        }
        for (int j = 0; j < 6; j++) {
            System.out.println("pp" + (j + 1) + ":");
            for (int i = 1; i <= 33; i++) {
                System.out.printf("%12s", i + "(" + preNumCount2[j][i] + ")");
                if (i % 11 == 0) {
                    System.out.println();
                }
            }
        }

        System.out.println("后区:");
        for (int i = 1; i <= 16; i++) {
            System.out.printf("%12s", i + "(" + sufNumCount[i] + ")");
            if (i % 8 == 0) {
                System.out.println();
            }
        }
    }
}
