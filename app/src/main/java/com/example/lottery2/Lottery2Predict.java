package com.example.lottery2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Lottery2Predict {

    public void exec() throws IOException {
        final long startTime = System.currentTimeMillis();
        predictNextLottery(48);
        final long endTime = System.currentTimeMillis();
        System.out.println("total cost time: " + (endTime - startTime));
    }

    private static class ResultEntry {
        public long score = Long.MAX_VALUE;
        public int[] result = new int[7];
    }

    private void predictNextLottery(final int predictCount) throws IOException {
        if (predictCount < 1 || predictCount > 200) {
            System.out.println("predictCount should in [1, 200]");
            return;
        }

        PriorityQueue<ResultEntry> nextResults = new PriorityQueue<>(
                predictCount,
                (o1, o2) -> Long.compare(o2.score, o1.score)
        );

        final int[][] pastResults = Lottery0Utils.loadPastResults();

        int[] itResult = new int[7];
        for (int i1 = 1; i1 <= 28; i1++) {
            itResult[0] = i1;
            for (int i2 = i1 + 1; i2 <= 29; i2++) {
                itResult[1] = i2;
                for (int i3 = i2 + 1; i3 <= 30; i3++) {
                    itResult[2] = i3;
                    for (int i4 = i3 + 1; i4 <= 31; i4++) {
                        itResult[3] = i4;
                        for (int i5 = i4 + 1; i5 <= 32; i5++) {
                            itResult[4] = i5;
                            for (int i6 = i5 + 1; i6 <= 33; i6++) {
                                itResult[5] = i6;
                                for (int j = 1; j <= 16; j++) {
                                    itResult[6] = j;

                                    long score = 0;
                                    for (int[] pastResult : pastResults) {
                                        String scoreKey = Lottery0Utils.calShotResult(itResult, pastResult);
                                        score += Lottery0Utils.getScore(scoreKey);
                                    }

                                    if (nextResults.size() < predictCount) {
                                        ResultEntry e = new ResultEntry();
                                        e.score = score;
                                        System.arraycopy(itResult, 0, e.result, 0, 7);
                                        nextResults.offer(e);
                                    } else {
                                        ResultEntry e = nextResults.peek();
                                        if (e.score > score) {
                                            System.out.println("new result:" + Arrays.toString(itResult) + ", " + score);
                                            nextResults.poll();
                                            e.score = score;
                                            System.arraycopy(itResult, 0, e.result, 0, 7);
                                            nextResults.offer(e);
                                        } else if (e.score == score) {
                                            System.out.println("equal result:" + Arrays.toString(itResult) + ", " + score);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("final result: ");
        nextResults.stream()
                .sorted(Comparator.comparingLong(o -> o.score))
                .forEach(e -> System.out.println(Arrays.toString(e.result) + ", " + e.score));
        System.out.println("--------------------------");
    }
}
