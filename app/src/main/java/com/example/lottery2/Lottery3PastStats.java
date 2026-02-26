package com.example.lottery2;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Lottery3PastStats {
    public void exec() throws IOException {
        int[][] pastResults = Lottery0Utils.loadPastResults();

        final int limit = 60;
        scoreStats(pastResults, limit);

        final int limit2 = 188;
        numStats(pastResults, limit2, false, 1);
//        numStats2(pastResults, limit2);
    }

    private void scoreStats(int[][] pastResults, int limit) {
        int[] itResult = new int[7];
        for (int i = 0; i < pastResults.length && i < limit; i++) {
            System.arraycopy(pastResults[i], 0, itResult, 0, 7);

            long score = 0;
            for (int j = i + 1; j < pastResults.length; j++) {
                String scoreKey = Lottery0Utils.calShotResult(itResult, pastResults[j]);
                score += Lottery0Utils.getScore(scoreKey);
            }

            System.out.println(Arrays.toString(itResult) + ": " + score);
        }
    }

    private void numStats(int[][] pastResults, int statsCount, boolean printMore, int printMore2) {
        statsCount = Math.min(pastResults.length, statsCount);

        int[] preCount = new int[33];
        int[] sufCount = new int[16];
        int[] preCount2 = new int[33];
        int[] sufCount2 = new int[16];
        LinkedList<Integer>[] preCount3 = new LinkedList[33];
        for (int i = 0; i < 33; i++) {
            preCount3[i] = new LinkedList<>();
        }
        LinkedList<Integer>[] sufCount3 = new LinkedList[16];
        for (int i = 0; i < 16; i++) {
            sufCount3[i] = new LinkedList<>();
        }
        System.out.println();
        System.out.println("每一期[号码]:[连续未现次数]:[区间是否命中]:[出现次数合计]");
        for (int i = statsCount - 1, c = 1; i >= 0; i--, c++) {
            System.out.printf("%4s", c);
            // 号码
            System.out.print(" : [");
            for (int j = 0; j < 6; j++) {
                System.out.printf("%4s", pastResults[i][j] + ",");
            }
            System.out.print(" ");
            System.out.printf("%4s", pastResults[i][6] + "");
            System.out.print(" ]");

            // 连续未现次数
            int[] preCount22 = new int[6];
            System.out.print(" : [");
            for (int j = 0; j < 6; j++) {
                System.out.printf("%4s", preCount2[pastResults[i][j] - 1] + ",");
                preCount22[j] = preCount2[pastResults[i][j] - 1];
            }
            System.out.print(" ");
            System.out.printf("%4s", sufCount2[pastResults[i][6] - 1] + ",");
            System.out.print(" ]");

            Arrays.sort(preCount22);
            System.out.print("[");
            for (int j = 0; j < 6; j++) {
                System.out.printf("%4s", preCount22[j] + ",");
            }
            System.out.print(" ]");

            // 区间是否命中
            System.out.print(" : [");
            int[] ballRange = new int[7];
            for (int j = 0; j < 6; j++) {
                ballRange[(pastResults[i][j] - 1) / 11] += 1;
            }
            ballRange[3 + ((pastResults[i][6] - 1) / 4)] += 1;
            int emptyRangeCount = 0;
            for (int j = 0; j < 3; j++) {
                if (ballRange[j] > 0) {
                    System.out.printf("%4s", "Y" + ballRange[j] + ",");
                } else {
                    System.out.printf("%4s", "N,");
                    emptyRangeCount += 1;
                }
            }
            System.out.print(" (" + (3 - emptyRangeCount) + "Y" + emptyRangeCount + "N), ");
            for (int j = 3; j < 7; j++) {
                if (ballRange[j] > 0) {
                    System.out.print(" Y,");
                } else {
                    System.out.print(" N,");
                }
            }
            System.out.print(" ]");

            // 出现次数合计
            System.out.print(" : [");
            for (int j = 0; j < 6; j++) {
                System.out.printf("%4s", preCount[pastResults[i][j] - 1] + ",");
            }
            System.out.print(" ");
            System.out.printf("%4s", sufCount[pastResults[i][6] - 1] + ",");
            System.out.print(" ]");

            // 连续未现次数(全部球号)
            if (printMore) {
                System.out.print(" : [");
                for (int j = 0; j < 33; j++) {
                    System.out.printf("%4s", preCount2[j] + ",");
                }
                System.out.print("  ");
                for (int j = 0; j < 16; j++) {
                    System.out.printf("%4s", sufCount2[j] + ",");
                }
                System.out.print(" ]");
            }

            for (int j = 0; j < 33; j++) {
                preCount2[j] += 1;
            }
            for (int j = 0; j < 16; j++) {
                sufCount2[j] += 1;
            }
            for (int j = 0; j < 6; j++) {
                final int ballIndex = pastResults[i][j] - 1;
                preCount[ballIndex] += 1;
                preCount3[ballIndex].addFirst(preCount2[ballIndex] - 1);
                preCount2[ballIndex] = 0;
            }
            final int ballIndex = pastResults[i][6] - 1;
            sufCount[ballIndex] += 1;
            sufCount3[ballIndex].addFirst(sufCount2[ballIndex] - 1);
            sufCount2[ballIndex] = 0;

            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.print("啦啦啦啦球号:");
        for (int i = 1; i <= 33; i++) {
            System.out.print("\t" + i);
        }
        System.out.print("\t");
        for (int i = 1; i <= 16; i++) {
            System.out.print("\t" + i);
        }

        System.out.println();
        System.out.print("连续未现期数:");
        for (int i = 0; i < 33; i++) {
            System.out.print("\t" + preCount2[i]);
        }
        System.out.print("\t");
        for (int i = 0; i < 16; i++) {
            System.out.print("\t" + sufCount2[i]);
        }
        System.out.println();

        System.out.print("出现次数合计:");
        for (int i = 0; i < 33; i++) {
            System.out.print("\t" + preCount[i]);
        }
        System.out.print("\t");
        for (int i = 0; i < 16; i++) {
            System.out.print("\t" + sufCount[i]);
        }
        System.out.println();

        System.out.println("连续未现期数归并: 当前未现期数: 前区球号[再前" + printMore2 + "次合计,出现次数合计] | 后区");
        int printBallCount2 = 0;
        int loopTime2 = 0;
        while (printBallCount2 < (49 /*33 + 16*/)) {
            boolean hasPrintLoopTime = false;
            for (int i = 0; i < 33; i++) {
                if (preCount2[i] == loopTime2) {
                    if (!hasPrintLoopTime) {
                        System.out.printf("%7s", loopTime2 + ":");
                        hasPrintLoopTime = true;
                    }
                    System.out.print("  " + (i + 1));
                    if (printMore2 > 0) {
                        LinkedList<Integer> preList = preCount3[i];
                        int sum = 0;
                        for (int j = 0; j < printMore2 && j < preList.size(); j++) {
                            sum += preList.get(j);
                        }
                        System.out.print("[" + sum + "," + preCount[i] + "]");
                    }
                    printBallCount2 += 1;
                }
            }
            if (hasPrintLoopTime) {
                System.out.print("  |");
            }
            for (int i = 0; i < 16; i++) {
                if (sufCount2[i] == loopTime2) {
                    if (!hasPrintLoopTime) {
                        System.out.printf("%14s", loopTime2 + ":  xx  |");
                        hasPrintLoopTime = true;
                    }
                    System.out.print("  " + (i + 1));
                    if (printMore2 > 0) {
                        LinkedList<Integer> sufList = sufCount3[i];
                        int sum = 0;
                        for (int j = 0; j < printMore2 && j < sufList.size(); j++) {
                            sum += sufList.get(j);
                        }
                        System.out.print("[" + sum + "," + sufCount[i] + "]");
                    }
                    printBallCount2 += 1;
                }
            }
            loopTime2 += 1;
            if (hasPrintLoopTime) {
                System.out.println();
            }
        }

        System.out.println("出现次数合计归并:");
        int printBallCount = 0;
        int loopTime = 0;
        while (printBallCount < (49 /*33 + 16*/)) {
            boolean hasPrintLoopTime = false;
            for (int i = 0; i < 33; i++) {
                if (preCount[i] == loopTime) {
                    if (!hasPrintLoopTime) {
                        System.out.print("\t" + loopTime + ":");
                        hasPrintLoopTime = true;
                    }
                    System.out.print("\t" + (i + 1));
                    printBallCount += 1;
                }
            }
            if (hasPrintLoopTime) {
                System.out.print("\t|");
            }
            for (int i = 0; i < 16; i++) {
                if (sufCount[i] == loopTime) {
                    if (!hasPrintLoopTime) {
                        System.out.print("\t" + loopTime + ":\txx\t|");
                        hasPrintLoopTime = true;
                    }
                    System.out.print("\t" + (i + 1));
                    printBallCount += 1;
                }
            }
            loopTime += 1;
            if (hasPrintLoopTime) {
                System.out.println();
            }
        }

        System.out.println();
    }

}
