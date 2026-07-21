package com.example.lottery2;

import com.example.lottery2.net.Lottery26083;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Lottery4Evaluate {

    public void exec() throws IOException {
        scorePrefLotteryBasePastOrNet();
//        statsNetLottery();
    }

    private void scorePrefLotteryBasePastOrNet() throws IOException {
        Lottery[] prefLotteries = new Lottery[]{
//                new Lottery.FixUnfixLottery(
//                        new int[]{4, 17, 20, 21, 25,},
//                        new int[]{1, 2, 3, 28, 33},
//                        new int[]{10}
//                ),
                new Lottery.FixUnfixLottery(
                        new int[]{1, 10, 13, 24, 28},
                        new int[]{32, 33},
                        new int[]{9, 10, 11}
                ),


                //待打票
                new Lottery.SingleLottery(new int[][]{
                }),
                //已打票
                new Lottery.SingleLottery(new int[][]{
                }),
        };
        scorePrefLotteryBasePast(prefLotteries);
//        scorePrefLotteryBaseNet(prefLotteries);
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
                for (Lottery netLottery : Lottery26083.lotteries) {
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
        int[] sufNumCount = new int[16 + 1];

        for (Lottery lottery : Lottery26083.lotteries) {
            for (int[] one : lottery.results) {
                for (int i = 0; i < 6; i++) {
                    preNumCount[one[i]] += lottery.multiple;
                }
                sufNumCount[one[6]] += lottery.multiple;
            }
        }

        System.out.println("前区:");
        Map<Integer, Integer> preMap = new HashMap<>(33);
        for (int i = 1; i <= 33; i++) {
            System.out.printf("%12s",  i + "=" + preNumCount[i]);
            if (i % 11 == 0) {
                System.out.println();
            }
            preMap.put(i, preNumCount[i]);
        }
        System.out.println();
        Object[] preSortResult = preMap.entrySet().toArray();
        Arrays.sort(preSortResult, (o1, o2) -> {
            Map.Entry<Integer, Integer> o11 = (Map.Entry<Integer, Integer>) o1;
            Map.Entry<Integer, Integer> o22 = (Map.Entry<Integer, Integer>) o2;
            return o11.getValue() - o22.getValue();
        });
        for (int i = 0; i < preSortResult.length; i++) {
            Map.Entry<Integer, Integer> item = (Map.Entry<Integer, Integer>) preSortResult[i];
            System.out.printf("%12s", item);
            if (i % 11 == 10) {
                System.out.println();
            }
        }

        System.out.println("后区:");
        Map<Integer, Integer> sufOutMap = new HashMap<>(16);
        for (int i = 1; i <= 16; i++) {
            System.out.printf("%12s",  i + "=" + sufNumCount[i]);
            if (i % 8 == 0) {
                System.out.println();
            }
            sufOutMap.put(i, sufNumCount[i]);
        }
        System.out.println();
        Object[] sufSortResult = sufOutMap.entrySet().toArray();
        Arrays.sort(sufSortResult, (o1, o2) -> {
            Map.Entry<Integer, Integer> o11 = (Map.Entry<Integer, Integer>) o1;
            Map.Entry<Integer, Integer> o22 = (Map.Entry<Integer, Integer>) o2;
            return o11.getValue() - o22.getValue();
        });
        for (int i = 0; i < sufSortResult.length; i++) {
            Map.Entry<Integer, Integer> item = (Map.Entry<Integer, Integer>) sufSortResult[i];
            System.out.printf("%12s", item);
            if (i % 8 == 7) {
                System.out.println();
            }
        }
    }
}
