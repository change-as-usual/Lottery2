package com.example.lottery2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lottery0Utils {
    public static int comb(int n, int m) {
        if (m > n - m) m = n - m;
        if (m == 0) return 1;
        if (m == 1) return n;
        int d1 = 1;
        for (int i = n; i >= n - m + 1; i--) {
            d1 = i * d1;
        }
        int d2 = 1;
        for (int i = m; i > 1; i--) {
            d2 = i * d2;
        }
        return d1 / d2;
    }

    private static Map<String, Integer> genScoreMap() {
        Map<String, Integer> scoreMap = new HashMap<>(14);
        final int total = comb(33, 6) * comb(16, 1);
        scoreMap.put("6+1", total / (comb(6, 6) * comb(27, 0) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("6+0", total / (comb(6, 6) * comb(27, 0) * comb(1, 0) * comb(15, 1)));
        scoreMap.put("5+1", total / (comb(6, 5) * comb(27, 1) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("5+0", total / (comb(6, 5) * comb(27, 1) * comb(1, 0) * comb(15, 1)));
        scoreMap.put("4+1", total / (comb(6, 4) * comb(27, 2) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("4+0", total / (comb(6, 4) * comb(27, 2) * comb(1, 0) * comb(15, 1)));
        scoreMap.put("3+1", total / (comb(6, 3) * comb(27, 3) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("3+0", total / (comb(6, 3) * comb(27, 3) * comb(1, 0) * comb(15, 1)));
        scoreMap.put("2+1", total / (comb(6, 2) * comb(27, 4) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("2+0", total / (comb(6, 2) * comb(27, 4) * comb(1, 0) * comb(15, 1)));
        scoreMap.put("1+1", total / (comb(6, 1) * comb(27, 5) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("1+0", total / (comb(6, 1) * comb(27, 5) * comb(1, 0) * comb(15, 1)));
        scoreMap.put("0+1", total / (comb(6, 0) * comb(27, 6) * comb(1, 1) * comb(15, 0)));
        scoreMap.put("0+0", 0);
        //System.out.println(scoreMap);
        return scoreMap;
    }

    private static final Map<String, Integer> scoreMap = genScoreMap();

    public static int getScore(String key) {
        Integer value = scoreMap.get(key);
        return value != null ? value : 0;
    }

    private static Map<String, Integer> genPrizeMap() {
        Map<String, Integer> prizeMap = new HashMap<>(14);
        // 一等奖
        prizeMap.put("6+1", 1000_0000);
        // 二等奖
        prizeMap.put("6+0", 30_0000);
        // 三等奖
        prizeMap.put("5+1", 3000);
        // 四等奖
        prizeMap.put("5+0", 200);
        prizeMap.put("4+1", 200);
        // 五等奖
        prizeMap.put("4+0", 10);
        prizeMap.put("3+1", 10);
        // 六等奖
        prizeMap.put("3+0", 5);
        prizeMap.put("2+1", 5);
        prizeMap.put("1+1", 5);
        prizeMap.put("0+1", 5);
        // 无奖
        prizeMap.put("2+0", 0);
        prizeMap.put("1+0", 0);
        prizeMap.put("0+0", 0);
        //System.out.println(prizeMap);
        return prizeMap;
    }

    private static final Map<String, Integer> prizeMap = genPrizeMap();

    public static int getPrize(String key) {
        return getPrize(key, 1);
    }

    public static int getPrize(String key, int multiple) {
        Integer value = prizeMap.get(key);
        int prize = value != null ? value : 0;
        if (prize > 0) {
            prize *= multiple;
        }
        return prize;
    }

    public static String calShotResult(int[] result1, int[] result2) {
        int r1Index = 0;
        int r2Index = 0;
        int preSameCount = 0;
        while (r1Index < 6 && r2Index < 6) {
            if (result1[r1Index] < result2[r2Index]) {
                r1Index++;
            } else if (result1[r1Index] > result2[r2Index]) {
                r2Index++;
            } else {
                r1Index++;
                r2Index++;
                preSameCount++;
            }
        }

        int sufSameCount = (result1[6] == result2[6]) ? 1 : 0;

        return preSameCount + "+" + sufSameCount;
    }

    public static int[][] loadPastResults() throws IOException {
        Path path = Path.of("download_result.json");
        List<String> lines = Files.readAllLines(path);
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            content.append(line);
        }

        Gson gson = new Gson();
        JsonArray jsonResults = gson.fromJson(content.toString(), JsonArray.class);

        int[][] pastResults = new int[jsonResults.size()][7];
        for (int i = 0; i < jsonResults.size(); i++) {
            JsonObject item = (JsonObject) jsonResults.get(i);

            String red = item.get("red").getAsString();
            String[] redSS = red.split(",");
            for (byte j = 0; j < 6; j++) {
                pastResults[i][j] = Integer.parseInt(redSS[j]);
            }

            String blue = item.get("blue").getAsString();
            pastResults[i][6] = Integer.parseInt(blue);
        }

        System.out.println("load past result success"
                + ", size=" + jsonResults.size()
                + ", first=" + Arrays.toString(pastResults[0])
        );

        return pastResults;
    }

}
