package com.example.lottery2;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Lottery3PastStats {
    public void exec() throws IOException {
        int[][] pastResults = Lottery0Utils.loadPastResults();

//        final int limit = 60;
//        scoreStats(pastResults, limit);

        final int limit2 = 352;
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

        int[] preHitCount = new int[33];
        int[] sufHitCount = new int[16];
        int[] preNotHitTime = new int[33];
        int[] sufNotHitTime = new int[16];
        LinkedList<Integer>[] preNotHitTimeHistory = new LinkedList[33];
        for (int i = 0; i < 33; i++) {
            preNotHitTimeHistory[i] = new LinkedList<>();
        }
        LinkedList<Integer>[] sufNotHitTimeHistory = new LinkedList[16];
        for (int i = 0; i < 16; i++) {
            sufNotHitTimeHistory[i] = new LinkedList<>();
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
                System.out.printf("%4s", preNotHitTime[pastResults[i][j] - 1] + ",");
                preCount22[j] = preNotHitTime[pastResults[i][j] - 1];
            }
            System.out.print("  ");
            System.out.printf("%4s", sufNotHitTime[pastResults[i][6] - 1] + ",");
            System.out.print(" ]");

            Arrays.sort(preCount22);
            System.out.print("[");
            for (int j = 0; j < 6; j++) {
                System.out.printf("%4s", preCount22[j] + ",");
            }
            System.out.print("  ");
            System.out.printf("%4s", sufNotHitTime[pastResults[i][6] - 1] + ",");
            System.out.print(" ]");

            // 区间是否命中
            System.out.print(" : [");
            int[] preBallRange = new int[11];
            int[] sufBallRange = new int[4];
            for (int j = 0; j < 6; j++) {
                preBallRange[(pastResults[i][j] - 1) / 3] += 1;
            }
            sufBallRange[((pastResults[i][6] - 1) / 4)] += 1;
            int preEmptyRangeCount = 0;
            for (int j : preBallRange) {
                if (j > 0) {
                    System.out.printf("%4s", "Y" + j + ",");
                } else {
                    System.out.printf("%4s", "N,");
                    preEmptyRangeCount += 1;
                }
            }
            System.out.print(" (" + (11 - preEmptyRangeCount) + "Y" + preEmptyRangeCount + "N), ");
            for (int j : sufBallRange) {
                if (j > 0) {
                    System.out.print(" Y,");
                } else {
                    System.out.print(" N,");
                }
            }
            System.out.print(" ]");

            // 出现次数合计
            System.out.print(" : [");
            for (int j = 0; j < 6; j++) {
                System.out.printf("%4s", preHitCount[pastResults[i][j] - 1] + ",");
            }
            System.out.print(" ");
            System.out.printf("%4s", sufHitCount[pastResults[i][6] - 1] + ",");
            System.out.print(" ]");

            // 连续未现次数(全部球号)
            if (printMore) {
                System.out.print(" : [");
                for (int j = 0; j < 33; j++) {
                    System.out.printf("%4s", preNotHitTime[j] + ",");
                }
                System.out.print("  ");
                for (int j = 0; j < 16; j++) {
                    System.out.printf("%4s", sufNotHitTime[j] + ",");
                }
                System.out.print(" ]");
            }

            for (int j = 0; j < 33; j++) {
                preNotHitTime[j] += 1;
            }
            for (int j = 0; j < 16; j++) {
                sufNotHitTime[j] += 1;
            }
            for (int j = 0; j < 6; j++) {
                final int ballIndex = pastResults[i][j] - 1;
                preHitCount[ballIndex] += 1;
                preNotHitTimeHistory[ballIndex].addFirst(preNotHitTime[ballIndex] - 1);
                preNotHitTime[ballIndex] = 0;
            }
            final int ballIndex = pastResults[i][6] - 1;
            sufHitCount[ballIndex] += 1;
            sufNotHitTimeHistory[ballIndex].addFirst(sufNotHitTime[ballIndex] - 1);
            sufNotHitTime[ballIndex] = 0;

            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.print("啦啦啦啦球号:");
        for (int i = 1; i <= 33; i++) {
            System.out.printf("%4s", i);
        }
        System.out.printf("%4s", " ");
        for (int i = 1; i <= 16; i++) {
            System.out.printf("%4s", i);
        }

        System.out.println();
        System.out.print("连续未现期数:");
        for (int i = 0; i < 33; i++) {
            System.out.printf("%4s", preNotHitTime[i]);
        }
        System.out.printf("%4s", " ");
        for (int i = 0; i < 16; i++) {
            System.out.printf("%4s", sufNotHitTime[i]);
        }
        System.out.println();

        System.out.print("出现次数合计:");
        for (int i = 0; i < 33; i++) {
            System.out.printf("%4s", preHitCount[i]);
        }
        System.out.printf("%4s", " ");
        for (int i = 0; i < 16; i++) {
            System.out.printf("%4s", sufHitCount[i]);
        }
        System.out.println();

        System.out.println("连续未现期数归并: 当前未现期数: 前区球号[再前" + printMore2 + "次合计,出现次数合计]"
                + " ("
                + statsCount + "*2/11=" + (statsCount * 2 / 11) + "~" + ((statsCount * 2) % 11) // 每个球平均出现次数=statsCount*6/33
                + ", "
                + statsCount + "/16=" + (statsCount / 16) + "~" + (statsCount % 16) // 每个球平均出现次数=statsCount*1/16
                + ")"
        );
        System.out.print("-".repeat(3));
        System.out.print("|");
        for (int i = 0; i < 11; i++) {
            System.out.print("-".repeat(22));
            System.out.print("|");
        }
        System.out.println();
        int printBallCount = 0;
        int notHitTime = 0;
        StringBuilder[] rangeBallSb = new StringBuilder[11];
        for (int i = 0; i < 11; i++) {
            rangeBallSb[i] = new StringBuilder(22);
        }
        while (printBallCount < 33) {
            for (int rangeIndex = 0; rangeIndex < 11; rangeIndex++) {
                rangeBallSb[rangeIndex].setLength(0);
                for (int i = 0; i < 3; i++) {
                    final int ballIndex = rangeIndex * 3 + i;
                    final int ballNum = ballIndex + 1;
                    if (preNotHitTime[ballIndex] == notHitTime) {
                        rangeBallSb[rangeIndex].append(" ").append(ballNum);
                        if (printMore2 > 0) {
                            LinkedList<Integer> preList = preNotHitTimeHistory[ballIndex];
                            int sum = 0;
                            for (int j = 0; j < printMore2 && j < preList.size(); j++) {
                                sum += preList.get(j);
                            }
                            rangeBallSb[rangeIndex].append("[")
                                    .append(sum)
                                    .append(",")
                                    .append(preHitCount[ballIndex])
                                    .append("]");
                        }
                        printBallCount += 1;
                    }
                }
            }

            boolean hasBall = false;
            for (StringBuilder sb : rangeBallSb) {
                if (!sb.isEmpty()) {
                    hasBall = true;
                    break;
                }
            }
            if (hasBall) {
                System.out.printf("%4s", notHitTime + ":|");
                for (int i = 0; i < 11; i++) {
                    System.out.printf("%22s", rangeBallSb[i]);
                    System.out.print("|");
                }
                System.out.println();

                System.out.print("-".repeat(3));
                System.out.print("|");
                for (int i = 0; i < 11; i++) {
                    System.out.print("-".repeat(22));
                    System.out.print("|");
                }
                System.out.println();
            }

            notHitTime += 1;
        }

        System.out.println();
    }

}
